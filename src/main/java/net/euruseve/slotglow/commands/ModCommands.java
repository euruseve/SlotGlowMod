package net.euruseve.slotglow.commands;

import net.euruseve.slotglow.gui.ConfigScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;

public class ModCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    ClientCommands.literal("slotglow")
                           .executes(ctx -> {
                                Minecraft.getInstance().execute(() -> {
                                Minecraft.getInstance().mouseHandler.releaseMouse();
                                Minecraft.getInstance().setScreen(new ConfigScreen());
                            });
                        return 1;
                    })

            );
        });
    }
}