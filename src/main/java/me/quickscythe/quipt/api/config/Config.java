package me.quickscythe.quipt.api.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Config {

    private final File file;
    private final String name;

    @ConfigValue(override = true)
    public Number version = 1;

    public Config(File file, String name) {
        this.file = file;
        this.name = name;
    }

    public File file() {
        return file;
    }

    public String name() {
        return name;
    }

    public void save() {
        ConfigManager.saveConfig(this);
    }

    Field[] getContentValues() {
        return Arrays.stream(this.getClass().getFields()).filter(f -> f.isAnnotationPresent(ConfigValue.class)).toArray(Field[]::new);
    }
}
