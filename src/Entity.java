import bagel.Image;
import bagel.util.Point;

/**
 * Represents all entities in the game
 * @author Taiyo Siew-Adachi
 */
public abstract class Entity {
    private double x;
    private double y;
    private double xSpeed;
    private Image IMAGE;

    /**
     * Constructs a new Entity object with the specified coordinates, speed, and image.
     * @param x The initial x-coordinate of the Entity from the csv file
     * @param y The initial y-coordinate of the Entity from the csv file
     * @param xSpeed The horizontal speed of the Entity
     * @param image The image associated with the Entity
     */
    public Entity(double x, double y, double xSpeed, Image image) {
        this.x = x;
        this.y = y;
        this.IMAGE = image;
        this.xSpeed = xSpeed;
    }

    /**
     * Render image of the entity to the game window at co-ordinates (x,y)
     */
    public void draw() {
        IMAGE.draw(x, y);
    }

    /**
     * Moves the entity against the player to the left, while player "moves" to the right
     */
    public void moveRight() {
        x -= xSpeed;
    }

    /**
     * Moves the entity against the player to the right, while player "moves" to the left
     */
    public void moveLeft() {
        x += xSpeed;
    }

    /**
     * Converts a point with an x and y value to a Point object
     * @return Point The position of the entity in the form of an instance of Bagel Class, Point
     */
    public Point getPosition() {
        return new Point(x, y);
    }

    /**
     * Returns the x value of the Entity
     * @return x The x co-ord of the Entity
     */
    protected double getX() {
        return x;
    }

    /**
     * Returns the y value of the Entity
     * @return y The y co-ord of the Entity
     */
    protected double getY() {
        return y;
    }

    /**
     * Assigns a new value to x of Entity
     * @param x The x co-ord of the Entity
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Assigns a new value to y of Entity
     * @param y The y co-ord of the Entity
     */
    protected void setY(double y) {
        this.y = y;
    }
}
