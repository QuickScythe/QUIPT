package me.quickscythe.quipt.listeners;

import io.papermc.paper.advancement.AdvancementDisplay;
import me.quickscythe.quipt.api.advancements.EphemeralAdvancement;
import me.quickscythe.quipt.utils.CoreUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static net.kyori.adventure.text.Component.text;


public class PlayerListener implements Listener {
    public PlayerListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, NoSuchAlgorithmException {
        CoreUtils.packServer().setPack(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.DIAMOND) {
            EphemeralAdvancement.Builder builder = new EphemeralAdvancement.Builder(CoreUtils.plugin());
            builder.title(text("Test", NamedTextColor.RED))
                    .description("description")
                    .icon(Material.DIAMOND)
                    .frame(AdvancementDisplay.Frame.TASK)
                    .chat(true);
            EphemeralAdvancement advancement = builder.build();
            advancement.send(event.getPlayer());
        }
    }

}
