package io.github.wynncraft_overhaul.notifier;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Config {
    private static final Gson gson = new Gson();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("notifier.json");
    public ArrayList<String> ignoredVersions = new ArrayList<>();

    public static Config load() throws IOException {
        Config config;
        if (Files.isRegularFile(PATH)) {
            config = gson.fromJson(new String(Files.readAllBytes(PATH)), Config.class);
        } else {
            config = new Config();
            Files.createDirectories(PATH.getParent());
            Files.createFile(PATH);
            Files.write(PATH, gson.toJson(config).getBytes());
        }
        return config;
    }

    public void save() throws IOException {
        Files.write(PATH, gson.toJson(this).getBytes());
    }
}
