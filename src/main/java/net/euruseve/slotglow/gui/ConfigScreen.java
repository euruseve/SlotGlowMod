package net.euruseve.slotglow.gui;

import net.euruseve.slotglow.config.Config;
import net.euruseve.slotglow.gui.buttons.ColorPreviewButton;
import net.euruseve.slotglow.gui.buttons.TitledLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private final int HEADER_HEIGHT = 25;
    private final int HEADER_COLOR = 0xCE171010;
    private final int HEADER_BORDER_COLOR = 0xCE2B2B2B;

    private Button modeButton;
    private Button colorButton;

    public ConfigScreen() {
        super(Component.literal("SlotGlow Config"));
    }

    @Override
    protected void init() {
        super.init();
        initButtons();
    }

    private void initButtons() {
        int buttonWidth = 300;
        int labelWidth = buttonWidth * 2 / 3;
        int buttonX = (this.width - buttonWidth) / 2;

        addRenderableWidget(new TitledLabel(
                buttonX, 40, labelWidth, 20,
                Component.literal("Highlight Mode")
        ));

        modeButton = Button.builder(
                Component.literal(modeLabel(Config.getHighlightMode())),
                button -> cycleMode()
        ).bounds(buttonX + labelWidth, 40, buttonWidth - labelWidth, 20).build();

        addRenderableWidget(modeButton);

        addRenderableWidget(new TitledLabel(
                buttonX, 70, labelWidth, 20,
                Component.literal("Highlighting Color")
        ));

        colorButton = ColorPreviewButton.create(
                buttonX + labelWidth, 70, buttonWidth - labelWidth, 20,
                button -> Minecraft.getInstance().setScreen(
                        new ColorPickerScreen(this, () -> ColorPreviewButton.refresh(colorButton))
                )
        );
        colorButton.active = Config.getHighlightMode() == Config.HighlightMode.CUSTOM;

        addRenderableWidget(colorButton);

        int backWidth = 100;
        int backHeight = 20;
        int backX = (this.width - backWidth) / 2;
        int backY = this.height - HEADER_HEIGHT + (HEADER_HEIGHT - backHeight) / 2;

        addRenderableWidget(Button.builder(
                Component.literal("Back"),
                button -> this.onClose()
        ).bounds(backX, backY, backWidth, backHeight).build());
    }

    private void cycleMode() {
        Config.HighlightMode current = Config.getHighlightMode();
        Config.HighlightMode next = switch (current) {
            case DEFAULT -> Config.HighlightMode.CUSTOM;
            case CUSTOM -> Config.HighlightMode.NO_DIM;
            case NO_DIM -> Config.HighlightMode.DEFAULT;
        };

        Config.setHighlightMode(next);
        Config.save();

        modeButton.setMessage(Component.literal(modeLabel(next)));
        colorButton.active = next == Config.HighlightMode.CUSTOM;
    }

    private static String modeLabel(Config.HighlightMode mode) {
        return switch (mode) {
            case DEFAULT -> "Default";
            case CUSTOM -> "Custom";
            case NO_DIM -> "Default (No Dim)";
        };
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        renderHeaderBg(graphics);
        renderBg(graphics);
        renderFooterBg(graphics);

        Component title = Component.literal("SlotGlow Config");
        int textWidth = this.font.width(title);
        int x = (this.width - textWidth) / 2;
        int y = (HEADER_HEIGHT - 9) / 2;
        graphics.text(this.font, title, x, y, 0xFFFFFFFF, false);

        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private void renderHeaderBg(GuiGraphicsExtractor graphics) {
        graphics.fill(0, 0, this.width, HEADER_HEIGHT, HEADER_COLOR);
        graphics.fill(0, HEADER_HEIGHT - 1, this.width, HEADER_HEIGHT, HEADER_BORDER_COLOR);
    }

    private void renderFooterBg(GuiGraphicsExtractor graphics) {
        int y = this.height - HEADER_HEIGHT;
        graphics.fill(0, y, this.width, y + HEADER_HEIGHT, HEADER_COLOR);
        graphics.fill(0, y, this.width, y + 1, HEADER_BORDER_COLOR);
    }

    private void renderBg(GuiGraphicsExtractor graphics) {
        final int color = 0x882B2B2B;
        graphics.fill(0, HEADER_HEIGHT, this.width, this.height - HEADER_HEIGHT, color);
    }
}