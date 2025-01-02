/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package me.quickscythe.quipt.utils.sessions;

import me.quickscythe.quipt.api.config.Config;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.config.files.SessionConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static final Map<UUID, JSONObject> CURRENT_SESSIONS = new HashMap<>();
    private static SessionConfig config;

    public static void start(JavaPlugin plugin) {
        config = ConfigManager.registerConfig(plugin, SessionConfig.class);
    }

    public static JSONObject getSession(Player player) {
        return CURRENT_SESSIONS.getOrDefault(player.getUniqueId(), null);
    }

    public static Config getConfig() {
        return config;
    }

    public static void finish() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            finishSession(player);
        }
        config.save();
    }

    public static void startSession(Player player) {
        //{"this-is-a-uuid":[{"joined":238}]}
        JSONObject session = new JSONObject();
        session.put("joined", System.currentTimeMillis());
        CURRENT_SESSIONS.put(player.getUniqueId(), session);
        System.out.println("Started session for " + player.getName());
    }

    public static void finishSession(Player player) {
        if (!config.sessions.has(player.getUniqueId().toString()))
            config.sessions.put(player.getUniqueId().toString(), new JSONArray());
        JSONObject session = getSession(player);
        session.put("left", System.currentTimeMillis());
        session.put("playtime", session.getLong("left") - session.getLong("joined"));
        config.sessions.getJSONArray(player.getUniqueId().toString()).put(session);
        CURRENT_SESSIONS.remove(player.getUniqueId());
        System.out.println("Finished session for " + player.getName());
    }

    public static long getOverallPlaytime(Player player) {
        long playtime = 0;
        JSONArray sessions = getSessions(player);
        for (int i = 0; i != sessions.length(); i++) {
            JSONObject session = sessions.getJSONObject(i);
            if (session.has("playtime")) playtime = playtime + session.getLong("playtime");
            else {
                playtime = playtime + (System.currentTimeMillis() - getSession(player).getLong("joined"));
            }
        }
        return playtime;
    }

    public static JSONArray getSessions(Player player) {
        final JSONArray sessions = new JSONArray();
        if (config.sessions.has(player.getUniqueId().toString())) {
            JSONArray old_ses = config.sessions.getJSONArray(player.getUniqueId().toString());
            for (int i = 0; i != old_ses.length(); i++)
                sessions.put(old_ses.getJSONObject(i));
        }
        sessions.put(getSession(player));
        return sessions;

    }
}
