package me.quickscythe.quipt.commands;

import me.quickscythe.quipt.commands.executors.ResourcePackCommand;
import me.quickscythe.quipt.commands.executors.UpdateCommand;
import me.quickscythe.quipt.utils.CoreUtils;

public class CommandManager {
    public static void init() {

        new CommandExecutor.Builder(new UpdateCommand(CoreUtils.plugin())).setDescription("Update Quick's plugins").setAliases("getnew").register();
        new CommandExecutor.Builder(new ResourcePackCommand(CoreUtils.plugin())).setDescription("Command to manage Resource Pack").setAliases("rp", "resource", "pack").register();
    }


}
