package net.euruseve.slotglow.gui;

import net.euruseve.slotglow.config.Config;
import net.euruseve.slotglow.gui.buttons.ColorSlider;
import net.euruseve.slotglow.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ColorPickerScreen extends Screen {
    private final Screen parentScreen;
    private final Runnable onApply;
    private int red, green, blue;
    private int selectedColor;

    public ColorPickerScreen(Screen parentScreen, Runnable onApply) {
        super(Component.literal("Color Picker"));

        this.parentScreen = parentScreen;
        this.onApply = onApply;
        this.selectedColor = Config.getHighlightColor();
        this.red = (selectedColor >> 16) & 0xFF;
        this.green = (selectedColor >> 8) & 0xFF;
        this.blue = selectedColor & 0xFF;
    }

    @Override
    protected void init() {
        int windowWidth = 240;
        int windowHeight = 180;
        int windowX = (this.width - windowWidth) / 2;
        int windowY = (this.height - windowHeight) / 2;

        this.addRenderableWidget(new ColorSlider(
                windowX + 20, windowY + 70, windowWidth - 40, 20,
                "Red", red, value -> {
            red = value;
            updateColor();
        }
        ));

        this.addRenderableWidget(new ColorSlider(
                windowX + 20, windowY + 95, windowWidth - 40, 20,
                "Green", green, value -> {
            green = value;
            updateColor();
        }
        ));

        this.addRenderableWidget(new ColorSlider(
                windowX + 20, windowY + 120, windowWidth - 40, 20,
                "Blue", blue, value -> {
            blue = value;
            updateColor();
        }
        ));

        this.addRenderableWidget(Button.builder(
                Component.literal("Reset"),
                button -> resetColor()
        ).bounds(windowX + 20, windowY + 145, 80, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Cancel"),
                button -> this.onClose()
        ).bounds(windowX + 110, windowY + 145, 50, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.literal("Apply"),
                button -> applyColor()
        ).bounds(windowX + 170, windowY + 145, 50, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, 0xA0000000);

        int windowWidth = 240;
        int windowHeight = 210;
        int windowX = (this.width - windowWidth) / 2;
        int windowY = (this.height - windowHeight) / 2;

        renderColorPreview(graphics, windowX + 20, windowY, windowWidth - 40, 30);
        renderHexText(graphics, windowX + 20, windowY + 35);

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private void updateColor() {
        selectedColor = (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

    private void resetColor() {
        selectedColor = 0xFFA5D977;
        red = (selectedColor >> 16) & 0xFF;
        green = (selectedColor >> 8) & 0xFF;
        blue = selectedColor & 0xFF;

        updateColor();
        this.clearWidgets();
        this.init();
    }

    private void applyColor() {
        Config.setHighlightColor(selectedColor);
        Config.save();

        if (onApply != null) {
            onApply.run();
        }

        this.onClose();
    }

    private void renderColorPreview(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, selectedColor);

        int lighter = ColorUtils.adjustBrightness(selectedColor, 30);
        int darker = ColorUtils.adjustBrightness(selectedColor, -30);

        graphics.fill(x, y, x + width, y + 1, lighter);
        graphics.fill(x, y, x + 1, y + height, lighter);
        graphics.fill(x, y + height - 1, x + width, y + height, darker);
        graphics.fill(x + width - 1, y, x + width, y + height, darker);
    }

    private void renderHexText(GuiGraphicsExtractor graphics, int x, int y) {
        String hexText = String.format("#%02X%02X%02X", red, green, blue);
        graphics.text(Minecraft.getInstance().font, hexText, x, y, 0xFFFFFFFF, false);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(parentScreen);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (parentScreen != null) {
            parentScreen.resize(width, height);
        }
    }
}