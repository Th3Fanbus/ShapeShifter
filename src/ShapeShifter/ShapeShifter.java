package ShapeShifter;

import ShapeShifter.Shapes.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static ShapeShifter.Reflector.*;

/**
 * Main class of this project.
 *
 * @author usuario
 */
public final class ShapeShifter {
    
    /* Computations to do with the elements inside the array of Figures. */
    private static enum Stats {
        SumArea     (Figura::area,      (ds) -> ds.sum()),
        MaxArea     (Figura::area,      (ds) -> ds.max().orElse(Double.NaN)),
        MinArea     (Figura::area,      (ds) -> ds.min().orElse(Double.NaN)),
        SumPerimetre(Figura::perimetre, (ds) -> ds.sum()),
        MaxPerimetre(Figura::perimetre, (ds) -> ds.max().orElse(Double.NaN)),
        MinPerimetre(Figura::perimetre, (ds) -> ds.min().orElse(Double.NaN));

        private final ToDoubleFunction<Figura> mapper;
        private final Function<DoubleStream, Double> reducer;

        private Stats(ToDoubleFunction<Figura> m, Function<DoubleStream, Double> r) {
            mapper  = m;
            reducer = r;
        }
        
        public Double doMapReduce(Stream<Figura> s) {
            return reducer.apply(s.mapToDouble(mapper));
        }
    }
    
    /* List of classes to use to generate new instances of Figura. */
    private static enum Shapes {
        CIRCLE     (Circle.class,      () -> new Circle     (Int(), Int(), Dbl())),
        RECTANGLE  (Rectangle.class,   () -> new Rectangle  (Int(), Int(), Dbl(), Dbl())),
        REGULARPOLY(RegularPoly.class, () -> new RegularPoly(Int(), Int(), Dbl(), Int(21) + 3)),
        SECTOR     (Sector.class,      () -> new Sector     (Int(), Int(), Dbl(), Dbl())),
        SQUARE     (Square.class,      () -> new Square     (Int(), Int(), Dbl())),
        TRIANGLE   (Triangle.class,    () -> new Triangle   (Int(), Int(), Dbl(), Dbl(), Dbl()));

        public final Class<?> clazz;
        public final Supplier<?> generator;

        private Shapes(Class<?> c, Supplier<?> s) {
            clazz = c;
            generator = s;
        }

        public static Supplier<?> getRandomGenerator() {
            return values()[Int(values().length)].generator;
        }

        public static Shapes valueOf(Figura f) {
            assert f != null;
            
            final Class<? extends Figura> clazz = f.getClass();
            for (var s : Shapes.values()) {
                if (s.clazz.equals(clazz)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("No enum value corresponds to " + clazz.getCanonicalName());
        }

        @Override
        public String toString() {
            return clazz.getSimpleName();
        }
    }
    
    /* Debug switch: Enable rather verbose debug messages. */
    private static final boolean DO_DEBUG_MSG = false;

    /* Number of Figura elements to spawn. */
    private static final int COUNT = 10000;

    /* Shape array. This reference won't be replaced, so it is made final. */
    private static final Figura[] shapes;

    /* Stores the statistics for all the shapes in the array. */
    private static final EnumMap<Stats, Double> globals = new EnumMap<>(Stats.class);
    
    /* Stores the statistics for each subtype of shape in the array. */
    private static final EnumMap<Stats, EnumMap<Shapes, Double>> results = new EnumMap<>(Stats.class);

    /**
     * Main function of this program.
     *
     * @param args ignored
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        
        /* Collect some statistics of the shape array. */
        System.out.print("Now obtaining statistics reports... ");
        final long reportStart = System.nanoTime();
        {
            final Map<Shapes, List<Figura>> shapeMap = Arrays.stream(shapes).collect(Collectors.groupingBy(Shapes::valueOf));
            
            for (Stats stat : Stats.values()) {
                EnumMap<Shapes, Double> tmp = new EnumMap<>(Shapes.class);
                shapeMap.forEach((type, list) -> tmp.put(type, stat.doMapReduce(list.stream())));
                results.put(stat, tmp);
                globals.put(stat, stat.doMapReduce(Arrays.stream(shapes)));
            }
        }
        final long reportDelta = System.nanoTime() - reportStart;
        System.out.printf("done in %.4f ms. \n\n", reportDelta / 1000000d);
        
        /* Now print them out. */
        System.out.println("Statistics Reports:");
        for (var stat : Stats.values()) {
            System.out.printf("%s: \t%f\n", stat, globals.get(stat));
            for (var type : Shapes.values()) {
                System.out.printf("\t%s: \t%f\n", type, results.get(stat).get(type));
            }
            System.out.println();
        }
        dumpArray("State before sorting:");
        
        /* Sort the shape array using its natural ordering (by area). */
        System.out.print("Now sorting the array by shape area... ");
        final long sortStart = System.nanoTime();
        {
            Arrays.parallelSort(shapes);
        }
        final long sortEnd = System.nanoTime() - sortStart;
        System.out.printf("done in %.4f ms. \n\n", sortEnd / 1000000d);

        dumpArray("State after sorting:");
    }
    
    /* Print an identifying prologue, and then the entire shape array. */
    private static void dumpArray(String prologue) {
        if (DO_DEBUG_MSG) {
            System.out.println(prologue);
            for (var s : shapes) {
                System.out.printf("%12.4f\t area of:\t %s\n", s.area(), s);
            }
            System.out.println();
        }
    }

    /** Reports whether asserts are enabled, for debug purposes. */
    private static void areAssertsOn() {
        boolean assertsOn = false;
        /* Intentional side effect in assert:
         * The assignment will only take place if asserts are enabled, so
         * that the boolean variable will end up reflecting that. */
        assert (assertsOn = true);

        System.out.println("Asserts enabled: " + assertsOn);
    }

    /* Static initializer, runs before main(). */
    static {
        areAssertsOn();
        
        /* Assert assignability and then register generators for each shape. */
        for (Shapes type : Shapes.values()) {
            assert Figura.class.isAssignableFrom(type.clazz);
            Reflector.register(type.clazz, type.generator);
        }

        /* DANGER: Using member reference here somehow breaks randomness! */
        Reflector.register(Figura.class, () -> Shapes.getRandomGenerator().get());

        /* Now that we set up the Reflector, initialize the shape array. */
        System.out.print("Initializing random array... ");
        final long initStart = System.nanoTime();
        {
            /* We can now create the array of random shapes. This must be done
             * inside a static block because the array reference is final. */
            shapes = IntStream.range(0, COUNT)
                              .mapToObj((arg) -> (Figura) Reflector.newInstance(Figura.class))
                              .toArray(Figura[]::new);
        }
        final long initDelta = System.nanoTime() - initStart;
        System.out.printf("done in %.4f ms. ", initDelta / 1000000d);
        System.out.printf("Length is %d elements.\n\n", shapes.length);
    }
}
