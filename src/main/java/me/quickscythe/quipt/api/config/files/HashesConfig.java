package me.quickscythe.quipt.api.config.files;

import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigTemplate;
import me.quickscythe.quipt.api.config.ConfigValue;

import java.io.File;

@ConfigTemplate(name = "hashes")
public class HashesConfig extends Config {

    @ConfigValue
    public String encrypted_zip_hash = "";

    @ConfigValue
    public String commit_hash = "";

    public HashesConfig(File file, String name) {
        super(file, name);
    }
}
