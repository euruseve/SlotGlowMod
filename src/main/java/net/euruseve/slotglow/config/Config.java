package net.euruseve.slotglow.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("slotglow.json");

    public enum HighlightMode {
        DEFAULT,
        CUSTOM,
        NO_DIM
    }

    private static Data data = new Data();

    private static class Data {
        HighlightMode highlightMode = HighlightMode.CUSTOM;
        int highlightColor = 0xFFA5D977;
    }

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (var reader = Files.newBufferedReader(CONFIG_PATH)) {
                Data loaded = GSON.fromJson(reader, Data.class);
                if (loaded != null) {
                    data = loaded;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (var writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HighlightMode getHighlightMode() {
        return data.highlightMode;
    }

    public static void setHighlightMode(HighlightMode mode) {
        data.highlightMode = mode;
    }

    public static int getHighlightColor() {
        return data.highlightColor;
    }

    public static void setHighlightColor(int color) {
        data.highlightColor = color;
    }
}