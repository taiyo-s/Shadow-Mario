import bagel.Image;
import java.util.Properties;
import java.util.Random;
import static java.lang.Math.abs;

/**
 * Represents the enemies that roam around and inflicts damage to the player
 * @author Taiyo Siew-Adachi
 */
public class Enemy extends Entity implements InflictsDamage, MovesIndependentFromPlayer, InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double damage;
    private final double radius;
    private boolean inflicted = false;
    private final double randSpeed;
    private double displacement = 0;
    private int direction;
    private final double maxDisplacement;


    /**
     * Constructor for enemy called from World classes, reads and assigns values from property file
     * @param x X co-ord of the enemy read from the csv file
     * @param y Y co-ord of the enemy read from the csv file
     */
    public Enemy(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.coin.speed")),
                new Image(game_props.getProperty("gameObjects.enemy.image")));
        damage = Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.enemy.radius"));
        randSpeed = Double.parseDouble(game_props.getProperty("gameObjects.enemy.randomSpeed"));
        maxDisplacement = Double.parseDouble(game_props.getProperty("gameObjects.enemy.maxRandomDisplacementX"));
        direction = getRandDirection();
    }

    /**
     * Called by takeDamage method in Player class, returning the damage that the enemy inflicts onto player
     */
    public double inflictDamage() {
        inflicted = true;
        return damage;
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
     * Inflicts damage on the player if this object has not already inflicted damage
     * @param player The player to interact with
     */
    public void interactWithPlayer(Player player) {
        if (!inflicted) {
            player.takeDamage(this);
            inflicted = true;
        }
    }

    /**
     * Generates a random direction for an enemy to move.
     * @return An integer representing the direction: 1 for positive direction, -1 for opposite direction
     */
    private int getRandDirection() {
        if ((new Random()).nextBoolean()) {
            return 1;
        }
        return -1;
    }

    /**
     * Retrieves the radius of the object
     * @return The radius of the object
     */
    public double getRadius() {
        return radius;
    }

}