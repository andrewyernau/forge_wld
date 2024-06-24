package net.andrewyernau.feel_the_wild.sound;

import net.andrewyernau.feel_the_wild.TheWild;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TheWild.MOD_ID);

    public static final RegistryObject<SoundEvent> QUETZAL_IDLE = registerSoundEvents("quetzal_sound_1");
//    with the use of comma "," and adding new sounds in the sounds.json , the game will choose one randomly

    //    public static final ForgeSoundType SOUND_QUETZAL= new ForgeSoundType(1f,1f,
//            ModSounds.QUETZAL_IDLE,);
//    Thats for blocks where you need to add break, step,fall,place,hit sounds to that block
    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(TheWild.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
