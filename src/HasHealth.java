/**
 * HasHealth interface representing entities that have health and can lose it to die
 * @author Taiyo Siew-Adachi
 */

public interface HasHealth {
    /** The fall speed constant. */
    double FALL_SPEED = 2.0;

    /**
     * Takes damage from a damage source
     * @param damageSource The source of the damage
     */
    void takeDamage(InflictsDamage damageSource);

    /**
     * Checks if the entity is alive (health > 0)
     * @return true if the entity is alive
     */
    boolean isAlive();
}
