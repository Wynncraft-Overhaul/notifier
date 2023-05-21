package io.github.wynncraft_overhaul.notifier.gui;

import io.github.wynncraft_overhaul.notifier.Notifier;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.IOException;

public class UpdateAvailableScreen extends WarningScreen {
    public UpdateAvailableScreen(Text header, Text message, Text narratedText) {
        super(header, message, narratedText);
    }

    @Override
    protected void initButtons(int yOffset) {
        int l = this.height / 4 + 48;

        if (Notifier.INSTALLER_PATH != null) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Open Installer"), (button) -> {
                try {
                    Process process = new ProcessBuilder(Notifier.INSTALLER_PATH.toString()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.client.scheduleStop();
            }).dimensions(this.width / 2 - 100, l + 72 + 12, 98, 20).build());
        } else {
            this.addDrawableChild(ButtonWidget.builder(Text.literal("Quit"), (button) -> {
                this.client.scheduleStop();
            }).dimensions(this.width / 2 - 100, l + 72 + 12, 98, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Continue"), (button) -> {
            Notifier.UPDATE_AVAILABLE = false;
            this.client.setScreen(new TitleScreen());
        }).dimensions(this.width / 2 + 2, l + 72 + 12, 98, 20).build());
    }
}
