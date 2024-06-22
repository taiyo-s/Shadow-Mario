import bagel.Image;
import java.util.Properties;
import java.util.Random;
import static java.lang.Math.abs;

/**
 * Represents the class for the enemy boss that is in level 3
 * @author Taiyo Siew-Adachi
 */
public class EnemyBoss extends Entity implements ShootsFireballs, HasHealth, MovesIndependentFromPlayer{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final double radius;
    private final double activationRadius;
    private double health;
    private final boolean facingRight = false;
    private final Random random = new Random();
    private final Timer fireballCharge;
    private final ScreenMessages fightMsg; // customisation

    /**
     * Constructor for enemy boss, reads and assigns values from property file
     * @param x X co-ord of the enemy boss read from the csv file
     * @param y Y co-ord of the enemy boss read from the csv file
     */
    public EnemyBoss(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.speed")),
                new Image(game_props.getProperty("gameObjects.enemyBoss.image")));
        health = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.health"));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.radius"));
        activationRadius = Double.parseDouble(game_props.getProperty("gameObjects.enemyBoss.activationRadius"));
        fireballCharge = new Timer(100);
        fightMsg = new ScreenMessages(); // customisation
    }

    /**
     * Returns boolean value depending on if the player and enemy boss is in range of each-other to fight
     * @param player Player to measure the distance from to determine if it is in range to shoot fireballs
     * @return boolean value: True if in range, False if out of range
     */
    public boolean inFireballRange(Player player) {
        if (abs(player.getX() - getX()) <= activationRadius && this.isAlive()) {
            fightMsg.fight(); // customisation
            return true;
        }
        return false;
    }

    /**
     * Charges up the fireball to potentially be shot by enemy boss
     */
    public void fireballChargeUp() {
        fireballCharge.countdown();
    }

    /**
     * Attempts to shoot a fireball when the charge is complete
     * @return true if the fireball can be shot; false if it cannot
     */
    public boolean tryShootFireball() {
        if (!fireballCharge.isActive() && random.nextBoolean()) { // 100th frame, fireball shot is fully charged up
            fireballCharge.restart();
            return true;
        }
        return false;
    }

    /**
     * Creates a new fireball object to be shot by the enemy boss
     * @return The newly created Fireball object
     */
    public Fireball shootFireball() {
        return new Fireball(getX(), getY(), this, facingRight);
    }

    /**
     * Checks if the enemy boss has collided with a fireball
     * @param fireball The fireball object to check collision with
     * @return true if a collision has occurred, false otherwise
     */
    public boolean collidedWithFireball(Fireball fireball) {
        double range = getRadius() + fireball.getRadius();
        if (this.getPosition().distanceTo(fireball.getPosition()) <= range) {
            if (!(fireball.damageInflicted()) && fireball.getShotBy() != this) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reduces the enemy boss's health based on the damage inflicted by a source, in this game, only a fireball
     * @param damageSource The source inflicting the damage
     */
    public void takeDamage(InflictsDamage damageSource) {
        if (health <= 0) {
            return;
        }
        if ((health -= damageSource.inflictDamage()) < 0) {
            health = 0;
        }
        health = Math.round(health*100)/100.0;
    }

    /**
     * Move the boss vertically, as it falls to its death until it is well off the screen
     */
    public void updatePos() {
        if (getY() < Integer.parseInt(game_props.getProperty("windowHeight")) + 120) {
            setY(getY() + FALL_SPEED);
        }
    }

    /**
     * Retrieves the current health of the enemy boss
     * @return The health value of the enemy boss
     */
    public double getHealth() {
        return health;
    }

    /**
     * Checks if the enemy boss is alive
     * @return true if the enemy boss is alive, false otherwise
     */
    public boolean isAlive() {
        return (health > 0);
    }

    /**
     * Retrieves the radius of the enemy boss for colliding with fireballs
     * @return The radius
     */
    public double getRadius() {
        return radius;
    }

}
