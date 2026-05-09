package org.fynixx.dungbeetle.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Dungbeetle.MODID);

    public static final Supplier<EntityType<DungBeetleEntity>> DUNG_BEETLE =
            ENTITY_TYPES.register("dung_beetle", () -> EntityType.Builder.of(DungBeetleEntity::new, MobCategory.CREATURE)
                    .sized(0.65f, 0.45f).build("dung_beetle"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
