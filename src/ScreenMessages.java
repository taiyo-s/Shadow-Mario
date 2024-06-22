import bagel.*;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Represents the screens that are rendered to the screen at different stages of the game
 * @author Taiyo Siew-Adachi
 */
public class ScreenMessages {
    private final Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
    private final String font = game_props.getProperty("font");
    private final Font titleFont = new Font(font, Integer.parseInt(game_props.getProperty("title.fontSize")));
    private final Font scorefont = new Font(font, Integer.parseInt(game_props.getProperty("score.fontSize")));
    private final Font msgFont = new Font(font, Integer.parseInt(game_props.getProperty("message.fontSize")));
    private final Font instructFont = new Font(font, Integer.parseInt(game_props.getProperty("instruction.fontSize")));
    private final Font playerHealthFont = new Font(font, Integer.parseInt(game_props.getProperty("playerHealth.fontSize")));
    private final Font bossHealthFont = new Font(font, Integer.parseInt(game_props.getProperty("enemyBossHealth.fontSize")));
    private final DrawOptions red = new DrawOptions().setBlendColour(Colour.RED);
    private double xCord;
    private double yCord;

    /**
     * Renders text to be drawn to the screen before the game starts
     */
    public void startScreen() {
        xCord = Double.parseDouble(game_props.getProperty("title.x"));
        yCord = Double.parseDouble(game_props.getProperty("title.y"));
        titleFont.drawString(message_props.getProperty("title"), xCord, yCord);
        xCord = setTextPos(instructFont, "USE ARROW KEYS TO MOVE");
        yCord = Double.parseDouble(game_props.getProperty("instruction.y"));
        instructFont.drawString("USE ARROW KEYS TO MOVE", xCord, yCord);
        xCord = setTextPos(msgFont, "ENTER LEVEL TO START - 1, 2, 3");
        yCord += 30;
        msgFont.drawString("ENTER LEVEL TO START - 1, 2, 3", xCord, yCord);
    }

    /**
     * Renders score and health(s) to be drawn to the screen while game is in play
     * @param score Score of the player to be drawn to game window
     * @param playerHealth Current health of the player
     * @param enemyBoss The boss in the game (null if it does not exist)
     */
    public void inPlayScreen(int score, double playerHealth, EnemyBoss enemyBoss) {
        xCord = Double.parseDouble(game_props.getProperty("score.x"));
        yCord = Double.parseDouble(game_props.getProperty("score.y"));
        scorefont.drawString(message_props.getProperty("score") + score, xCord, yCord);
        xCord = Double.parseDouble(game_props.getProperty("playerHealth.x"));
        yCord = Double.parseDouble(game_props.getProperty("playerHealth.y"));
        playerHealthFont.drawString(message_props.getProperty("health") + (int)(100*playerHealth), xCord, yCord);
        if (enemyBoss != null) {
            xCord = Double.parseDouble(game_props.getProperty("enemyBossHealth.x"));
            yCord = Double.parseDouble(game_props.getProperty("enemyBossHealth.y"));
            bossHealthFont.drawString(message_props.getProperty("health") + (int)(100*enemyBoss.getHealth()), xCord, yCord, red);
        }

    }

    /**
     * Renders message and instructions to be drawn to the screen when game is won
     */
    public void winScreen() { // Did not use game props for text, due to centre alignment
        xCord = setTextPos(msgFont, "CONGRATULATIONS, YOU WON!");
        yCord = Double.parseDouble(game_props.getProperty("message.y"));
        msgFont.drawString("CONGRATULATIONS, YOU WON!", xCord, yCord);
        xCord = setTextPos(instructFont, "PRESS SPACE TO CONTINUE");
        yCord += 30;
        instructFont.drawString("PRESS SPACE TO CONTINUE", xCord, yCord);
    }

    /**
     * Renders message and instructions to be drawn to the screen when the game is lost
     */
    public void loseScreen() { // Did not use game props for text, due to centre alignment
        xCord = setTextPos(msgFont, "GAME OVER, YOU LOST!");
        yCord = Double.parseDouble(game_props.getProperty("message.y"));
        msgFont.drawString("GAME OVER, YOU LOST!", xCord, yCord);
        xCord = setTextPos(instructFont, "PRESS SPACE TO CONTINUE");
        yCord += 30;
        instructFont.drawString("PRESS SPACE TO CONTINUE", xCord, yCord);
    }


    /**
     * Tells user that double score is active
     */
    public void doubleScore() {
        xCord = Double.parseDouble(game_props.getProperty("playerHealth.x")) - 120;
        yCord = Double.parseDouble(game_props.getProperty("playerHealth.y"));
        scorefont.drawString("2X", xCord, yCord, new DrawOptions().setBlendColour(new Colour(255, 215, 0)));
    }

    /**
     * Tells user that invincibility is active
     */
    public void invincibility() {
        xCord = Double.parseDouble(game_props.getProperty("playerHealth.x")) - 220;
        yCord = Double.parseDouble(game_props.getProperty("playerHealth.y"));
        scorefont.drawString("INV", xCord, yCord, new DrawOptions().setBlendColour(Colour.BLUE));
    }

    /**
     * Tells user that the player and enemy boss are in range to shoot fireballs at each other
     */
    public void fight() {
        xCord = setTextPos(msgFont, "FIGHT!");
        yCord = Window.getHeight() * (1/3.0);
        instructFont.drawString("FIGHT!", xCord, yCord, red);
    }

    /**
     * Helper function for rendering instructions
     */
    private double setTextPos(Font font, String msg) {
        return ((Window.getWidth() - font.getWidth(msg))/ 2.0);
    }

}