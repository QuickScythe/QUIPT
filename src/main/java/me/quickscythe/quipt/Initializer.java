package me.quickscythe.quipt;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.commands.CommandManager;
import me.quickscythe.quipt.listeners.PlayerListener;
import me.quickscythe.quipt.utils.CoreUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Initializer extends JavaPlugin {

    @Override
    public void onEnable() {
        CoreUtils.init(this);
        // Plugin startup logic

        CommandManager.init();

        new PlayerListener(this);


    }

    @Override
    public void onDisable() {
        ConfigManager.saveAll();
        // Plugin shutdown logic
    }
}
