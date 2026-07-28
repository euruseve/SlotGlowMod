package net.euruseve.slotglow.gui.components;

import net.euruseve.slotglow.Config;
import net.euruseve.slotglow.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

public class SlotPreviewWidget extends AbstractWidget {

    private static final int SLOT_SIZE = 18;
    private static final int TEXT_GAP  = 6;
    private static final String LABEL  = "Try it!";

    private final Supplier<Integer>              colorSupplier;
    private final Supplier<Config.HighlightMode> modeSupplier;

    private final ItemStack[] items = {
            new ItemStack(Items.DIAMOND_SWORD),
            new ItemStack(Items.GOLDEN_APPLE),
            ItemStack.EMPTY,
            new ItemStack(Items.OAK_PLANKS),
            new ItemStack(Items.ENDER_PEARL)
    };

    public SlotPreviewWidget(int x, int y,
                             Supplier<Integer> colorSupplier,
                             Supplier<Config.HighlightMode> modeSupplier) {
        super(x, y, SLOT_SIZE * 5, SLOT_SIZE, Component.literal("Preview"));
        this.active        = false;
        this.colorSupplier = colorSupplier;
        this.modeSupplier  = modeSupplier;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics,
                                            int mouseX, int mouseY, float partialTick) {
        int hoveredSlot = getHoveredSlot(mouseX, mouseY);

        for (int i = 0; i < items.length; i++) {
            int slotX = getX() + i * SLOT_SIZE;
            int slotY = getY();
            boolean hovered = (i == hoveredSlot);

            renderSlotBackground(graphics, slotX, slotY);
            if (hovered && modeSupplier.get() != Config.HighlightMode.DEFAULT) {
                renderHighlight(graphics, slotX + 1, slotY + 1);
            }

            if (!items[i].isEmpty()) {
                graphics.fakeItem(items[i], slotX + 1, slotY + 1);
            }
            if (hovered && modeSupplier.get() == Config.HighlightMode.DEFAULT) {
                renderHighlight(graphics, slotX + 1, slotY + 1);
            }
        }

        Font font  = Minecraft.getInstance().font;
        int textX  = getX() + SLOT_SIZE * items.length + TEXT_GAP;
        int textY  = getY() + (SLOT_SIZE - font.lineHeight) / 2;
        graphics.text(font, LABEL, textX, textY, 0xFFAAAAAA, false);
    }

    private int getHoveredSlot(int mouseX, int mouseY) {
        if (mouseY < getY() || mouseY >= getY() + SLOT_SIZE) return -1;
        int relX = mouseX - getX();
        if (relX < 0 || relX >= SLOT_SIZE * items.length) return -1;
        return relX / SLOT_SIZE;
    }

    private void renderSlotBackground(GuiGraphicsExtractor graphics, int x, int y) {
        graphics.fill(x,              y,              x + SLOT_SIZE, y + 1,          0xFF373737);
        graphics.fill(x,              y,              x + 1,         y + SLOT_SIZE,  0xFF373737);
        graphics.fill(x,              y + SLOT_SIZE - 1, x + SLOT_SIZE, y + SLOT_SIZE, 0xFFFFFFFF);
        graphics.fill(x + SLOT_SIZE - 1, y,          x + SLOT_SIZE, y + SLOT_SIZE,  0xFFFFFFFF);
        graphics.fill(x + 1,          y + 1,          x + SLOT_SIZE - 1, y + SLOT_SIZE - 1, 0xFF8B8B8B);
    }

    private void renderHighlight(GuiGraphicsExtractor graphics, int x, int y) {
        Config.HighlightMode mode = modeSupplier.get();

        switch (mode) {
            case DEFAULT, NO_DIM -> graphics.fill(x, y, x + 16, y + 16, 0x80FFFFFF);

            case CUSTOM -> {
                int color = colorSupplier.get();
                int light = ColorUtils.adjustBrightness(color, 60);
                int dark  = ColorUtils.adjustBrightness(color, -60);

                graphics.fill(x,      y,      x + 16, y + 16, color);
                graphics.fill(x - 1,  y - 1,  x + 17, y,      light);
                graphics.fill(x - 1,  y,      x,      y + 17, light);
                graphics.fill(x - 1,  y + 16, x + 17, y + 17, dark);
                graphics.fill(x + 16, y - 1,  x + 17, y + 17, dark);
            }
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX < getX() + getWidth()
                && mouseY >= getY() && mouseY < getY() + getHeight();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}