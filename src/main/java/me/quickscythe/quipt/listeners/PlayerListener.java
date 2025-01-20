package me.quickscythe.quipt.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.quickscythe.Bot;
import me.quickscythe.api.guild.QuiptGuild;
import me.quickscythe.api.guild.channel.QuiptTextChannel;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.config.files.DiscordConfig;
import me.quickscythe.quipt.utils.CoreUtils;
import me.quickscythe.quipt.utils.chat.MessageUtils;
import me.quickscythe.quipt.utils.network.NetworkUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PlayerListener implements Listener {
    public PlayerListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException, NoSuchAlgorithmException {
        CoreUtils.packServer().setPack(e.getPlayer());
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.plugin(), DiscordConfig.class);
        if (config.enable_bot) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.player_status_channel) || channel.getId().equalsIgnoreCase(config.player_status_channel)) {
                        channel.sendPlayerMessage(e.getPlayer().getUniqueId(), e.getPlayer().getName(), MessageUtils.plainText(e.joinMessage()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.plugin(), DiscordConfig.class);
        if (config.enable_bot) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.player_status_channel) || channel.getId().equalsIgnoreCase(config.player_status_channel)) {
                        channel.sendPlayerMessage(e.getPlayer().getUniqueId(), e.getPlayer().getName(), MessageUtils.plainText(e.quitMessage()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.plugin(), DiscordConfig.class);
        if (config.enable_bot) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.player_status_channel) || channel.getId().equalsIgnoreCase(config.player_status_channel)) {
                        channel.sendPlayerMessage(e.getPlayer().getUniqueId(), e.getPlayer().getName(), MessageUtils.plainText(e.deathMessage()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if (e.isCancelled()) return;
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.plugin(), DiscordConfig.class);
        if (config.enable_bot) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.chat_message_channel) || channel.getId().equalsIgnoreCase(config.chat_message_channel)) {
                        channel.sendPlayerMessage(e.getPlayer().getUniqueId(), e.getPlayer().getName(), MessageUtils.plainText(e.message()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

}
