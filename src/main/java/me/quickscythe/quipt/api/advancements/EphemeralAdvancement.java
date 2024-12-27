package me.quickscythe.quipt.api.advancements;


import io.papermc.paper.advancement.AdvancementDisplay;
import me.quickscythe.quipt.utils.CoreUtils;
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

public class EphemeralAdvancement {

    private final NamespacedKey key;

    public EphemeralAdvancement(JavaPlugin plugin, JSONObject data) {
        this.key = new NamespacedKey(plugin, "ephemeral_" + System.currentTimeMillis() + "_" + data.toString().hashCode());
        Bukkit.getUnsafe().loadAdvancement(key, data.toString());
    }

    public void send(Player player) {
        AdvancementProgress progress = player.getAdvancementProgress(Objects.requireNonNull(player.getServer().getAdvancement(key)));
        progress.getRemainingCriteria().forEach(progress::awardCriteria);
        Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), () -> {
            progress.getAwardedCriteria().forEach(progress::revokeCriteria);
        }, 20 * 5);

    }

    public NamespacedKey key() {
        return key;
    }

    public void remove() {
        Bukkit.getUnsafe().removeAdvancement(key);
    }


    public static class Builder {

        private final JavaPlugin plugin;
        private final JSONObject data = new JSONObject();

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

        public Builder title(String title) {
            data.getJSONObject("display").put("title", new JSONObject(MessageUtils.serialize(text(title))));
            return this;
        }

        public Builder title(Component title) {
            data.getJSONObject("display").put("title", new JSONObject(MessageUtils.serialize(title)));
            return this;
        }

        public Builder description(String description) {
            data.getJSONObject("display").put("description", new JSONObject(MessageUtils.serialize(text(description))));
            return this;
        }

        public Builder description(Component description) {
            data.getJSONObject("display").put("description", new JSONObject(MessageUtils.serialize(description)));
            return this;
        }

        public Builder icon(ItemStack itemStack) {
            JSONObject icon = new JSONObject();
            icon.put("id", itemStack.getType().getKey().toString());
            icon.put("count", itemStack.getAmount());
            JSONObject components;
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) components = new JSONObject();
            else {
                components = CoreUtils.serializeComponents(itemStack);
            }
            icon.put("components", components);
            data.getJSONObject("display").put("icon", icon);
            return this;
        }

        public Builder icon(Material item) {
            data.getJSONObject("display").getJSONObject("icon").put("id", item.getKey());
            return this;
        }


        public Builder frame(AdvancementDisplay.Frame frame) {
            data.getJSONObject("display").put("frame", frame.name().toLowerCase());
            return this;
        }

        public Builder toast(boolean showToast) {
            data.getJSONObject("display").put("show_toast", showToast);
            return this;
        }

        public Builder chat(boolean announceToChat) {
            data.getJSONObject("display").put("announce_to_chat", announceToChat);
            return this;
        }

        public Builder hidden(boolean hidden) {
            data.getJSONObject("display").put("hidden", hidden);
            return this;
        }

        public Builder criteria(String criteria, String trigger) {
            data.getJSONObject("criteria").put(criteria, new JSONObject().put("trigger", trigger));
            return this;
        }

        public EphemeralAdvancement build() {
            return new EphemeralAdvancement(plugin, data);
        }

    }
}

