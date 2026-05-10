package org.fynixx.dungbeetle.registry;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.fynixx.dungbeetle.DungBeetle;
import org.fynixx.dungbeetle.server.level.modifiers.AddAnimalsBiomeModifier;

public class DungBeetleBiomeModifiers {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, DungBeetle.MODID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<AddAnimalsBiomeModifier>> ADD_ANIMALS_CODEC =
            BIOME_MODIFIERS.register("add_animals", () -> MapCodec.unit(AddAnimalsBiomeModifier::new));
}
