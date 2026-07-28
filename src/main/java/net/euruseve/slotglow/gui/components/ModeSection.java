package net.euruseve.slotglow.gui.components;

import net.euruseve.slotglow.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;

public class ModeSection extends AbstractWidget {

    private static final int LABEL_COLOR = 0xFFCCCCCC;

    private Config.HighlightMode mode;
    private final Consumer<Config.HighlightMode> onModeChanged;

    private Button modeButton;

    public ModeSection(int x, int y, int width, int height,
                       Config.HighlightMode initialMode,
                       Consumer<Config.HighlightMode> onModeChanged) {
        super(x, y, width, height, Component.empty());
        this.active        = false;
        this.mode          = initialMode;
        this.onModeChanged = onModeChanged;
    }

    public void init(Consumer<AbstractWidget> register) {
        int buttonY = getY() + 9 + 4;

        modeButton = Button.builder(
                Component.literal(modeLabel(mode)),
                btn -> cycleMode()
        ).bounds(getX(), buttonY, getWidth(), 20).build();

        register.accept(modeButton);
    }

    private void cycleMode() {
        mode = switch (mode) {
            case DEFAULT -> Config.HighlightMode.CUSTOM;
            case CUSTOM  -> Config.HighlightMode.NO_DIM;
            case NO_DIM  -> Config.HighlightMode.DEFAULT;
        };
        modeButton.setMessage(Component.literal(modeLabel(mode)));
        onModeChanged.accept(mode);
    }

    public Config.HighlightMode getMode() {
        return mode;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics,
                                            int mouseX, int mouseY, float partialTick) {
        graphics.text(Minecraft.getInstance().font, "Mode",
                getX(), getY(), LABEL_COLOR, false);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) { return false; }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}

    private static String modeLabel(Config.HighlightMode m) {
        return switch (m) {
            case DEFAULT -> "Default";
            case CUSTOM  -> "Custom";
            case NO_DIM  -> "No Dim";
        };
    }
}