package ghost.wraith;

import ghost.wraith.manager.ColorManager;
import ghost.wraith.manager.CommandManager;
import ghost.wraith.manager.ConfigManager;
import ghost.wraith.manager.EventManager;
import ghost.wraith.manager.FriendManager;
import ghost.wraith.manager.HoleManager;
import ghost.wraith.manager.ModuleManager;
import ghost.wraith.manager.PositionManager;
import ghost.wraith.manager.RotationManager;
import ghost.wraith.manager.ServerManager;
import ghost.wraith.manager.SpeedManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ghost implements ModInitializer, ClientModInitializer {
    public static final String NAME = "Ghost";
    public static final String VERSION = "0.1 - 1.21";
    public static float TIMER = 1.0F;
    public static final Logger LOGGER = LogManager.getLogger("Ghost");
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    @Override
    public void onInitialize() {
        System.setProperty("java.awt.headless", "false");
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
    }

    @Override
    public void onInitializeClient() {
        eventManager.init();
        moduleManager.init();
        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }
}
