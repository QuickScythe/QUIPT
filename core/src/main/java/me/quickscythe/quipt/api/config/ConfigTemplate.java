package me.quickscythe.quipt.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to specify the template of a config file
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigTemplate {

    /**
     * The name of the config file
     * @return the name of the config file
     */
    String name();

    /**
     * The extension of the config file
     * @return the extension of the config file
     */
    String ext() default "json";
}
