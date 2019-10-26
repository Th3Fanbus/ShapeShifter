package ShapeShifter.Shapes;

import ShapeShifter.Figura;

/**
 * Representation of a rectangle.
 *
 * @author usuario
 */
public class Rectangle extends Figura {

    /** The rectangle's base. */
    protected double base;

    /** The rectangle's height. */
    protected double height;
    
    /**
     * Construct a rectangle given its base and height.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param b Base side length.
     * @param h Height side length.
     */
    public Rectangle(int x, int y, double b, double h) {
        super(x, y);
        
        base = b;
        height = h;
    }

    @Override
    public double area() {
        return base * height;
    }
    
    @Override
    public double perimetre() {
        return 2d * (base + height);
    }

    @Override
    public String toString() {
        return String.format("RECT\t {base: %4.4f, height: %4.4f}", base, height);
    }

    /**
     * @Getter
     * @return base
     */
    public double getBase() {
        return base;
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
     * @param b base
     */
    public void setBase(double b) {
        base = b;
    }

    /**
     * @Setter
     * @param h height
     */
    public void setHeight(double h) {
        height = h;
    }
}
