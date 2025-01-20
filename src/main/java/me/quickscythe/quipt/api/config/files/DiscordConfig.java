package me.quickscythe.quipt.api.config.files;

import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigTemplate;
import me.quickscythe.quipt.api.config.ConfigValue;

import java.io.File;

@ConfigTemplate(name = "discord")
public class DiscordConfig extends Config {

    @ConfigValue
    public boolean enable_bot = false;

    @ConfigValue
    public String bot_token = "<token_here>";

    @ConfigValue
    public String server_status_channel = "none";

    @ConfigValue
    public String player_status_channel = "none";

    @ConfigValue
    public String chat_message_channel = "none";

    public DiscordConfig(File file, String name) {
        super(file, name);
    }
}
