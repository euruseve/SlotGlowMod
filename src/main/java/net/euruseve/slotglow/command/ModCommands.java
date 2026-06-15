package net.euruseve.slotglow.command;

import com.mojang.brigadier.CommandDispatcher;
import net.euruseve.slotglow.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("slotglow")
                        .executes(ctx -> {
                            Minecraft.getInstance().execute(() -> {
                                Minecraft.getInstance().setScreen(new ConfigScreen());
                            });
                            return 1;
                        })
        );
    }
}
