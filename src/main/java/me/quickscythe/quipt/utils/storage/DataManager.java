package me.quickscythe.quipt.utils.storage;

import me.quickscythe.quipt.utils.CoreUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DataManager {

    private static final InternalStorage storage = new InternalStorage();
    private static File dataFolder;

    public static void init(JavaPlugin plugin) {
        dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) CoreUtils.logger().log("DataManager",  "Creating data folder: " + dataFolder.mkdir());
    }


    public static File getDataFolder() {
        return dataFolder;
    }

    public static InternalStorage getStorage() {
        return storage;
    }

}
