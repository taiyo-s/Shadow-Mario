import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.Math.abs;


/**
 * Represents the game world in level 3, containing entities such as those in level 2 and the enemy boss.
 * @author Taiyo Siew-Adachi
 */
public class World3 extends World {
    private Player player;
    private EnemyBoss enemyBoss = null;
    private Platform platform;
    private EndFlag endFlag;
    private final ArrayList<FlyingPlatform> flyingPlatforms = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final HashSet<Coin> coins = new HashSet<>();
    private final HashSet<DoubleScore> doubleScores = new HashSet<>();;
    private final HashSet<InvinciblePower> invinciblePowers = new HashSet<>();;
    private final HashSet<Fireball> fireballs = new HashSet<>();

    /**
     * Constructs a new World object for level 3 from CSV file containing the world layout.
     * @param filename name of csv file to be read in
     */
    public World3(String filename) {
        /* Read csv file */
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){

            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                String[] elements = nextLine.split(",");
                if (elements[0].equals("PLATFORM")) {
                    platform = new Platform(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                }
                else if (elements[0].equals("PLAYER")) {
                    player = new Player(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                }
                else if (elements[0].equals("ENEMY_BOSS")) {
                    enemyBoss = new EnemyBoss(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                }
                else if (elements[0].equals("END_FLAG")) {
                    endFlag = new EndFlag(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                }
                else if (elements[0].equals("FLYING_PLATFORM")) {
                    FlyingPlatform flyingPlatform = new FlyingPlatform(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                    flyingPlatforms.add(flyingPlatform);
                }
                else if (elements[0].equals("COIN")) {
                    Coin coin = new Coin(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                    coins.add(coin);
                }
                else if (elements[0].equals("DOUBLE_SCORE")) {
                    DoubleScore doubleScore = new DoubleScore(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                    doubleScores.add(doubleScore);
                }
                else if (elements[0].equals("INVINCIBLE_POWER")) {
                    InvinciblePower invinciblePower = new InvinciblePower(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                    invinciblePowers.add(invinciblePower);
                }
                else if (elements[0].equals("ENEMY")) {
                    Enemy enemy = new Enemy(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                    enemies.add(enemy);
                }
            }
            make_collections(); // Order in desired order instead of order of reading from csv
        }
        catch (IOException ignored) {
        }
    }


    /**
     * Updates the world by updating positions and checking collisions from superclass, excluding end flag as
     * in level 3, enemy boss should be defeated before winning. Also, responsible for checking fireball enemy boss
     * collision, fireball disappear condition, player falling off flying platform, ticking down the player's power ups,
     * and enemy boss random fireball shots.
     */
    public void updateWorld() {
        super.updateWorld();
        /* Win only if the boss is defeated */
        if (endFlag.checkCollisionWithPlayer(player)) {
            if (!enemyBoss.isAlive()) {
                endFlag.interactWithPlayer(player);
            }
        }
        /* Boss and fireball collisions and interactions */
        for (Fireball fireball: fireballs) {
            if (enemyBoss.collidedWithFireball(fireball)) {
                enemyBoss.takeDamage(fireball);
                if (!enemyBoss.isAlive()) {
                    addToMovesIndependentFromPlayer(enemyBoss);
                }
            }
        }
        /* Remove the fireballs that are no longer needed */
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            if (fireball.canDisappear()) {
                iterator.remove(); // Remove the current fireball using iterator
                removeEntity(fireball);
                removeMovesIndependentFromPlayer(fireball);
                removeInteractsWithPlayer(fireball);
            }
        }
        /* Check if player falls off the current platform */
        if (player.getIsOnFlyingPlatform()) {
            FlyingPlatform currentPlatform = player.getPlatformStandingOn();
            if (abs(player.getX() - currentPlatform.getX()) >= currentPlatform.getHalfLength()) { // fall off platform
                player.getOffFlyingPlatform();
            }
        }
        player.decrementTimers();
        enemyBoss.fireballChargeUp();
        /* Boss random fireball shooting */
        if (enemyBoss.inFireballRange(player)) {
            if (enemyBoss.tryShootFireball()) {
                newFireball(enemyBoss.shootFireball());
            }
        }
    }

    /**
     * Introduces new fireball into the world by adding it to collections in this class and the superclass
     * @param fireball The new fireball shot by enemy boss or player
     */
    public void newFireball(Fireball fireball) {
        fireballs.add(fireball);
        addToAllEntities(fireball);
        addToMovesIndependentFromPlayer(fireball);
        addToInteractsWithPlayer(fireball);
    }

    /**
     * Builds collections, HashSet and LinkedHashSet. LinkedHashSet constructed after finish reading csv to
     * control order of LinkedHashSet to allow player and other important entities to be brought to the front of the
     * game window when drawing. End flag not added to InteractsWithPlayer, as collisions dealt with in this class
     */
    private void make_collections() {
        addToAllEntities(platform);
        addToAllEntities(endFlag);
        for (FlyingPlatform flyingPlatform: flyingPlatforms) {
            addToAllEntities(flyingPlatform);
            addToMovesIndependentFromPlayer(flyingPlatform);
            addToInteractsWithPlayer(flyingPlatform);
        }
        for (Enemy enemy: enemies) {
            addToAllEntities(enemy);
            addToMovesIndependentFromPlayer(enemy);
            addToInteractsWithPlayer(enemy);
        }
        for (Coin coin: coins) {
            addToAllEntities(coin);
            addToMovesIndependentFromPlayer(coin);
            addToInteractsWithPlayer(coin);
        }
        for (DoubleScore doubleScore: doubleScores) {
            addToAllEntities(doubleScore);
            addToMovesIndependentFromPlayer(doubleScore);
            addToInteractsWithPlayer(doubleScore);
        }
        for (InvinciblePower invinciblePower: invinciblePowers) {
            addToAllEntities(invinciblePower);
            addToMovesIndependentFromPlayer(invinciblePower);
            addToInteractsWithPlayer(invinciblePower);
        }
        addToAllEntities(enemyBoss);
        addToAllEntities(player);
    }

    /**
     * Retrieves the player in this level 3 world
     * @return player of Player class representing the player in the current game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the end flag in this level 3 world
     * @return instance of EndFlag class representing the end flag in the current game
     */
    public EndFlag getEndFlag() {
        return endFlag;
    }

    /**
     * Retrieves the enemy boss for level 3
     * @return instance of enemy boss class representing the boss in level 3
     */
    public EnemyBoss getEnemyBoss() {
        return enemyBoss;
    }

}

