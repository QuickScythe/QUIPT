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

    public static <T extends Config> T registerConfig(JavaPlugin plugin, Class<T> template) {
        try {


            if (template.isAnnotationPresent(ConfigFile.class)) {
                ConfigFile cf = template.getAnnotation(ConfigFile.class);
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
                    if(writtenData.has(configField.getName())) configField.set(content, writtenData.get(configField.getName()));
                }

                data.put(plugin.getName() + "/" + cf.name(), content);
                return content;
            } else {
                throw new IllegalArgumentException("Class must have @ConfigFile annotation");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Config> T getConfig(JavaPlugin plugin, Class<T> clazz) {
        return (T) data.get(plugin.getName() + "/" + clazz.getAnnotation(ConfigFile.class).name());
    }
    public static Config getConfig(String name) {
        return data.get(name);
    }

    private static JSONObject loadJson(File file){
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            return new JSONObject(builder.toString());
        } catch (IOException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", "Failed to load config file");
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "QuiptConfig", e);
            return new JSONObject();
        }
    }

    public static void saveConfig(Config configContent) {
        try {
            File file = configContent.file();
            JSONObject data = new JSONObject();
            for (Field field : configContent.getContentValues()) {
                data.put(field.getName(), field.get(configContent));
            }
            CoreUtils.logger().log("QuiptConfig", "Result: " + writeJson(file, data));
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

    public static void saveAll() {
        for (Config config : data.values()) {
            saveConfig(config);
        }
    }
}
