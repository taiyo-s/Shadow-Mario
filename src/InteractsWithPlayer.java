import bagel.util.Point;

/**
 * InteractsWithPlayer interface for all the entities that can collide with player with a simple radius check
 * @author Taiyo Siew-Adachi
 */
public interface InteractsWithPlayer {
    /**
     * Retrieves the radius of the entity
     * @return The radius of the entity
     */
    double getRadius();

    /**
     * Retrieves the position of the entity that interacts with player for collision detection
     * @return The position of the entity
     */
    Point getPosition();

    /**
     * Checks collision with the player using the radius of player and the other entity's radius
     * @param player The player to check collision with
     * @return true if there is a collision, false otherwise
     */
    default boolean checkCollisionWithPlayer(Player player) {
        double range = player.getRadius() + this.getRadius();
        return (this.getPosition().distanceTo(player.getPosition()) <= range);
    }

    /**
     * Interacts with the player and applies relevant changes to itself and the player
     * @param player The player to interact with
     */
    void interactWithPlayer(Player player);

}
