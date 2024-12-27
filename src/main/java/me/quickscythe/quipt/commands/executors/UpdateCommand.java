package me.quickscythe.quipt.commands.executors;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.quipt.api.config.ConfigManager;
import me.quickscythe.quipt.api.config.files.JenkinsConfig;
import me.quickscythe.quipt.commands.CommandExecutor;
import me.quickscythe.quipt.utils.CoreUtils;
import me.quickscythe.quipt.utils.chat.Logger;
import me.quickscythe.quipt.utils.chat.MessageUtils;
import me.quickscythe.quipt.utils.network.NetworkUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import static net.kyori.adventure.text.Component.text;

public class UpdateCommand extends CommandExecutor {

    String[] props;


    public UpdateCommand(JavaPlugin plugin) {
        super(plugin, "update");
        JenkinsConfig jenkinsConfig = ConfigManager.getConfig(plugin, JenkinsConfig.class);
        props = new String[]{jenkinsConfig.username, jenkinsConfig.password, jenkinsConfig.url, jenkinsConfig.api_endpoint};
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> logError(context.getSource().getSender(), "Usage: /update <plugin> <version|latest>")).then(argument("plugin", StringArgumentType.string()).suggests(this::getPluginList).executes(context -> logError(context.getSource().getSender(), "Usage: /update <plugin> <version|latest>")).then(argument("version", StringArgumentType.string()).suggests(this::getVersionList).executes(context -> {
            if (!context.getSource().getSender().hasPermission("quipt.admin.update"))
                return logError(context.getSource().getSender(), MessageUtils.getMessage("cmd.error.no_perm"));
            String plugin = StringArgumentType.getString(context, "plugin");
            String version = StringArgumentType.getString(context, "version");
            String filename = plugin + "-" + version + ".jar";
            String url = props[2] + "/job/" + plugin + "/lastSuccessfulBuild/artifact/build/libs/" + filename;
            //Downloading <plugin> <version>...
            CoreUtils.logger().log(Logger.LogLevel.INFO, "Updater", text("Downloading ", NamedTextColor.WHITE).append(getStylizedName(plugin, version)).append(text("...", NamedTextColor.YELLOW)), context.getSource().getSender());
            InputStream in = NetworkUtils.downloadFile(url, props[0], props[1]);
            if (in != null) {
                try {

                    //TODO Make getMessage(<key>) work with placeholders
                    for (File file : CoreUtils.plugin().getDataFolder().getParentFile().listFiles()) {
                        String name = file.getName();
                        if (name.startsWith(plugin) && file.isFile()) {
                            //Found existing file.
                            CoreUtils.logger().log(Logger.LogLevel.INFO, "Updater", text("Found existing file.").color(NamedTextColor.YELLOW), context.getSource().getSender());
                            String old_version = name.replaceAll(plugin + "-", "").replaceAll(".jar", "");
                            Files.deleteIfExists(file.toPath());
                            //<plugin> <version> has veen deleted.
                            CoreUtils.logger().log(Logger.LogLevel.INFO, "Updater", text().content("").append(getStylizedName(plugin, old_version)).append(text(" has been deleted.", NamedTextColor.YELLOW)).build(), context.getSource().getSender());
                        }
                    }
                    NetworkUtils.saveStream(in, new FileOutputStream("plugins/" + filename));
                    //Finished downloading <plugin> <name>.
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Updater", text().content("Finished downloading ").color(NamedTextColor.YELLOW).append(getStylizedName(plugin, version)).append(text(".", NamedTextColor.WHITE)).build(), context.getSource().getSender());
                } catch (FileNotFoundException e) {
                    CoreUtils.logger().log(Logger.LogLevel.ERROR, "Updater", e);
                } catch (IOException e) {
                    CoreUtils.logger().log(Logger.LogLevel.ERROR, "Updater", "ERROR");
                    throw new RuntimeException(e);
                }
            }
            return Command.SINGLE_SUCCESS;
        }))).build();
    }

    private CompletableFuture<Suggestions> getVersionList(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            String plugin = context.getInput().split(" ")[1];
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document xml = db.parse(NetworkUtils.downloadFile(props[2] + "job/" + plugin + "/lastSuccessfulBuild/" + props[3], props[0], props[1]));
            xml.getDocumentElement().normalize();
            NodeList versions = xml.getElementsByTagName("artifact");
            for (int temp = 0; temp < versions.getLength(); temp++) {
                Node job = versions.item(temp);
                String s = ((Element) job).getElementsByTagName("fileName").item(0).getTextContent();
                if (s.endsWith("-javadoc.jar") || s.endsWith("-sources.jar") || s.endsWith("-reobf.jar")) continue;
                s = s.replaceAll(plugin + "-", "");
                s = s.replaceAll(".jar", "");
                builder.suggest(s);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> getPluginList(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.parse(NetworkUtils.downloadFile(props[2] + props[3], props[0], props[1]));
            xml.getDocumentElement().normalize();
            NodeList jobs = xml.getElementsByTagName("job");
            for (int temp = 0; temp < jobs.getLength(); temp++) {
                Node job = jobs.item(temp);
                if (((Element) job).getAttribute("_class").equalsIgnoreCase("org.jenkinsci.plugins.workflow.job.WorkflowJob")) {
                    String s = ((Element) job).getElementsByTagName("name").item(0).getTextContent();
                    builder.suggest(s);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new RuntimeException(ex);
        }
        return builder.buildFuture();
    }

    private Component getStylizedName(String plugin, String version) {
        TextColor AQUA = NamedTextColor.AQUA;
        TextColor GREEN = NamedTextColor.GREEN;
        return text().content("").color(NamedTextColor.WHITE).append(text(plugin, AQUA)).append(text(" v" + version, GREEN)).build();
    }
}
