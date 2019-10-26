package ShapeShifter;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

import static java.util.concurrent.ThreadLocalRandom.*;

/**
 * A cursed utility class. Will try to instantiate anything you throw at it.
 * 
 * @author usuario
 */
public final class Reflector {

    private static final int NUM_PRIMITIVES = 9;
    /* Map classes to suppliers of random values. */
    private static final HashMap<Class<?>, Supplier<?>> generators = new HashMap<>(NUM_PRIMITIVES);

    static {
        register(Boolean.TYPE, () -> current().nextBoolean());
        register(Byte.TYPE, () -> (byte) Int());
        register(Character.TYPE, () -> (char) Int());
        register(Double.TYPE, () -> Dbl());
        register(Float.TYPE, () -> 3f / current().nextFloat());
        register(Integer.TYPE, () -> Int());
        register(Long.TYPE, () -> current().nextLong());
        register(Short.TYPE, () -> (short) Int());
        register(Void.TYPE, () -> null);

        assert generators.size() == NUM_PRIMITIVES;

        /* Register any additional type generators here. */
    }

    /* Aliased names for the most used random value generator functions. */
    public static double Dbl() {
        return 3d / current().nextDouble();
    }

    public static int Int() {
        return current().nextInt();
    }

    public static int Int(int bound) {
        return current().nextInt(bound);
    }

    /**
     * Specify which generator to use to instantiate a certain class.
     *
     * @param clazz The class to assign the generator to.
     * @param supplier The supplier of new instances of said class.
     */
    public static void register(Class<?> clazz, Supplier<?> supplier) {
        generators.put(clazz, supplier);
    }

    /**
     * Try to create a new instance of a given class.
     * 
     * @param <T> Generic type.
     * @param clazz The class to attempt to instantiate.
     * @return A new instance of said class, or null, or magic smoke.
     */
    public static <T> T newInstance(Class<T> clazz) {
        assert clazz != null;

        /* Arrays need to be handled differently. */
        if (clazz.isArray()) {
            return clazz.cast(nextArray(clazz.getComponentType()));
        }
        return nextThing(clazz);
    }

    private static <T> T nextThing(Class<T> clazz) {

        /* If we know about this type, get a new instance from its Supplier. */
        if (generators.containsKey(clazz)) {
            return clazz.cast(generators.get(clazz).get());
        }

        /* FIXME: Can recurse infinitely and blow up the stack. I don't care. */
        return spawn(clazz);
    }

    /* Spawn an array of the given type with a random size. */
    private static Object nextArray(Class<?> clazz) {
        assert clazz != null;

        final int n = Int(50) + 1;

        /* Java uses Objects to store arrays instantiated through reflection. */
        Object arr = Array.newInstance(clazz, n);
        for (int i = 0; i < n; i++) {
            Array.set(arr, i, newInstance(clazz));
        }

        return arr;
    }

    /* Create a new instance of T and then return it, or else return null. */
    private static <T> T spawn(Class<T> clazz) {
        assert clazz != null;

        /* Cannot construct interfaces. */
        if (clazz.isInterface()) {
            return null;
        }

        /* To create a new T, try all constructors until one works. */
        for (Constructor<?> ctor : clazz.getConstructors()) {

            /* Get the arguments this constructor requires. */
            Object[] args = Arrays.stream(ctor.getParameterTypes()).map(Reflector::newInstance).toArray();

            Object result;

            /* (breathes in and braces for impact with a wild Throwable) */
            try {
                result = ctor.newInstance(args);

                if (result != null) {
                    /* Rise, my glorious creation. Rise! */
                    return clazz.cast(result);
                }
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException ex) {
                /* We're trying to use every constructor of a random class until
                 * one of them works, so errors are completely expected. */
            }
        }
        /* Oops, bad luck. No new object for us. */
        return null;
    }

    private Reflector() {
    }
}
