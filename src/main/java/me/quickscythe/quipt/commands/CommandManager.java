package me.quickscythe.quipt.commands;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.quickscythe.quipt.commands.executors.ResourcePackCommand;
import me.quickscythe.quipt.commands.executors.UpdateCommand;
import me.quickscythe.quipt.utils.CoreUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandManager {
    public static void init() {

        new CommandBuilder(new UpdateCommand(CoreUtils.plugin())).setDescription("Update Quick's plugins").setAliases("getnew").register();
        new CommandBuilder(new ResourcePackCommand(CoreUtils.plugin())).setDescription("Command to manage Resource Pack").setAliases("rp", "resource", "pack").register();
    }

    public static class CommandBuilder {
        CommandExecutor cmd;
        String desc = "";
        String[] aliases = new String[]{};


        @CheckReturnValue
        public CommandBuilder(CommandExecutor executor) {
            this.cmd = executor;
        }

        @CheckReturnValue
        public CommandBuilder setDescription(String desc) {
            this.desc = desc;
            return this;
        }

        @CheckReturnValue
        public CommandBuilder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public void register() {
            @NotNull LifecycleEventManager<Plugin> manager = cmd.getPlugin().getLifecycleManager();
            manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
                final Commands commands = event.registrar();
                commands.register(cmd.execute(), desc, List.of(aliases));
            });
        }
    }
}
