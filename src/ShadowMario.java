import bagel.*;
import java.util.Properties;

/**
 * @author Taiyo Siew-Adachi
 */
public class ShadowMario extends AbstractGame {
    private final Image BACKGROUND_IMAGE;
    private final ScreenMessages screenMsg = new ScreenMessages();
    private World world;
    private enum GameState {
        START,
        IN_PLAY,
        NO_HEALTH,
        WON,
        LOST
    }
    private GameState gamestate = GameState.START;
    private enum Level {
        ONE,
        TWO,
        THREE
    }
    private Level level;
    private Player player;
    private final int WINDOW_HEIGHT;

    /**
     * The constructor
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
                Integer.parseInt(game_props.getProperty("windowHeight")),
                message_props.getProperty("title"));

        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        WINDOW_HEIGHT = Integer.parseInt(game_props.getProperty("windowHeight"));
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     * @param input, the key input from the user
     */
    @Override
    protected void update(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (input.wasPressed(Keys.ESCAPE)){ // close window
            Window.close();
        }
        /* Game is in play, display score/health, draw entities, read inputs and process */
        if (gamestate == GameState.IN_PLAY) {
            /* Update world for new frame, for things to happen regardless of key inputs */
            world.updateWorld();

            if (input.isDown(Keys.RIGHT) || input.wasPressed(Keys.RIGHT)) {
                world.moveRight(); // world moves against player perspective to the left
            }
            if (input.isDown(Keys.LEFT) || input.wasPressed(Keys.LEFT)) {
                world.moveLeft(); // world moves against player perspective to the right
            }
            if (player.getIsJumping()) { // jumping or fell off flying platform
                player.isMidAir();
            }
            else if (input.isDown(Keys.UP) || input.wasPressed(Keys.UP)) {
                if (!player.getIsJumping()) {
                    player.jump();
                }
            }
            /* Player shoots fireball if in range of shooting the enemy boss */
            if (input.wasPressed(Keys.S)) {
                if (level == Level.THREE){ //&& world.getEnemyBoss().inFireballRange(player)) {
                    world.newFireball(player.shootFireball());
                }
            }

            /* Lose condition */
            if (!player.isAlive()) {
                player.noHealth();
                gamestate = GameState.NO_HEALTH;
            }
            /* Win condition */
            if (world.getEndFlag().getCollided()) {
                gamestate = GameState.WON;
            }

            /* Done reading inputs and checking collisions. Now, draw the entities onto background image */
            world.drawWorld();
            screenMsg.inPlayScreen(player.getScore(), player.getHealth(), world.getEnemyBoss());
        }
        else { // not taking any player movement inputs
            if (gamestate == GameState.NO_HEALTH) {
                player.noHealth();
                if (player.getY() > WINDOW_HEIGHT) { // losing animation is finished
                    gamestate = GameState.LOST;
                }
                else { // play losing animation
                    world.drawWorld();
                    screenMsg.inPlayScreen(player.getScore(), player.getHealth(), world.getEnemyBoss());
                }
            }
            else if (gamestate == GameState.START) {
                screenMsg.startScreen();
                if (input.wasPressed(Keys.NUM_1) || input.wasPressed(Keys.NUM_2) || input.wasPressed(Keys.NUM_3)) {
                    if (input.wasPressed(Keys.NUM_1)) {
                        level = Level.ONE;
                    }
                    else if (input.wasPressed(Keys.NUM_2)) {
                        level = Level.TWO;
                    }
                    else if (input.wasPressed(Keys.NUM_3)) {
                        level = Level.THREE;
                    }
                    newGame();
                    gamestate = GameState.IN_PLAY;
                }
            }
            else if (gamestate == GameState.WON) {
                screenMsg.winScreen();
            }
            else if (gamestate == GameState.LOST) {
                screenMsg.loseScreen();
            }
            if (input.wasPressed(Keys.SPACE)) {
                if (gamestate == GameState.WON || gamestate == GameState.LOST) {
                    gamestate = GameState.START;
                }
            }
        }
    }

    /**
     * Resets the world for a new game according to the level chosen
     */
    private void newGame() {
        if (level == Level.ONE) {
            world = new World1(IOUtils.readPropertiesFile("res/app.properties").getProperty("level1File"));
        }
        if (level == Level.TWO) {
            world = new World2(IOUtils.readPropertiesFile("res/app.properties").getProperty("level2File"));
        }
        if (level == Level.THREE) {
            world = new World3(IOUtils.readPropertiesFile("res/app.properties").getProperty("level3File"));
        }
        player = world.getPlayer();
    }
}
