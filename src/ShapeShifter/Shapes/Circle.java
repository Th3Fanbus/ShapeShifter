package ShapeShifter.Shapes;

import ShapeShifter.Figura;

/**
 * Represents a circle on a 2D environment.
 *
 * @author usuario
 */
public class Circle extends Figura {

    /** The circle's radius. */
    protected double radius;
    
    /**
     * Construct a circle given its radius.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param r Radius.
     */
    public Circle(int x, int y, double r) {
        super(x, y);
        
        radius = r;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimetre() {
        return 2d * Math.PI * radius;
    }

    @Override
    public String toString() {
        return String.format("CIRC\t {radius: %4.4f}", radius);
    }

    /**
     * @Getter
     * @return radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @Setter
     * @param r radius
     */
    public void setRadius(double r) {
        radius = r;
    }
}
