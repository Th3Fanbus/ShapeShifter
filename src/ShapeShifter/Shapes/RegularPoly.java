package ShapeShifter.Shapes;

import ShapeShifter.Figura;

/**
 * Representation of a regular-sided polygon.
 *
 * @author usuario
 */
public class RegularPoly extends Figura {

    private double base;
    private int sides;

    /**
     * Construct a regular polygon given its base side length and number of sides.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param b Base side length.
     * @param s Number of sides.
     * 
     * @throws IllegalArgumentException If the number of sides is less than three.
     */
    public RegularPoly(int x, int y, double b, int s) {
        super(x, y);

        if (s < 3) {
            throw new IllegalArgumentException("Number of sides must be equal or greater than three");
        }

        base = b;
        sides = s;
    }

    /**
     * Construct a regular polygon inside a circle given its number of sides.
     *
     * @param c Circle to use as base.
     * @param s Number of sides.
     * 
     * @throws IllegalArgumentException If the number of sides is less than three.
     */
    public RegularPoly(Circle c, int s) {
        this(c.getPosX(), c.getPosY(), c.getRadius(), s);
    }

    /**
     * Return a Triangle that can be used to draw the polygon.
     *
     * @return That triangle.
     */
    public Triangle getGenerator() {
        return new Triangle(getPosX(), getPosY(), base, apothem(), base / 2d);
    }

    @Override
    public double area() {
        return perimetre() * apothem() / 2d;
    }

    @Override
    public double perimetre() {
        return base * sides;
    }

    @Override
    public String toString() {
        return String.format("POLY\t {base: %4.4f, sides: %d}", base, sides);
    }

    /**
     * @Getter
     * @return base
     */
    public double getBase() {
        return base;
    }

    /**
     * @Setter
     * @param b base
     */
    public void setBase(double b) {
        base = b;
    }

    /**
     * @Getter
     * @return sides
     */
    public int getSides() {
        return sides;
    }

    /**
     * @Setter
     * @param s sides
     *
     * @throws IllegalArgumentException If the number of sides is less than three.
     */
    public void setSides(int s) {
        if (s < 3) {
            throw new IllegalArgumentException("Number of sides must be equal or greater than three");
        }
        sides = s;
    }

    private double alfa() {
        return (Math.PI - beta()) / 2d;
    }

    private double beta() {
        return (2d * Math.PI / sides);
    }

    private double apothem() {
        return Math.tan(alfa()) * base / 2d;
    }
}
