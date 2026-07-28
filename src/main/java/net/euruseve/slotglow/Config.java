package net.euruseve.slotglow;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public enum HighlightMode {
        DEFAULT,
        CUSTOM,
        NO_DIM
    }

    public static ModConfigSpec.EnumValue<HighlightMode> HIGHLIGHT_MODE = BUILDER
            .comment(
                    "Slot highlight mode:",
                    "DEFAULT - vanilla behavior",
                    "CUSTOM - custom highlight",
                    "NO_DIM - vanilla without the foreground dimming overlay"
            )
            .defineEnum("highlightMode", HighlightMode.CUSTOM);

    public static ModConfigSpec.IntValue HIGHLIGHT_COLOR = BUILDER
            .comment("Color used for the slot highlight")
            .defineInRange("highlightColor", 0xFFA5D977, 0xFF000000, 0xFFFFFFFF);

    public static ModConfigSpec SPEC = BUILDER.build();

    public static HighlightMode getHighlightMode() {
        return HIGHLIGHT_MODE.get();
    }

    public static int getHighlightColor() {
        return HIGHLIGHT_COLOR.get();
    }

    public static void setHighlightMode(HighlightMode mode) {
        HIGHLIGHT_MODE.set(mode);
    }

    public static void setHighlightColor(int color) {
        HIGHLIGHT_COLOR.set(color);
    }

    public static void save() {
        SPEC.save();
    }
}
