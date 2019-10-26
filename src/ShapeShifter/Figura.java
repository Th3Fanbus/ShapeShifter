package ShapeShifter;

/**
 * Represents the idea of a generic shape.
 *
 * @author usuario
 */
public abstract class Figura implements Comparable<Figura> {
    private int posX;
    private int posY;
    
    /**
     * Construct a new shape.
     *
     * @param posX
     * @param posY
     */
    protected Figura(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * @Getter
     * @return x
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @Setter
     * @param posX x
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @Getter
     * @return y
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @Setter
     * @param posY y
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Compare with another instance according to the area.
     *
     * @param other The other instance.
     * @return The usual output of this method.
     */
    @Override
    public int compareTo(Figura other) {
        return Double.compare(this.area(), other.area());
    }
    
    /**
     * Obtain the area of this shape.
     * @return the area in square units.
     */
    public abstract double area();

    /**
     * Obtain the perimeter of this shape.
     * @return the perimeter in units.
     */
    public abstract double perimetre();
}
