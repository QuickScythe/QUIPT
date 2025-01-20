package me.quickscythe.quipt.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for marking fields in a {@link Config} class as a value that should be loaded from the config file
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigValue {

    /**
     * Whether this value should be overridden when the config is loaded
     * @return true if the value should be overridden, false otherwise
     */
    boolean override() default false;
}
