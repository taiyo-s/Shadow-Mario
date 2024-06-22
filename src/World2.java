import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.Math.abs;


/**
 * Represents the game world for level 2, containing entities such as those in level 1, flying platforms and power ups.
 * @author Taiyo Siew-Adachi
 */
public class World2 extends World{
    private Player player;
    private Platform platform;
    private EndFlag endFlag;
    private final ArrayList<FlyingPlatform> flyingPlatforms = new ArrayList<>();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final HashSet<Coin> coins = new HashSet<>();
    private final HashSet<DoubleScore> doubleScores = new HashSet<>();;
    private final HashSet<InvinciblePower> invinciblePowers = new HashSet<>();;

    /**
     * Constructs a new World object for level 2 from CSV file containing the world layout.
     * @param filename name of csv file to be read in
     */
    public World2(String filename) {
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
     * Updates the world by initially updating positions and checking collisions from superclass. Also checks whether
     * player falls off the flying platform it is standing on and finally ticks down the player's power ups
     */
    public void updateWorld() {
        super.updateWorld();
        if (player.getIsOnFlyingPlatform()) {
            FlyingPlatform currentPlatform = player.getPlatformStandingOn();
            if (abs(player.getX() - currentPlatform.getX()) >= currentPlatform.getHalfLength()) { // fall off platform
                player.getOffFlyingPlatform();
            }
        }
        player.decrementTimers();
    }

    /**
     * Builds collections, HashSet and LinkedHashSet. LinkedHashSet constructed after finish reading csv in order to
     * control order of LinkedHashSet to allow player and other important entities to be brought to the front in the
     * game window
     */
    private void make_collections() {
        addToAllEntities(platform);
        addToAllEntities(endFlag);
        addToInteractsWithPlayer(endFlag);
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
        addToAllEntities(player);
    }

    /**
     * Retrieves the player in this level 2 world
     * @return player of Player class representing the player in the current game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the end flag in this level 2 world
     * @return instance of EndFlag class representing the end flag in the current game
     */
    public EndFlag getEndFlag() {
        return endFlag;
    }

}

