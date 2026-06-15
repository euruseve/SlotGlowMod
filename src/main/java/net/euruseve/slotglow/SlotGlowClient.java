package net.euruseve.slotglow;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SlotGlow.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SlotGlow.MODID, value = Dist.CLIENT)
public class SlotGlowClient {
    public SlotGlowClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> new net.euruseve.slotglow.gui.ConfigScreen());
    }
}
