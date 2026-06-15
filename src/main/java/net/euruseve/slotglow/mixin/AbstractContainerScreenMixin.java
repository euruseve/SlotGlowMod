package net.euruseve.slotglow.mixin;

import net.euruseve.slotglow.Config;
import net.euruseve.slotglow.util.ColorUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow
    protected Slot hoveredSlot;

    @Inject(method = "extractSlotHighlightBack", at = @At("HEAD"), cancellable = true)
    private void slotglow$drawCustomHighlight(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        Config.HighlightMode mode = Config.HIGHLIGHT_MODE.get();

        if (mode == Config.HighlightMode.DEFAULT ||
            mode == Config.HighlightMode.NO_DIM)
        {
            return;
        }

        if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable()) {
            int x = this.hoveredSlot.x;
            int y = this.hoveredSlot.y;
            int color = Config.HIGHLIGHT_COLOR.get();

            int lightColor = ColorUtils.adjustBrightness(color, 60);
            int darkColor = ColorUtils.adjustBrightness(color, -60);

            graphics.fill(x, y, x + 16, y + 16, color);

            graphics.fill(x - 1, y - 1, x + 17, y, lightColor);
            graphics.fill(x - 1, y, x, y + 17, lightColor);

            graphics.fill(x - 1, y + 16, x + 17, y + 17, darkColor);
            graphics.fill(x + 16, y - 1, x + 17, y + 17, darkColor);
        }
        ci.cancel();
    }

    @Inject(method = "extractSlotHighlightFront", at = @At("HEAD"), cancellable = true)
    private void slotglow$cancelVanillaFront(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        Config.HighlightMode mode = Config.HIGHLIGHT_MODE.get();

        if (mode == Config.HighlightMode.DEFAULT) {
            return;
        }
        ci.cancel();
    }
}