package me.quickscythe.quipt.utils.storage;



import me.quickscythe.quipt.utils.CoreUtils;
import me.quickscythe.quipt.utils.chat.Logger;
import org.apache.logging.log4j.core.Core;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InternalStorage {

    private static final long RETENTION_TIME = TimeUnit.MINUTES.toMillis(30); // 30 minutes
    private final Map<String, Long> lastModified = new java.util.HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    JSONObject data = new JSONObject();

    public InternalStorage() {
        startGarbageCollector();
    }

    private void startGarbageCollector() {
        scheduler.scheduleAtFixedRate(this::runGarbageCollector, 5, 5, TimeUnit.MINUTES);
    }

    private void runGarbageCollector() {
        long currentTime = System.currentTimeMillis();
        for (String key : new HashSet<>(lastModified.keySet())) {
            if (currentTime - lastModified.get(key) > RETENTION_TIME) {
                saveAndRemove(key);
            }
        }
    }

    public void saveAndRemove(String key) {
        Object value = get(key);
        if (value != null) {
            write(key);
            remove(key);
        }
    }

    public void remove(String key) {
        String[] paths = key.split("\\.");
        JSONObject current = data;
        for (int i = 0; i < paths.length - 1; i++) {
            current = current.getJSONObject(paths[i]);
        }
        current.remove(paths[paths.length - 1]);
        lastModified.remove(key);
    }

    public void set(String path, Object value) {
        String[] paths = path.split("\\.");
        lastModified.put(path, System.currentTimeMillis());

        JSONObject current = data;
        for (int i = 0; i < paths.length; i++) {
            if (i == paths.length - 1) {
                current.put(paths[i], value);
            } else {
                if (!current.has(paths[i]) || !(current.get(paths[i]) instanceof JSONObject)) {
                    current.put(paths[i], new JSONObject());
                }
                current = current.getJSONObject(paths[i]);
            }
        }
    }


    public Object get(String path) {
//        path = StorageManager.getConfigFolder().getName() + "." + path;
        String[] paths = path.split("\\.");

        JSONObject current = data;
        for (int i = 0; i < paths.length; i++) {
            if (i == paths.length - 1) {
                return current.get(paths[i]);
            } else {
                if (!current.has(paths[i])) {
                    return null;

                }
                current = current.getJSONObject(paths[i]);
            }
        }
        return current;

    }

    public JSONObject root() {
        return data;
    }


    public void write(String path) {
        String fileName = path.replaceAll("\\.", "/") + ".json";

        try {
            File file = new File(DataManager.getDataFolder(), fileName);
            CoreUtils.logger().logger().info("test {}", "test");
            CoreUtils.logger().log("InternalStorage", "Checking if file exists: " + file.getAbsolutePath());
            if (!(file.getParentFile() == null)) if (!file.getParentFile().exists()) {
                String filePath = file.getParentFile().getPath();
                boolean result = file.getParentFile().mkdirs();
                CoreUtils.logger().log("InternalStorage", "Creating directories " + filePath + ": " + result);
            }
            if (!file.exists())
                CoreUtils.logger().log("InternalStorage", "Creating file " + file.getName() + ": " + file.createNewFile());
            FileWriter f2 = new FileWriter(file, false);
            Object object = get(path);
            String context;
            if (object instanceof JSONObject) {
                context = ((JSONObject) object).toString(2);
            } else if (object instanceof JSONArray) {
                context = ((JSONArray) object).toString(2);
            } else {
                context = new JSONObject(object).toString(2);
            }

//            System.out.println(context);
            CoreUtils.logger().log("InternalStorage", "Writing to file: " + fileName);
            f2.write(context);
            f2.close();
            CoreUtils.logger().log("InternalStorage", "File written: " + fileName);
        } catch (Exception e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "InternalStorage", "There was an error writing to file: " + fileName);
            CoreUtils.logger().error("InternalStorage", e);
        }

    }

    public Object load(String path) {
        return load(new File(DataManager.getDataFolder(),path.replaceAll("\\.", "/") + ".json"));
    }

    public Object load(File file) {
        CoreUtils.logger().log("InternalStorage", "Loading " + file.getAbsolutePath());
        try {
            CoreUtils.logger().log("InternalStorage", "File exists: " +  file.exists());
            Scanner scanner = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) sb.append(scanner.nextLine());

            scanner.close();
            String key = file.getPath().split("\\.")[0].replaceAll("/", ".").replaceAll("\\\\", ".");
            if(key.startsWith("config")){
                key = key.substring(21);
            }
            CoreUtils.logger().log("InternalStorage", "Loading " + file.getName() + " into " + key);
            String context = sb.toString();

            Object o = context.isEmpty() ? null : context;

            if (context.startsWith("{")) {
                o = new JSONObject(context);
            }
            if (context.startsWith("[")) {
                o = new JSONArray(context);
            }
            if (o != null) set(key, o);
            return o;
        } catch (IOException e) {
            if (file.isDirectory()) {
                CoreUtils.logger().log(Logger.LogLevel.ERROR, "InternalStorage", "Can not write to folder: " + file.getName());
                CoreUtils.logger().error("InternalStorage", e);
            } else {
                CoreUtils.logger().log(Logger.LogLevel.ERROR, "InternalStorage", "Can not write to file: " + file.getName());
                CoreUtils.logger().error("InternalStorage", e);
            }
        }
        return null;
    }

    public boolean has(String path) {
        return get(path) != null;
    }


    //* Extra stuff

    public JSONObject getJsonObject(String path) {
        return (JSONObject) get(path);
    }

    public JSONArray getJsonArray(String path) {
        return (JSONArray) get(path);
    }

    public String getString(String path) {
        return (String) get(path);
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public float getFloat(String path) {
        return (float) get(path);
    }

    public double getDouble(String path) {
        return (double) get(path);
    }

    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }
}