package io.github.wynncraft_overhaul.notifier.gui;

import io.github.wynncraft_overhaul.notifier.Notifier;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class UpdateAvailableScreen extends WarningScreen {
    public UpdateAvailableScreen() {
        super(
                Text.literal("Update Available!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true).withItalic(true)),
                Text.literal("You are on version: " + Notifier.LOCAL_VERSION + ", when " + Notifier.UPSTREAM_VERSION + " is the latest version.\nPlease open the installer and update the modpack!"),
                Text.literal("Update available!\nYou are on version: " + Notifier.LOCAL_VERSION + ", when " + Notifier.UPSTREAM_VERSION + " is the latest version.\nPlease open the installer and update the modpack!")
        );
    }

    @Override
    protected void initButtons(int yOffset) {
        int l = this.height / 4 + 48;
        int width = 110;
        int margin = (this.width / 3 - width) / 2;

        if (Notifier.INSTALLER_PATH != null) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Open Installer"), (button) -> {
                try {
                    Process process = new ProcessBuilder(Notifier.INSTALLER_PATH.toString()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.client.scheduleStop();
            }).dimensions(this.width / 3 - width, l + 72 + 12, width, 20).build());
        } else {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Quit"), (button) -> {
                this.client.scheduleStop();
            }).dimensions(this.width / 3 - width, l + 72 + 12, width, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Don't Update"), (button) -> {
            Notifier.UPDATE_AVAILABLE = false;
            this.client.setScreen(new TitleScreen());
        }).dimensions(this.width / 3 + margin, l + 72 + 12, width, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Ignore This Version"), (button) -> {
            Notifier.UPDATE_AVAILABLE = false;
            Notifier.config.ignoredVersions.add(Notifier.UPSTREAM_VERSION);
            try {
                Notifier.config.save();
            } catch (IOException e) {
                Notifier.LOGGER.error("Failed to save config!", e);
            }
            this.client.setScreen(new TitleScreen());
        }).dimensions(this.width / 3 + width + margin * 2, l + 72 + 12, width, 20).build());
    }
}
