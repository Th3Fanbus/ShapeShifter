package ShapeShifter.Shapes;

import ShapeShifter.Figura;

/**
 * Representation of a triangle.
 *
 * @author usuario
 */
public class Triangle extends Figura {

    private static double hypotenuse(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    /** The triangle's base. */
    protected double base;

    /** The triangle's height. */
    protected double height;

    /** The triangle's skew, which is the horizontal deviation of the height with respect to one of the base's vertices. */
    protected double skew;

    /**
     * Construct a triangle given its base and height.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param b Base side length.
     * @param h Height side length.
     * @param s Skew of the height.
     */
    public Triangle(int x, int y, double b, double h, double s) {
        super(x, y);

        base = b;
        height = h;
        skew = s;
    }

    @Override
    public double area() {
        return base * height / 2d;
    }

    @Override
    public double perimetre() {
        return base + hypotenuse(skew, height) + hypotenuse(base - skew, height);
    }

    @Override
    public String toString() {
        return String.format("TRNGL\t {base: %4.4f, height: %4.4f, skew: %4.4f}", base, height, skew);
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
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @Setter
     * @param h height
     */
    public void setHeight(double h) {
        height = h;
    }

    /**
     * @Getter
     * @return skew
     */
    public double getSkew() {
        return skew;
    }

    /**
     * @Setter
     * @param s skew
     */
    public void setSkew(double s) {
        skew = s;
    }
}
