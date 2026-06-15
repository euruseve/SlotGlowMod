package net.euruseve.slotglow.gui.buttons;

import net.euruseve.slotglow.config.Config;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class ColorPreviewButton {

    private ColorPreviewButton() {
    }

    public static Button create(int x, int y, int width, int height, Button.OnPress onPress) {
        return Button.builder(Component.literal(getCurrentHexColor()), onPress)
                .bounds(x, y, width, height)
                .build();
    }

    public static String getCurrentHexColor() {
        return String.format("#%06X", Config.getHighlightColor() & 0xFFFFFF);
    }

    public static void refresh(Button button) {
        button.setMessage(Component.literal(getCurrentHexColor()));
    }
}