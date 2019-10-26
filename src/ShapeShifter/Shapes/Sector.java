package ShapeShifter.Shapes;

/**
 * Representation of a sector of a circle.
 *
 * @author usuario
 */
public class Sector extends Circle {

    /* Modulus and remainder are not the same! */
    private static double mod(double a, double b) {
        final double r = a % b;
        if (r < 0d) {
            return r + b;
        }
        return r;
    }

    /** The aperture angle of the sector. */
    protected double angle;

    /**
     * Construct a sector given its radius and aperture angle.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param r Radius.
     * @param a Aperture angle.
     */
    public Sector(int x, int y, double r, double a) {
        super(x, y, r);

        angle = mod(a, Math.PI * 2d);
    }

    /**
     * Construct a sector given a circle and an aperture angle.
     *
     * @param c Circle to use as base.
     * @param a Aperture angle.
     */
    public Sector(Circle c, double a) {
        this(c.getPosX(), c.getPosY(), c.getRadius(), a);
    }

    @Override
    public double area() {
        return super.area() * getAngle() / Math.PI * 2d;
    }

    @Override
    public double perimetre() {
        return super.perimetre() + 2d * radius;
    }

    @Override
    public String toString() {
        return String.format("SECT\t {radius: %4.4f, angle: %4.4f}", radius, angle);
    }

    /**
     * @Getter
     * @return angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @Setter
     * @param a angle
     */
    public void setAngle(double a) {
        angle = mod(a, Math.PI * 2d);
    }
}
