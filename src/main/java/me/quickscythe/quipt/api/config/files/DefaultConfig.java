package me.quickscythe.quipt.api.config.files;


import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigFile;
import me.quickscythe.quipt.api.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

@ConfigFile(name = "config")
public class DefaultConfig extends Config {


    @ConfigValue
    public JSONObject resource_pack = new JSONObject().put("server_port", 9009).put("url", "").put("server_ip", "127.0.0.1");

    @ConfigValue
    public JSONObject jenkins = new JSONObject().put("user", "username").put("password", "password").put("url", "http://jenkins.example.com:8080").put("api_endpoint", "/api/xml");

    public DefaultConfig(File file, String name) {
        super(file, name);
    }
}
