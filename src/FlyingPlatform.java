import bagel.Image;
import java.util.Properties;
import java.util.Random;
import static java.lang.Math.abs;

/**
 * Represents the flying platforms in the game, player can land on these
 * @author Taiyo Siew-Adachi
 */
public class FlyingPlatform extends Entity implements MovesIndependentFromPlayer, InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double halfLength;
    private final double halfHeight;
    private final double randSpeed;
    private double displacement = 0;
    private double direction;
    private final double maxDisplacement;

    /**
     * Constructor for enemy boss called from World classes, reads and assigns values from property file
     * @param x X co-ord of the flying platform read from the csv file
     * @param y Y co-ord of the flying platform read from the csv file
     */
    public FlyingPlatform(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.speed")),
                new Image(game_props.getProperty("gameObjects.flyingPlatform.image")));
        halfLength = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.halfLength"));
        halfHeight = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.halfHeight"));
        randSpeed = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        maxDisplacement = Double.parseDouble(game_props.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));
        direction = getRandDirection();
    }


    /**
     * Checks collision with the player using half-length and half-height
     * @param player The player object to check collision with
     * @return true if there is a collision, false if not
     */
    public boolean checkCollisionWithPlayer(Player player) {
        return (abs(player.getX() - getX()) < halfLength && (getY() - player.getY()) <= halfHeight &&
                (getY() - player.getY()) >= halfHeight - 1);
    }

    /**
     * Interacts with the player by landing the player on the flying platform if it is range
     * @param player The player object to potentially land on the platform
     */
    public void interactWithPlayer(Player player) {
        if (player.isFalling() && (getY() <= player.getLastGroundY())) {
            player.landOnFlyingPlatform(this);
        }
    }

    /**
     * Updates the position of the object based on a randomized speed and direction
     * If the displacement exceeds the maximum displacement, the direction is reversed
     */
    @Override
    public void updatePos() {
        if (abs(displacement) >= maxDisplacement) {
            direction *= -1; // switch direction
        }
        setX(getX() + direction * randSpeed);
        displacement += direction * randSpeed;
    }

    /**
     * Generates a random direction for a flying platform to move.
     * @return An integer representing the direction: 1 for positive direction, -1 for opposite direction
     */
    private int getRandDirection() {
        if ((new Random()).nextBoolean()) {
            return 1;
        }
        return -1;
    }

    /**
     * Retrieves the half-length of the flying platform
     * @return The half-length
     */
    public double getHalfLength() {
        return halfLength;
    }

    /**
     * Returns 0, as the collision for flying platform is more complex than just radius
     * @return Dummy int
     */
    public double getRadius() {
        return 0;
    }
}
