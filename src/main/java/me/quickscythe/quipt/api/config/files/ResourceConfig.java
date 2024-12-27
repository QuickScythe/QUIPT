package me.quickscythe.quipt.api.config.files;

import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigFile;
import me.quickscythe.quipt.api.config.ConfigValue;

import java.io.File;

@ConfigFile(name = "resources")
public class ResourceConfig extends Config {

    @ConfigValue
    public String repo_url = "";

    @ConfigValue
    public String repo_branch = "main";

    @ConfigValue
    public boolean is_private = false;

    @ConfigValue
    public String repo_username = "user";

    @ConfigValue
    public String repo_password = "pass";

    @ConfigValue
    public int server_port = 9009;

    @ConfigValue
    public String server_ip = "127.0.0.1";

    public ResourceConfig(File file, String name) {
        super(file, name);
    }
}
