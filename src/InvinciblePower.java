import bagel.Image;
import java.util.Properties;

/**
 * Represents the collectable invincible power up items in the game
 * @author Taiyo Siew-Adachi
 */
public class InvinciblePower extends Entity implements MovesIndependentFromPlayer, InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double radius;
    private boolean collected = false;
    private static final double FLOAT_SPEED = -10.0;
    private static final Timer timer = new Timer(Integer.parseInt(game_props.getProperty("gameObjects.invinciblePower.maxFrames")));

    /**
     * Constructor for invincible power, reads and assigns values from property file
     * @param x X co-ord of the power up, as read from the csv file
     * @param y Y co-ord of the power up, as read from the csv file
     */
    public InvinciblePower(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.invinciblePower.speed")),
                new Image(game_props.getProperty("gameObjects.invinciblePower.image")));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.invinciblePower.radius"));
    }

    /**
     * Float the invincible power item up each frame, after being collected
     */
    @Override
    public void updatePos() {
        if (collected && getY() > -50) {
            setY(getY() + FLOAT_SPEED);
        }
    }

    /**
     * The interaction between an invincible power item and a player when they collide
     * @param player Player that collided with the invincible power item
     */
    public void interactWithPlayer(Player player) {
        if (!collected) {
            collected = true;
            player.getInvincibleTimer().restart();
        }
    }

    /**
     * Retrieves the value associated with this coin
     * @return The integer value of the coin
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Retrieves the timer associated with the double score power
     * @return The timer for double scores
     */
    public static Timer getTimer() {
        return timer;
    }
}