package net.andrewyernau.wildlifemod.event;

import net.andrewyernau.wildlifemod.WildLifeDelights;
import net.andrewyernau.wildlifemod.entity.ModEntities;
import net.andrewyernau.wildlifemod.entity.custom.QuetzalEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WildLifeDelights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.QUETZAL.get(), QuetzalEntity.createAttributes().build());
    }
}
