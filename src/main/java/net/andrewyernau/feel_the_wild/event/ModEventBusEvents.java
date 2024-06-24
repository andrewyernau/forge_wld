package net.andrewyernau.feel_the_wild.event;

import net.andrewyernau.feel_the_wild.TheWild;
import net.andrewyernau.feel_the_wild.entity.ModEntities;
import net.andrewyernau.feel_the_wild.entity.custom.QuetzalEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheWild.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.QUETZAL.get(), QuetzalEntity.createAttributes().build());
    }
}
