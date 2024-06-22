import bagel.Image;
import java.util.Properties;

/**
 * Represents the coins that the player collects to increase the score
 * @author Taiyo Siew-Adachi
 */
public class Coin extends Entity implements MovesIndependentFromPlayer, InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final int coinValue;
    private final double radius;
    private boolean collected = false;
    private static final double FLOAT_SPEED = -10.0;

    /**
     * Constructor for coins called from World classes, reads and assigns values from property file
     * @param x X co-ord of the coin read from the csv file
     * @param y Y co-ord of the coin boss read from the csv file
     */
    public Coin(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.coin.speed")),
                new Image(game_props.getProperty("gameObjects.coin.image")));
        coinValue = Integer.parseInt(game_props.getProperty("gameObjects.coin.value"));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.coin.radius"));
    }

    /**
     * Specifies the interaction between a coin and a player when they collide
     * @param player Player that collided with the coin
     */
    @Override
    public void interactWithPlayer(Player player) {
        if (!collected) {
            collected = true;
            player.addScore(this);
        }
    }

    /**
     * Float the coin up each frame, after being collected until it is off the screen
     */
    @Override
    public void updatePos() {
        if (collected && getY() > -50) {
            setY(getY() + FLOAT_SPEED);
        }
    }

    /**
     * Retrieves the value associated with this coin
     * @return The integer value of the coin
     */
    public int getCoinValue() {
        return coinValue;
    }

    /**
     * Retrieves the radius of this coin
     * @return The radius of collision of the coin
     */
    public double getRadius() {
        return radius;
    }
}