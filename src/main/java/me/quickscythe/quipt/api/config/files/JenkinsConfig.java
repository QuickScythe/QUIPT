package me.quickscythe.quipt.api.config.files;

import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigFile;
import me.quickscythe.quipt.api.config.ConfigValue;

import java.io.File;

@ConfigFile(name = "jenkins")
public class JenkinsConfig extends Config {

    @ConfigValue
    public String url = "https://ci.quickscythe.me";

    @ConfigValue
    public String username = "admin";

    @ConfigValue
    public String password = "password";

    @ConfigValue
    public String api_endpoint = "/api/json";

    public JenkinsConfig(File file, String name) {
        super(file, name);
    }
}
