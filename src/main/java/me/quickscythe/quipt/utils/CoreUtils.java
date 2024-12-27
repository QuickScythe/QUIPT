package me.quickscythe.quipt.utils;


import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.config.files.DefaultConfig;
import me.quickscythe.quipt.api.config.files.HashesConfig;
import me.quickscythe.quipt.api.config.files.JenkinsConfig;
import me.quickscythe.quipt.api.config.files.ResourceConfig;
import me.quickscythe.quipt.utils.chat.Logger;
import me.quickscythe.quipt.utils.chat.MessageUtils;
import me.quickscythe.quipt.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.quipt.utils.network.resources.ResourcePackServer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;

public class CoreUtils {

    private static Logger logger;
    private static DefaultConfig config;
    private static JavaPlugin plugin;
    private static ResourcePackServer packserver;
    private static File dataFolder;


    public static void init(JavaPlugin plugin) {
        CoreUtils.plugin = plugin;
        logger = new Logger(plugin);
        dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) CoreUtils.logger().log("DataManager", "Creating data folder: " + dataFolder.mkdir());
        config = ConfigManager.registerConfig(plugin, DefaultConfig.class);
        ResourceConfig resourceConfig = ConfigManager.registerConfig(plugin, ResourceConfig.class);
        ConfigManager.registerConfig(plugin, JenkinsConfig.class);
        ConfigManager.registerConfig(plugin, HashesConfig.class);

        PlaceholderUtils.registerPlaceholders();
        MessageUtils.start();
        packserver = new ResourcePackServer();

        if (!resourceConfig.repo_url.isEmpty()) {
            packserver.setUrl(resourceConfig.repo_url);
        }
    }

    public static File dataFolder() {
        return dataFolder;
    }


    public static Logger logger() {
        return logger;
    }

    public static JavaPlugin plugin() {
        return plugin;
    }

    public static DefaultConfig config() {
        return config;
    }

    public static ResourcePackServer packServer() {
        return packserver;
    }

    public static JSONObject serializeComponents(ItemStack itemStack) {
        String input = itemStack.getItemMeta().getAsComponentString();
        input = input.substring(1, input.length() - 1);

        String[] pairs = input.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        JSONObject jsonObject = new JSONObject();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            if (value.startsWith("{") && value.endsWith("}")) {
                jsonObject.put(key, new JSONObject(value));
            } else {
                jsonObject.put(key, Integer.parseInt(value));
            }
        }

        return jsonObject;
    }
}
