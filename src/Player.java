import bagel.Image;
import java.util.Properties;
import java.lang.Math;

/**
 * Represents the class for the player
 * @author Taiyo Siew-Adachi
 */
public class Player extends Entity implements ShootsFireballs, HasHealth{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final Image PLAYER_IMAGE_LEFT;
    private final double initialY;
    private final double radius;
    private int score = 0;
    private double ySpeed = 0;
    private boolean isJumping = false;
    private boolean isOnFlyingPlatform = false;
    private FlyingPlatform platformStandingOn;
    private double lastGroundY;
    private double health;
    private final Timer invincibleTimer;
    private final Timer doubleScoreTimer;
    private boolean facingRight = true;
    private final ScreenMessages powerUps;
    private static final double NO_LATERAL_MOVEMENT = 0;
    private static final double DECELERATION = 1.0;
    private static final int DOUBLE_SCORE = 2;

    /**
     * Constructor for player, reads and assigns values from property file
     * @param x X co-ord of the player read from the csv file
     * @param y Y co-ord of the player boss read from the csv file
     */
    public Player(double x, double y) {
        super(x, y,
                NO_LATERAL_MOVEMENT,
                new Image(game_props.getProperty("gameObjects.player.imageRight")));
        initialY = y;
        lastGroundY = y;
        health = Double.parseDouble(game_props.getProperty("gameObjects.player.health"));
        radius = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"));
        PLAYER_IMAGE_LEFT = new Image(game_props.getProperty("gameObjects.player.imageLeft"));
        powerUps = new ScreenMessages();
        invincibleTimer = InvinciblePower.getTimer();
        doubleScoreTimer = DoubleScore.getTimer();
    }

    /**
     * Sets the player vertical speed to a new speed
     */
    @Override
    public void draw() {
        if (facingRight) {
            super.draw();
        }
        else {
            PLAYER_IMAGE_LEFT.draw(getX(), getY());
        }
    }

    /**
     * Sets the player vertical speed to the jump speed
     */
    public void jump() {
        isJumping = true;
        ySpeed = -20.0;
        setY(getY() + ySpeed);
    }

    /**
     * Changes player vertical speed while player is not grounded to mimic gravity
     */
    public void isMidAir() {
        ySpeed += DECELERATION;
        setY(getY() + ySpeed);
        if (getY() >= initialY) {
            ySpeed = 0;
            setY(initialY);
            lastGroundY = initialY;
            isJumping = false;
        }
    }

    /**
     * Updates player vertical co-ords to fall at constant speed after losing all health
     */
    public void noHealth() {
        setY(getY() + FALL_SPEED);
    }

    /**
     * Updates player health according to damage taken from colliding with enemy
     * @param damageSource The source of the damage
     */
    public void takeDamage(InflictsDamage damageSource) {
        if (invincibleTimer.isActive()) {
            damageSource.inflictDamage(); // inflict damage to nothing, so fireball can disappear
            return;
        }
        if ((health -= damageSource.inflictDamage()) < 0) {
            health = 0;
        }
        health = Math.round(health*100)/100.0;
    }

    /**
     * Updates the state of the coin and score of the player following a collsion
     * @param coin the coin that is collected
     */
    public void addScore(Coin coin) {
        if (doubleScoreTimer.isActive()) {
            score += DOUBLE_SCORE * coin.getCoinValue();
        }
        else {
            score += coin.getCoinValue();
        }
    }

    /**
     * Lands the player on a platform, as it is in the range to do so
     * @param flyingPlatform the platform that the player lands on
     */
    public void landOnFlyingPlatform(FlyingPlatform flyingPlatform) {
        platformStandingOn = flyingPlatform;
        isOnFlyingPlatform = true;
        ySpeed = 0;
        isJumping = false;
        lastGroundY = flyingPlatform.getY();
    }

    /**
     * Creates and returns a new Fireball at the position of Player
     */
    public Fireball shootFireball() {
        return new Fireball(getX(), getY(), this, facingRight, initialY);
    }

    /**
     * Updates the states of the player after falling off the edge of a platform
     */
    public void getOffFlyingPlatform() {
        isOnFlyingPlatform = false;
        isJumping = true;
    }

    /**
     * Updates the counters for power ups collected by the player
     */
    public void decrementTimers() {
        if (invincibleTimer.isActive()) {
            powerUps.invincibility();
            invincibleTimer.countdown();
        }
        if (doubleScoreTimer.isActive()) {
            powerUps.doubleScore();
            doubleScoreTimer.countdown();
        }
    }

    /**
     * Checks if the entity is alive.
     * @return true if the entity's health is greater than 0, otherwise false
     */
    public boolean isAlive() {
        return (health > 0);
    }

    /**
     * Moves the entity to the right by setting the facing direction to right.
     */
    public void moveRight() {
        facingRight = true;
    }

    /**
     * Moves the entity to the left by setting the facing direction to left.
     */
    public void moveLeft() {
        facingRight = false;
    }

    /**
     * Gets the current health of the entity
     * @return the current health value
     */
    public double getHealth() {
        return health;
    }

    /**
     * Gets the invincibility timer of the entity
     * @return the Timer object representing the invincibility duration
     */
    public Timer getInvincibleTimer() {
        return invincibleTimer;
    }

    /**
     * Gets the radius of the entity.
     * @return the radius value.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Checks if the entity is currently jumping.
     * @return true if the entity is jumping, otherwise false
     */
    public boolean getIsJumping() {
        return isJumping;
    }

    /**
     * Checks if the entity is currently falling.
     * @return true if the entity's ySpeed is greater than 0, otherwise false
     */
    public boolean isFalling() {
        return (ySpeed > 0);
    }

    /**
     * Checks if the entity is on a flying platform.
     * @return true if the entity is on a flying platform, otherwise false
     */
    public boolean getIsOnFlyingPlatform() {
        return isOnFlyingPlatform;
    }

    /**
     * Gets the flying platform the entity is standing on.
     * @return the FlyingPlatform object the entity is standing on
     */
    public FlyingPlatform getPlatformStandingOn() {
        return platformStandingOn;
    }

    /**
     * Gets the Y-coordinate of the last ground the entity stood on.
     * @return the Y-coordinate of the last ground.
     */
    public double getLastGroundY() {
        return lastGroundY;
    }

    /**
     * Gets the current score of the entity.
     * @return the current score value.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the double score timer of the entity.
     * @return the Timer object representing the double score duration.
     */
    public Timer getDoubleScoreTimer() {
        return doubleScoreTimer;
    }
}