package me.quickscythe.quipt;

import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.commands.CommandManager;
import me.quickscythe.quipt.listeners.PlayerListener;
import me.quickscythe.quipt.utils.CoreUtils;
import org.bukkit.plugin.java.JavaPlugin;

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
