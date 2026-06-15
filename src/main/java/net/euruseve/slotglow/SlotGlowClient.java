package net.euruseve.slotglow;

import net.euruseve.slotglow.commands.ModCommands;
import net.euruseve.slotglow.config.Config;
import net.fabricmc.api.ClientModInitializer;

public class SlotGlowClient implements ClientModInitializer {
    public static final String MOD_ID = "slotglow";

    @Override
    public void onInitializeClient() {
        Config.load();
        ModCommands.register();
    }
}