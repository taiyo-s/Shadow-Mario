import bagel.Image;
import java.util.Properties;

/**
 * Represents the platform the player runs on and enemy boss stands on. Player cannot go below the platform while alive
 * @author Taiyo Siew-Adachi
 */
public class Platform extends Entity{
    private static final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final Image PLATFORM;

    /**
     * Constructor for platform, reads from property file and assigns values
     * @param x X co-ord of the platform read from the csv file
     * @param y Y co-ord of the platform read from the csv file
     */
    public Platform(double x, double y) {
        super(x, y,
                Double.parseDouble(game_props.getProperty("gameObjects.platform.speed")),
                new Image(game_props.getProperty("gameObjects.platform.image")));
        PLATFORM = new Image(game_props.getProperty("gameObjects.platform.image"));
    }

    /**
     * Render platform image to game window
     */
    @Override
    public void draw() {
        if (getX() < 3000) {
            super.draw();
        }
        else {
            PLATFORM.draw(3000, getY());
        }
    }

}