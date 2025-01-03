package me.quickscythe.quipt.api.config;

import me.quickscythe.quipt.utils.CoreUtils;
import me.quickscythe.quipt.utils.chat.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConfigManager {

    private static final Map<String, Config> data = new HashMap<>();

    /**
     * Registers a config file for a plugin
     * @param plugin The plugin to register the config file for
     * @param template The class of the config file
     * @param <T> The type of the config file
     * @return The config file
     */
    public static <T extends Config> T registerConfig(JavaPlugin plugin, Class<T> template) {
        try {


            if (template.isAnnotationPresent(ConfigTemplate.class)) {
                ConfigTemplate cf = template.getAnnotation(ConfigTemplate.class);
                CoreUtils.logger().log("QuiptConfig", "Registering config file \"" + cf.name() + "\".");
                if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
                File file = new File(plugin.getDataFolder(), cf.name() + "." + cf.ext());
                if(!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if(!file.exists()) {
                    CoreUtils.logger().log("QuiptConfig", "Config file \"" + cf.name() + "\" does not exist. Creating...");
                    CoreUtils.logger().log("QuiptConfig", file.createNewFile() ? "Success" : "Failure");
                }
                T content = template.getConstructor(File.class, String.class).newInstance(file, cf.name());

                //Variables set. Now time to load the file or default values
                JSONObject writtenData = loadJson(file);

                for (Field configField : content.getContentValues()) {
                    if(writtenData.has(configField.getName())) {
                        Object writtenValue = writtenData.get(configField.getName());
                        if(configField.getType().isEnum()){
                            writtenValue = Enum.valueOf((Class<Enum>) configField.getType(), (String) writtenValue);
                        }
                        configField.set(content, writtenValue);
                    }
                }

                data.put(plugin.getName() + "/" + cf.name(), content);
                content.save();
                return content;
            } else {
                throw new IllegalArgumentException("Class must have @ConfigFile annotation");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a config file for a plugin
     * @param plugin The plugin to get the config file for
     * @param clazz The class of the config file
     * @param <T> The type of the config file
     * @return The config file
     */
    public static <T extends Config> T getConfig(JavaPlugin plugin, Class<T> clazz) {
        return (T) data.get(plugin.getName() + "/" + clazz.getAnnotation(ConfigTemplate.class).name());
    }

    /**
     * Gets a config file by name
     * @param name The name of the config file
     * @return The config file
     */
    public static Config getConfig(String name) {
        return data.get(name);
    }

    private static JSONObject loadJson(File file){
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            String content = builder.toString();
            return content.isEmpty() || content.isBlank() ? new JSONObject() : new JSONObject(builder.toString());
        } catch (IOException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", "Failed to load config file");
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", e);
            return new JSONObject();
        }
    }



    /**
     * Saves a config file
     * @param configContent The config file to save
     */
    public static void saveConfig(Config configContent) {
        try {
            File file = configContent.file();
            JSONObject data = new JSONObject();
            for (Field field : configContent.getContentValues()) {
                data.put(field.getName(), field.get(configContent));
            }
            CoreUtils.logger().log("QuiptConfig", "Saving %s: ".formatted(configContent.name()) + (writeJson(file, data) ? "Success" : "Failed"));
        } catch (IllegalAccessException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", "Failed to save config file");
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", e);
        }
    }

    private static boolean writeJson(File file, JSONObject data){
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data.toString(2));
            return true;
        } catch (IOException e) {
            return false;
        }


    }

    /**
     * Saves all config files
     */
    public static void saveAll() {
        for (Config config : data.values()) {
            saveConfig(config);
        }
    }
}
