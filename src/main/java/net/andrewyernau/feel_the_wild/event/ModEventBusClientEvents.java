package net.andrewyernau.feel_the_wild.event;

import net.andrewyernau.feel_the_wild.TheWild;
import net.andrewyernau.feel_the_wild.entity.client.ModModelLayers;
import net.andrewyernau.feel_the_wild.entity.client.QuetzalModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheWild.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.QUETZAL_LAYER, QuetzalModel::createBodyLayer);
    }
}
