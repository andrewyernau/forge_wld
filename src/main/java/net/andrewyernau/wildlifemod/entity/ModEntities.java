package net.andrewyernau.wildlifemod.entity;

import net.andrewyernau.wildlifemod.WildLifeDelights;
import net.andrewyernau.wildlifemod.entity.custom.QuetzalEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES=
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WildLifeDelights.MOD_ID);

    public static final RegistryObject<EntityType<QuetzalEntity>> QUETZAL=
            ENTITY_TYPES.register("quetzal",()->EntityType.Builder.of(QuetzalEntity::new, MobCategory.CREATURE)
                    .sized(0.5f,0.5f).build("quetzal"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
