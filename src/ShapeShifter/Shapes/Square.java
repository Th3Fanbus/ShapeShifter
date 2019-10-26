package ShapeShifter.Shapes;

/**
 * Representation of a square.
 *
 * @author usuario
 */
public class Square extends Rectangle {
    
    /**
     * Construct a rectangle given its base and height.
     *
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param s Side length.
     */
    public Square(int x, int y, double s) {
        super(x, y, s, s);
    }
    
    @Override
    public String toString() {
        return String.format("SQR\t {side: %4.4f}", base);
    }

    /**
     * @Setter
     * @param b base
     */
    @Override
    public void setBase(double b) {
        height = b;
        base = b;
    }

    /**
     * @Setter
     * @param h height
     */
    @Override
    public void setHeight(double h) {
        height = h;
        base = h;
    }
}
