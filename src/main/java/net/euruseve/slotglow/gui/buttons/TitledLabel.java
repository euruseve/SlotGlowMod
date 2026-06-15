package net.euruseve.slotglow.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class TitledLabel extends AbstractWidget {

    public TitledLabel(int x, int y, int width, int height, Component text) {
        super(x, y, width, height, text);
        this.active = false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.text(Minecraft.getInstance().font, this.getMessage(),
                this.getX() + 5,
                this.getY() + (this.getHeight() - 8) / 2,
                0xFFFFFFFF,
                false);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}