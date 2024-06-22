import bagel.Image;
import java.util.Properties;

/**
 * Represents the fireballs in the game shot by both player and enemy boss
 * @author Taiyo Siew-Adachi
 */
public class Fireball extends Entity implements InflictsDamage, MovesIndependentFromPlayer, InteractsWithPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double trajectorySpeed;
    private final double radius;
    private boolean inflicted = false;
    private final double damage;
    private final double WINDOW_BOUNDARY;
    private final ShootsFireballs shotBy;
    private int direction = 1;

    /**
     * Constructs a new Fireball object with the specified coordinates, shot by, and direction
     * @param x The initial x-coordinate of the Fireball
     * @param y The initial y-coordinate of the Fireball
     * @param shotBy The entity that shot the fireball
     * @param shotToTheRight Indicates whether the fireball is shot to the right
     */
    public Fireball(double x, double y, ShootsFireballs shotBy, boolean shotToTheRight) {
        super(x, y,
                /* Assuming the fireball moves according to player lateral movement at same speed as an enemy */
                Double.parseDouble(game_props.getProperty("gameObjects.enemy.speed")),
                new Image(game_props.getProperty("gameObjects.fireball.image")));
        this.shotBy = shotBy;
        trajectorySpeed = Double.parseDouble(game_props.getProperty("gameObjects.fireball.speed"));
        damage = Double.parseDouble(game_props.getProperty("gameObjects.fireball.damageSize"));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.fireball.radius"));
        WINDOW_BOUNDARY = Integer.parseInt(game_props.getProperty("windowWidth"));
        if (!shotToTheRight) {
            direction = -1;
        }
    }

    /**
     * Interacts with the player by inflicting damage if shot by the enemy boss and if not already inflicted
     * @param player The player to interact with
     */
    @Override
    public void interactWithPlayer(Player player) {
        if (!inflicted && shotBy != player) {
            player.takeDamage(this);
        }
    }

    /**
     * Inflicts damage and marks the fireball as inflicted
     * @return The amount of damage inflicted
     */
    @Override
    public double inflictDamage() {
        inflicted = true;
        return damage;
    }

    /**
     * Checks if the fireball can disappear from the game window
     * @return True if the fireball can disappear
     */
    public boolean canDisappear() {
        if (inflicted) {
            return true;
        }
        if (getX() < 0 || getX() > WINDOW_BOUNDARY) {
            return true;
        }
        return false;
    }

    /**
     * Updates the position of the fireball during the trajectory
     */
    @Override
    public void updatePos() {
        setX(getX() + direction * trajectorySpeed);
    }

    /**
     * Checks if damage has been inflicted by the fireball
     * @return true if damage has been inflicted false otherwise
     */
    public boolean damageInflicted() {
        return inflicted;
    }

    /**
     * Retrieves the entity that shot the fireball so that shooters do not get damaged by their own fireballs
     * @return entity that shot the fireball
     */
    public ShootsFireballs getShotBy() {
        return shotBy;
    }

    /**
     * Retrieves the radius of the fireball for collision with player or boss
     * @return The radius of the fireball
     */
    public double getRadius() {
        return radius;
    }

}

