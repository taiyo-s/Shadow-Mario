import bagel.Image;
import java.util.Properties;

/**
 * Represents the end flag that the player moves towards
 * @author Taiyo Siew-Adachi
 */
public class EndFlag extends Entity implements InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double radius;
    private boolean collided = false;

    /**
     * Constructor for end flag called from World classes, reads and assigns values from property file
     * @param x X co-ord of the enemy boss read from the csv file
     * @param y Y co-ord of the enemy boss read from the csv file
     */
    public EndFlag(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.endFlag.speed")),
                new Image(game_props.getProperty("gameObjects.endFlag.image")));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius"));
    }
    /**
     * Changes the collided state to true, when the end flag and a player collide, meaning the game is won
     * @param player Player that collided with the end flag
     */
    @Override
    public void interactWithPlayer(Player player) {
        collided = true;
    }

    /**
     * Retrieves the collision status of the object.
     * @return true if the object has collided, false otherwise
     */
    public boolean getCollided() {
        return collided;
    }

    /**
     * Retrieves the radius of this end flag
     * @return The radius of the end flag
     */
    public double getRadius() {
        return radius;
    }
}