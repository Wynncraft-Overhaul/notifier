package io.github.wynncraft_overhaul.notifier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class Notifier implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("notifier");
	public static final Path MANIFEST_PATH = FabricLoader.getInstance().getGameDir().resolve("manifest.json");
	public static Boolean UPDATE_AVAILABLE = false;
	public static Path INSTALLER_PATH;
	public static String UPSTREAM_VERSION;
	public static String LOCAL_VERSION;

	@Override
	public void onInitialize() {
		Gson gson = new Gson();
		try {
			JsonObject local_manifest = gson.fromJson(Files.readString(MANIFEST_PATH), JsonObject.class);
			if (!local_manifest.has("source") || !local_manifest.has("modpack_version") || !local_manifest.has("installer_path")) {
				LOGGER.error("Invalid local manifest!");
				return;
			}
			String source = local_manifest.get("source").getAsString();
			String[] source_split = source.split("/");
			LOCAL_VERSION = local_manifest.get("modpack_version").getAsString();
			JsonObject upstream_manifest = gson.fromJson(HttpClient.newHttpClient().send(HttpRequest.newBuilder(new URI("https://raw.githubusercontent.com/" + source + "/manifest.json")).GET().build(), HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
			if (!upstream_manifest.has("modpack_version")) {
				LOGGER.error("Invalid upstream manifest!");
				return;
			}
			UPSTREAM_VERSION = upstream_manifest.get("modpack_version").getAsString();
			if (!UPSTREAM_VERSION.equals(LOCAL_VERSION)) {
				UPDATE_AVAILABLE = true;
				Path installer_path = Path.of(local_manifest.get("installer_path").getAsString());
				if (installer_path.toFile().isFile()) {
					INSTALLER_PATH = installer_path;
				}
			}
		} catch (IOException e) {
			LOGGER.error("Could not read local manifest!");
		} catch (URISyntaxException e) {
			LOGGER.error(String.valueOf(e));
			LOGGER.error("Invalid source!");
		} catch (InterruptedException e) {
			LOGGER.error("Failed to get upstream manifest!");
		}
	}
}
