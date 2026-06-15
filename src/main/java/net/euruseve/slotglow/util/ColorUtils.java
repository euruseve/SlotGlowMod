package net.euruseve.slotglow.util;

public class ColorUtils {

    private ColorUtils() {
    }

    public static int adjustBrightness(int argb, int delta) {
        int a = (argb >> 24) & 0xFF;
        int r = clamp(((argb >> 16) & 0xFF) + delta);
        int g = clamp(((argb >> 8) & 0xFF) + delta);
        int b = clamp((argb & 0xFF) + delta);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}