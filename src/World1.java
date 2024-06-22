import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Represents the game world for level 1, containing entities such as player, platforms, coins, enemies, and end flag.
 * @author Taiyo Siew-Adachi
 */
public class World1 extends World {
    private Player player;
    private Platform platform;
    private EndFlag endFlag;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final HashSet<Coin> coins = new HashSet<>();

    /**
     * Constructs a new World object for level 1 from CSV file containing the world layout.
     * @param filename name of csv file to be read in
     */
    public World1(String filename) {
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
                else if (elements[0].equals("COIN")) {
                    coins.add(new Coin(Double.parseDouble(elements[1]), Double.parseDouble(elements[2])));
                }
                else if (elements[0].equals("ENEMY")) {
                    enemies.add(new Enemy(Double.parseDouble(elements[1]), Double.parseDouble(elements[2])));
                }
            }
            make_collections(); // Order in desired order instead of order of reading from csv
        }
        catch (IOException ignored) {
        }
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
        addToAllEntities(player);
    }

    /**
     * Retrieves the player in this level 1 world
     * @return player of Player class representing the player in the current game
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the end flag in this level 1 world
     * @return instance of EndFlag class representing the end flag in the current game
     */
    public EndFlag getEndFlag() {
        return endFlag;
    }

}

