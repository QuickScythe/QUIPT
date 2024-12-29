/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package me.quickscythe.quipt.listeners;

import me.quickscythe.quipt.utils.sessions.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class SessionListener implements Listener {

    public SessionListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        SessionManager.startSession(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        SessionManager.finishSession(e.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        JSONObject session = SessionManager.getSession(e.getPlayer());
        String key = "blocks_mined";
        session.put(key, session.has(key) ? session.getInt(key) + 1 : 1);
    }
}
