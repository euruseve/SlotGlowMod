package net.euruseve.slotglow.gui;

import net.euruseve.slotglow.Config;
import net.euruseve.slotglow.gui.components.SlotPreviewWidget;
import net.euruseve.slotglow.gui.components.ColorSection;
import net.euruseve.slotglow.gui.components.HeaderWidget;
import net.euruseve.slotglow.gui.components.ModeSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

    private static final int PANEL_W       = 340;
    private static final int PAD           = 20;
    private static final int DIVIDER_COLOR = 0xFF3A3A3A;
    private static final int BG_COLOR      = 0xC0101010;

    private int panelX, startY, dividerY;

    private HeaderWidget     header;
    private SlotPreviewWidget preview;
    private ModeSection      modeSection;
    private ColorSection     colorSection;


    public ConfigScreen() {
        super(Component.literal("SlotGlow Config"));
    }

    @Override
    protected void init() {
        super.init();

        panelX = (this.width - PANEL_W) / 2;

        int totalH = 18 + 10 + 1 + 10       // header row + gap + divider + gap
                + 9 + 4 + 20 + 20           // color+mode labels + controls + gap
                + (14 + 8) * 3              // 3 sliders
                + 10 + 20;                  // footer gap + buttons

        startY = Math.max(16, (this.height - totalH) / 2);

        int y = startY;

        int previewW = 18 * 5;
        int headerW  = PANEL_W - PAD * 2 - previewW - 8;

        header = new HeaderWidget(panelX + PAD, y, headerW, 18);
        addRenderableWidget(header);

        preview = new SlotPreviewWidget(
                panelX + PANEL_W - PAD - previewW, y,
                () -> colorSection != null ? colorSection.getSelectedColor() : Config.getHighlightColor(),
                () -> modeSection  != null ? modeSection.getMode()           : Config.getHighlightMode()
        );
        addRenderableWidget(preview);
        y += 18 + 10;

        dividerY = y;
        y += 1 + 10;
        int halfW = (PANEL_W - PAD * 2) / 2;

        modeSection = new ModeSection(
                panelX + PAD + halfW + 8, y, halfW - 8, 9 + 4 + 20,
                Config.getHighlightMode(),
                newMode -> {
                    colorSection.setCanEdit(newMode == Config.HighlightMode.CUSTOM);
                }
        );
        addRenderableWidget(modeSection);
        modeSection.init(this::addRenderableWidget);

        colorSection = new ColorSection(
                panelX + PAD, y, halfW, 9 + 4 + 20 + 20 + (14 + 8) * 3,
                Config.getHighlightColor(),
                Config.getHighlightMode() == Config.HighlightMode.CUSTOM,
                newColor -> {}
        );
        addRenderableWidget(colorSection);
        colorSection.init(this::addRenderableWidget);

        int btnW    = 90;
        int totalBW = btnW * 3 + 8 * 2;
        int btnX    = (this.width - totalBW) / 2;
        int btnY    = this.height - 36;

        addRenderableWidget(Button.builder(
                Component.literal("Reset"),
                btn -> {
                    clearWidgets();
                    Config.setHighlightColor(0xFFA5D977);
                    init();
                }
        ).bounds(btnX, btnY, btnW, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Cancel"),
                btn -> this.onClose()
        ).bounds(btnX + btnW + 8, btnY, btnW, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Apply"),
                btn -> applyAndClose()
        ).bounds(btnX + (btnW + 8) * 2, btnY, btnW, 20).build());
    }

    private void applyAndClose() {
        Config.setHighlightMode(modeSection.getMode());
        Config.setHighlightColor(colorSection.getSelectedColor());
        Config.save();
        this.onClose();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics,
                                   int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, BG_COLOR);
        graphics.fill(panelX + PAD, dividerY,
                panelX + PANEL_W - PAD, dividerY + 1,
                DIVIDER_COLOR);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null);
    }
}