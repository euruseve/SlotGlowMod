package net.euruseve.slotglow.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;

public class ColorSection extends AbstractWidget {

    private static final int COLOR_SQ       = 32;
    private static final int HEX_W          = 76;
    private static final int NUM_W          = 28;
    private static final int SLIDER_H       = 14;
    private static final int ROW_GAP        = 8;

    private static final int LABEL_COLOR    = 0xFFCCCCCC;
    private static final int DISABLED_COLOR = 0xFF555555;
    private static final int[] DOT_COLORS   = { 0xFFFF5555, 0xFF55FF55, 0xFF5599FF };

    private int red, green, blue;
    private int selectedColor;
    private boolean canEdit;

    private final Consumer<Integer> onColorChanged;

    private EditBox     hexInput;
    private ColorSlider redSlider, greenSlider, blueSlider;
    private boolean     updatingFromSlider;

    private int redRowY, greenRowY, blueRowY;
    private int sqX, sqY;

    public ColorSection(int x, int y, int width, int height,
                        int initialColor, boolean canEdit,
                        Consumer<Integer> onColorChanged) {
        super(x, y, width, height, Component.empty());
        this.active         = false;
        this.selectedColor  = initialColor;
        this.red            = (initialColor >> 16) & 0xFF;
        this.green          = (initialColor >> 8)  & 0xFF;
        this.blue           =  initialColor        & 0xFF;
        this.canEdit        = canEdit;
        this.onColorChanged = onColorChanged;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        if (hexInput   != null) hexInput.active   = canEdit;
        if (redSlider   != null) redSlider.active   = canEdit;
        if (greenSlider != null) greenSlider.active = canEdit;
        if (blueSlider  != null) blueSlider.active  = canEdit;
    }

    public int getSelectedColor() { return selectedColor; }

    public void init(Consumer<AbstractWidget> register) {
        int labelH   = 9;
        int gap      = 4;
        int controlY = getY() + labelH + gap;

        sqX = getX();
        sqY = controlY;

        int hexX = sqX + COLOR_SQ + 8;
        hexInput = new EditBox(
                Minecraft.getInstance().font,
                hexX, controlY, HEX_W, 20,
                Component.literal("Hex")
        );
        hexInput.setMaxLength(7);
        hexInput.setValue(colorToHex(selectedColor));
        hexInput.setResponder(this::onHexChanged);
        hexInput.active = canEdit;
        register.accept(hexInput);

        // sliders
        int sliderX      = getX() + 12 + 40;
        int sliderRight  = getX() + getWidth() - NUM_W - 6;
        int sW           = sliderRight - sliderX;
        int sliderStartY = controlY + 20 + 20;

        redRowY = sliderStartY;
        redSlider = new ColorSlider(sliderX, redRowY, sW, SLIDER_H,
                "Red", red, v -> { red = v; updateColor(); });
        redSlider.active = canEdit;
        register.accept(redSlider);

        greenRowY = redRowY + SLIDER_H + ROW_GAP;
        greenSlider = new ColorSlider(sliderX, greenRowY, sW, SLIDER_H,
                "Green", green, v -> { green = v; updateColor(); });
        greenSlider.active = canEdit;
        register.accept(greenSlider);

        blueRowY = greenRowY + SLIDER_H + ROW_GAP;
        blueSlider = new ColorSlider(sliderX, blueRowY, sW, SLIDER_H,
                "Blue", blue, v -> { blue = v; updateColor(); });
        blueSlider.active = canEdit;
        register.accept(blueSlider);
    }

    private void onHexChanged(String text) {
        if (updatingFromSlider) return;
        String hex = text.startsWith("#") ? text.substring(1) : text;
        if (hex.length() != 6) return;
        try {
            int color = Integer.parseUnsignedInt(hex, 16);
            red   = (color >> 16) & 0xFF;
            green = (color >> 8)  & 0xFF;
            blue  =  color        & 0xFF;
            selectedColor = (0xFF << 24) | color;
            onColorChanged.accept(selectedColor);
        } catch (NumberFormatException ignored) {}
    }

    private void updateColor() {
        selectedColor = (0xFF << 24) | (red << 16) | (green << 8) | blue;
        updatingFromSlider = true;
        if (hexInput != null) hexInput.setValue(colorToHex(selectedColor));
        updatingFromSlider = false;
        onColorChanged.accept(selectedColor);
    }

    public void reset(int defaultColor) {
        selectedColor = defaultColor;
        red   = (selectedColor >> 16) & 0xFF;
        green = (selectedColor >> 8)  & 0xFF;
        blue  =  selectedColor        & 0xFF;
        if (hexInput    != null) hexInput.setValue(colorToHex(selectedColor));
        if (redSlider   != null) { }
        onColorChanged.accept(selectedColor);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics,
                                            int mouseX, int mouseY, float partialTick) {
        // "Color" label
        graphics.text(Minecraft.getInstance().font, "Color",
                getX(), getY(),
                canEdit ? LABEL_COLOR : DISABLED_COLOR, false);

        // color preview square
        int drawColor = canEdit ? selectedColor : 0xFF444444;
        graphics.fill(sqX, sqY, sqX + COLOR_SQ, sqY + COLOR_SQ, drawColor);
        graphics.fill(sqX - 1, sqY - 1, sqX + COLOR_SQ + 1, sqY,                       0xFF222222);
        graphics.fill(sqX - 1, sqY - 1, sqX,                  sqY + COLOR_SQ + 1,       0xFF222222);
        graphics.fill(sqX - 1, sqY + COLOR_SQ, sqX + COLOR_SQ + 1, sqY + COLOR_SQ + 1, 0xFF888888);
        graphics.fill(sqX + COLOR_SQ, sqY - 1, sqX + COLOR_SQ + 1, sqY + COLOR_SQ + 1, 0xFF888888);

        // slider dots + labels + numeric values
        String[] names  = { "Red", "Green", "Blue" };
        int[]    values = { red, green, blue };
        int[]    rows   = { redRowY, greenRowY, blueRowY };
        int dotX  = getX();
        int nameX = getX() + 12;
        int numX  = getX() + getWidth() - NUM_W;
        var font  = Minecraft.getInstance().font;

        for (int i = 0; i < 3; i++) {
            int dotColor  = canEdit ? DOT_COLORS[i] : DISABLED_COLOR;
            int textColor = canEdit ? LABEL_COLOR    : DISABLED_COLOR;
            int ry = rows[i];

            graphics.fill(dotX, ry + 4, dotX + 6, ry + 10, dotColor);
            graphics.text(font, names[i], nameX, ry + 3, textColor, false);

            String valStr = String.valueOf(values[i]);
            int valW = font.width(valStr);
            graphics.text(font, valStr, numX + (NUM_W - valW), ry + 3, textColor, false);
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) { return false; }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}

    private static String colorToHex(int argb) {
        return String.format("#%02X%02X%02X",
                (argb >> 16) & 0xFF,
                (argb >> 8)  & 0xFF,
                argb        & 0xFF);
    }
}