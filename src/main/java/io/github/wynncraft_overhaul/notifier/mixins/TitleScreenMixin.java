package io.github.wynncraft_overhaul.notifier.mixins;

import io.github.wynncraft_overhaul.notifier.Notifier;
import io.github.wynncraft_overhaul.notifier.gui.UpdateAvailableScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        if (Notifier.UPDATE_AVAILABLE) {
            this.client.setScreen(new UpdateAvailableScreen(Text.literal("Update Available!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true).withItalic(true)), Text.literal("You are on version: " + Notifier.LOCAL_VERSION + ", when " + Notifier.UPSTREAM_VERSION + " is the latest version!\nPlease open the installer and update the modpack!"), Text.empty()));
        }
    }
}
