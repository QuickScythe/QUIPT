package me.quickscythe.quipt.utils.network.discord;

import me.quickscythe.quipt.api.exceptions.SimpleQuiptException;
import me.quickscythe.quipt.utils.network.discord.embed.Embed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class WebhookManager {

    private final static Map<String, Webhook> WEBHOOKS = new HashMap<>();


    public static Webhook add(String name, String id, String token) {
        Webhook hook = new Webhook(id, token);
        WEBHOOKS.put(name, hook);
        return hook;
    }

    public static Webhook get(String name) {
        return WEBHOOKS.getOrDefault(name, null);
    }

    public static void send(String webhookName, Embed embed) throws SimpleQuiptException {
        send(get(webhookName), embed);
    }

    public static void send(Webhook hook, Embed embed) throws SimpleQuiptException {
        JSONObject data = new JSONObject();
        data.put("embeds", new JSONArray().put(embed.json()));
        send(hook, data);
    }

    public static void send(String webhookName, JSONObject data) throws SimpleQuiptException {
        send(get(webhookName), data);
    }

    public static void send(String webhookName, String message) throws SimpleQuiptException {
        send(get(webhookName), message);
    }

    public static void send(Webhook hook, String message) throws SimpleQuiptException {
        JSONObject data = new JSONObject();
        data.put("content", message);
        send(hook, data);

    }

    public static void send(Webhook hook, JSONObject data) throws SimpleQuiptException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(hook.url())).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(data.toString())).build();

        final HttpClient client = HttpClient.newHttpClient();

        final HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new SimpleQuiptException("Failed to send http request!");
        }

        final int statusCode = response.statusCode();
        if (!(statusCode >= 200 && statusCode < 300)) {
            throw new SimpleQuiptException("Http status code " + statusCode + "! Response was: '" + response.body() + "'.");
        }

        // From JDK 21 the HttpClient class extends AutoCloseable, but as we want to support Minecraft versions
        //  that use JDK 17, where HttpClient doesn't extend AutoCloseable, we need to check if it's
        //  an instance of AutoCloseable before trying to close it.
        //noinspection ConstantValue
        if (client instanceof AutoCloseable) {
            try {
                ((AutoCloseable) client).close();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
