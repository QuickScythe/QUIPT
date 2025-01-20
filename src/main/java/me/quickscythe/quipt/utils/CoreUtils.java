package me.quickscythe.quipt.utils;


import me.quickscythe.Bot;
import me.quickscythe.api.embed.Embed;
import me.quickscythe.api.guild.QuiptGuild;
import me.quickscythe.api.guild.channel.QuiptTextChannel;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.config.files.DiscordConfig;
import me.quickscythe.quipt.api.config.files.HashesConfig;
import me.quickscythe.quipt.api.config.files.JenkinsConfig;
import me.quickscythe.quipt.api.config.files.ResourceConfig;
import me.quickscythe.quipt.utils.chat.Logger;
import me.quickscythe.quipt.utils.chat.MessageUtils;
import me.quickscythe.quipt.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.quipt.utils.events.EventManager;
import me.quickscythe.quipt.utils.heartbeat.HeartbeatUtils;
import me.quickscythe.quipt.utils.network.resources.ResourcePackServer;
import me.quickscythe.quipt.utils.sessions.SessionManager;
import me.quickscythe.quipt.utils.teleportation.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;

public class CoreUtils {

    private static Logger logger;
    private static JavaPlugin plugin;
    private static ResourcePackServer packserver;
    private static File dataFolder;


    public static void init(JavaPlugin plugin) {
        CoreUtils.plugin = plugin;
        logger = new Logger(plugin);
        dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) CoreUtils.logger().log("Initializer", "Creating data folder: " + dataFolder.mkdir());
        ResourceConfig resourceConfig = ConfigManager.registerConfig(plugin, ResourceConfig.class);
        ConfigManager.registerConfig(plugin, JenkinsConfig.class);
        ConfigManager.registerConfig(plugin, HashesConfig.class);
        DiscordConfig discordConfig = ConfigManager.registerConfig(plugin, DiscordConfig.class);
        SessionManager.start(plugin);
        EventManager.init();

        PlaceholderUtils.registerPlaceholders();
        MessageUtils.start();
        packserver = new ResourcePackServer();

        if (!resourceConfig.repo_url.isEmpty()) {
            packserver.setUrl(resourceConfig.repo_url);
        }

        HeartbeatUtils.init(plugin);

        LocationUtils.start(plugin);

        if (discordConfig.enable_bot) {
            logger().log("Initializer", "Starting discord bot");
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                Bot.start(discordConfig.json());
                for (QuiptGuild guild : Bot.qda().getGuilds()) {
                    for (QuiptTextChannel channel : guild.getTextChannels()) {

                        if (channel.getName().equalsIgnoreCase(discordConfig.server_status_channel) || channel.getId().equalsIgnoreCase(discordConfig.server_status_channel)) {
                            Embed embed = new Embed();
                            embed.title("Server Status");
                            embed.description("Server has started.");
                            embed.color(Color.GREEN.getRGB());
                            channel.sendMessage(embed);
                        }
                    }
                }
            }, 20);

        }
    }

    public static File dataFolder() {
        return dataFolder;
    }


    public static Logger logger() {
        return logger;
    }

    public static JavaPlugin plugin() {
        return plugin;
    }


    public static ResourcePackServer packServer() {
        return packserver;
    }


}
