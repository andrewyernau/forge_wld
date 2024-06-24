package net.andrewyernau.feel_the_wild;

import com.mojang.logging.LogUtils;
import net.andrewyernau.feel_the_wild.entity.ModEntities;
import net.andrewyernau.feel_the_wild.entity.api.CustomSensors;
import net.andrewyernau.feel_the_wild.entity.client.QuetzalRenderer;
import net.andrewyernau.feel_the_wild.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TheWild.MOD_ID)
public class TheWild {
    public static final String MOD_ID = "feel_the_wild";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TheWild() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        //EntityRegistry.DEF_REG.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);

        CustomSensors.SENSORS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.QUETZAL.get(), QuetzalRenderer::new);
        }
    }
}
