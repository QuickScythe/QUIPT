package me.quickscythe.quipt.api.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Config {

    private final File file;
    private final String name;

    @ConfigValue(override = true)
    public Number version = 1;

    /**
     * @param file The file to save to
     * @param name The name of the config
     */
    public Config(File file, String name) {
        this.file = file;
        this.name = name;
    }

    /**
     * @return The file this config is saved to
     */
    public File file() {
        return file;
    }

    /**
     * @return The name of this config
     */
    public String name() {
        return name;
    }

    /**
     * Saves the config
     */
    public void save() {
        ConfigManager.saveConfig(this);
    }

    /**
     * @return The fields annotated with @ConfigValue
     */
    Field[] getContentValues() {
        return Arrays.stream(this.getClass().getFields()).filter(f -> f.isAnnotationPresent(ConfigValue.class)).toArray(Field[]::new);
    }
}
