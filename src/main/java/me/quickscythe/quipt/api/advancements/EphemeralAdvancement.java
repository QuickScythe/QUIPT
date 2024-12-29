/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package me.quickscythe.quipt.api.advancements;


import io.papermc.paper.advancement.AdvancementDisplay;
import me.quickscythe.quipt.utils.CoreUtils;
import me.quickscythe.quipt.utils.ItemUtils;
import me.quickscythe.quipt.utils.chat.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.util.Objects;

import static net.kyori.adventure.text.Component.text;

/**
 * An ephemeral advancement that is removed after 5 seconds
 */
public class EphemeralAdvancement {

    private final NamespacedKey key;

    /**
     * Creates a new ephemeral advancement
     *
     * @param plugin the plugin
     * @param data   the data
     */
    public EphemeralAdvancement(JavaPlugin plugin, JSONObject data) {
        this.key = new NamespacedKey(plugin, "ephemeral_" + System.currentTimeMillis() + "_" + data.toString().hashCode());
        Bukkit.getUnsafe().loadAdvancement(key, data.toString());
    }

    /**
     * Sends the advancement to a player
     *
     * @param player the player
     */
    public void send(Player player) {
        AdvancementProgress progress = player.getAdvancementProgress(Objects.requireNonNull(player.getServer().getAdvancement(key)));
        progress.getRemainingCriteria().forEach(progress::awardCriteria);
        Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), () -> {
            progress.getAwardedCriteria().forEach(progress::revokeCriteria);
        }, 20 * 5);

    }

    /**
     * Gets the key of the advancement
     *
     * @return the key
     */
    public NamespacedKey key() {
        return key;
    }

    /**
     * Removes the advancement
     */
    public void remove() {
        Bukkit.getUnsafe().removeAdvancement(key);
    }


    /**
     * A builder for creating an ephemeral advancement
     */
    public static class Builder {

        private final JavaPlugin plugin;
        private final JSONObject data = new JSONObject();

        /**
         * Creates a new builder
         *
         * @param plugin the plugin
         */
        public Builder(JavaPlugin plugin) {
            this.plugin = plugin;
            JSONObject display = new JSONObject();
            display.put("title", new JSONObject(MessageUtils.serialize(text("title"))));
            display.put("description", "description");
            JSONObject displayIcon = new JSONObject();
            displayIcon.put("id", "minecraft:stone");
            display.put("icon", displayIcon);
            display.put("frame", "task");
            display.put("show_toast", true);
            display.put("announce_to_chat", false);
            display.put("hidden", false);

            JSONObject criteria = new JSONObject();
            criteria.put("criteria1", new JSONObject().put("trigger", "minecraft:impossible"));

            data.put("display", display);
            data.put("criteria", criteria);
        }

        /**
         * Sets the title of the advancement
         *
         * @param title the title
         * @return the builder
         */
        public Builder title(String title) {
            data.getJSONObject("display").put("title", new JSONObject(MessageUtils.serialize(text(title))));
            return this;
        }

        /**
         * Sets the title of the advancement
         *
         * @param title the title
         * @return the builder
         */
        public Builder title(Component title) {
            data.getJSONObject("display").put("title", new JSONObject(MessageUtils.serialize(title)));
            return this;
        }

        /**
         * Sets the description of the advancement
         *
         * @param description the description
         * @return the builder
         */
        public Builder description(String description) {
            data.getJSONObject("display").put("description", new JSONObject(MessageUtils.serialize(text(description))));
            return this;
        }

        /**
         * Sets the description of the advancement
         *
         * @param description the description
         * @return the builder
         */
        public Builder description(Component description) {
            data.getJSONObject("display").put("description", new JSONObject(MessageUtils.serialize(description)));
            return this;
        }

        /**
         * Sets the icon of the advancement
         *
         * @param itemStack the item stack
         * @return the builder
         */
        public Builder icon(ItemStack itemStack) {
            JSONObject icon = new JSONObject();
            icon.put("id", itemStack.getType().getKey().toString());
            icon.put("count", itemStack.getAmount());
            JSONObject components;
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) components = new JSONObject();
            else {
                components = ItemUtils.serializeComponents(itemStack);
            }
            icon.put("components", components);
            data.getJSONObject("display").put("icon", icon);
            return this;
        }

        /**
         * Sets the icon of the advancement
         *
         * @param item the material
         * @return the builder
         */
        public Builder icon(Material item) {
            data.getJSONObject("display").getJSONObject("icon").put("id", item.getKey());
            return this;
        }

        /**
         * Sets the frame for the advancement
         *
         * @param frame the design for the advancement
         * @return the builder
         */
        public Builder frame(AdvancementDisplay.Frame frame) {
            data.getJSONObject("display").put("frame", frame.name().toLowerCase());
            return this;
        }

        /**
         * If true the advancement will be shown in a toast
         * @param showToast if true the advancement will be shown in a toast
         * @return the builder
         */
        public Builder toast(boolean showToast) {
            data.getJSONObject("display").put("show_toast", showToast);
            return this;
        }

        /**
         * If true the advancement will be announced in chat
         * @param announceToChat if true the advancement will be announced in chat
         * @return this
         */
        public Builder chat(boolean announceToChat) {
            data.getJSONObject("display").put("announce_to_chat", announceToChat);
            return this;
        }

        /**
         * If true the advancement will be hidden
         * @param hidden if true the advancement will be hidden
         * @return this
         */
        public Builder hidden(boolean hidden) {
            data.getJSONObject("display").put("hidden", hidden);
            return this;
        }

        /**
         * Adds a criteria to the advancement
         *
         * @param criteria the criteria
         * @param trigger  the trigger
         * @return the builder
         */
        public Builder criteria(String criteria, String trigger) {
            data.getJSONObject("criteria").put(criteria, new JSONObject().put("trigger", trigger));
            return this;
        }

        /**
         * Builds the advancement
         *
         * @return the advancement
         */
        public EphemeralAdvancement build() {
            return new EphemeralAdvancement(plugin, data);
        }

    }
}

