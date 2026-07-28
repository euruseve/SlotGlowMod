package net.euruseve.slotglow.gui.components;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class ColorSlider extends AbstractSliderButton {

    private final ValueCallback callback;

    public ColorSlider(int x, int y, int width, int height,
                       String label, int initialValue, ValueCallback callback) {
        super(x, y, width, height, Component.empty(), initialValue / 255.0);
        this.callback = callback;
    }

    @Override
    protected void updateMessage() {
    }

    @Override
    protected void applyValue() {
        callback.onValueChanged(getIntValue());
    }

    public int getIntValue() {
        return (int) (this.value * 255);
    }

    public interface ValueCallback {
        void onValueChanged(int value);
    }
}