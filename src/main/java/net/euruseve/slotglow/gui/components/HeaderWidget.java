package net.euruseve.slotglow.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class HeaderWidget extends AbstractWidget {

    private static final int TEXT_COLOR     = 0xFFFFFFFF;
    private static final int SUBTITLE_COLOR = 0xFF888888;

    private static final String TITLE    = "Slot Glow";
    private static final String SUBTITLE = "Highlight color and mode settings";

    public HeaderWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(TITLE));
        this.active = false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics,
                                            int mouseX, int mouseY, float partialTick) {
        int totalTextH = 9 + 3 + 9;
        int titleY     = getY() + (getHeight() - totalTextH) / 2;

        graphics.text(Minecraft.getInstance().font, TITLE,
                getX(), titleY, TEXT_COLOR, false);
        graphics.text(Minecraft.getInstance().font, SUBTITLE,
                getX(), titleY + 9 + 3, SUBTITLE_COLOR, false);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) { return false; }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}