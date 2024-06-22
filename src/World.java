import java.util.*;


/**
 * Represents the game world for all levels, containing general collections of entities.
 * @author Taiyo Siew-Adachi
 */
public abstract class World {
    private final LinkedHashSet<Entity> allEntities = new LinkedHashSet<>();
    private final HashSet<MovesIndependentFromPlayer> movesIndependentFromPlayers = new HashSet<>();
    private final HashSet<InteractsWithPlayer> interactsWithPlayers = new HashSet<>();

    /**
     * Default constructor for the World class
     */
    public World(){
    }

    /**
     * Uniformly move the entities to the right, against the player's movement
     */
    public void moveRight() {
        for (Entity entity: allEntities) {
            entity.moveRight();
        }
    }

    /**
     * Uniformly move the entities to the left, against the player's movement
     */
    public void moveLeft() {
        for (Entity entity: allEntities) {
            entity.moveLeft();
        }
    }

    /**
     * Render images of all world entities onto game window
     */
    public void drawWorld() {
        for (Entity entity: allEntities) {
            entity.draw();
        }
    }

    /**
     * Iteratively checks each entity that interacts with the player if they are in range and collides them if so
     */
    public void updateWorld() {
        updatePositions();
        for (InteractsWithPlayer interactsWithPlayer: interactsWithPlayers) {
            if (interactsWithPlayer.checkCollisionWithPlayer(getPlayer())) {
                interactsWithPlayer.interactWithPlayer(getPlayer());
            }
        }
    }

    /**
     * Moves all entities that move independent of the player's movement such as random movements
     */
    private void updatePositions() {
        for (MovesIndependentFromPlayer mover: movesIndependentFromPlayers) {
            mover.updatePos();
        }
    }

    /**
     * Introduces new fireball into the world, Can be extended in future to introduce new entities after game commence
     * by changing parameter to an interface of entities that are added during game is in play
     * @param fireball The new fireball shot by enemy boss or player
     */
    public void newFireball(Fireball fireball) {
    }

    /**
     * Retrieves the Player object in the current game
     * @return The Player object
     */
    public abstract Player getPlayer();

    /**
     * Retrieves the EnemyBoss object in the current game
     * @return The EnemyBoss object
     */
    public EnemyBoss getEnemyBoss() {
        return null;
    }

    /**
     * Retrieves the EndFlag object in the current game
     * @return The EndFlag object
     */
    public abstract EndFlag getEndFlag();

    /**
     * Adds an entity to the LinkedHashSet of all entities
     * @param entity The entity to be added
     */
    protected void addToAllEntities(Entity entity) {
        allEntities.add(entity);
    }

    /**
     * Adds a MovesIndependentFromPlayer object to the list of moves independent of the player.
     * @param movesIndependentFromPlayer The MovesIndependentFromPlayer object to be added to the set
     */
    protected void addToMovesIndependentFromPlayer(MovesIndependentFromPlayer movesIndependentFromPlayer) {
        movesIndependentFromPlayers.add(movesIndependentFromPlayer);
    }

    /**
     * Adds an InteractsWithPlayer object to the list of objects that interact with the player.
     * @param interactsWithPlayer The InteractsWithPlayer object to be added to the HashSet
     */
    protected void addToInteractsWithPlayer(InteractsWithPlayer interactsWithPlayer) {
        interactsWithPlayers.add(interactsWithPlayer);
    }

    /**
     * Removes an entity from the list of all entities, only used for fireball in this particular implementation
     * @param entity The entity to be removed from the LinkedHashSet,
     */
    protected void removeEntity(Entity entity) {
        allEntities.remove(entity);
    }

    /**
     * Removes a MovesIndependentFromPlayer object from the list of moves independent of the player, only fireball
     * for this implementation
     * @param movesIndependentFromPlayer The MovesIndependentFromPlayer object to be removed.
     */
    protected void removeMovesIndependentFromPlayer(MovesIndependentFromPlayer movesIndependentFromPlayer) {
        movesIndependentFromPlayers.remove(movesIndependentFromPlayer);
    }

    /**
     * Removes an InteractsWithPlayer object from the list of objects that interact with the player, only fireball
     * for this implementation
     * @param interactsWithPlayer The InteractsWithPlayer object to be removed.
     */
    protected void removeInteractsWithPlayer(InteractsWithPlayer interactsWithPlayer) {
        interactsWithPlayers.remove(interactsWithPlayer);
    }
}

