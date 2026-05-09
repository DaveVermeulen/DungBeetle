package org.fynixx.dungbeetle.server.level.modifiers;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.fynixx.dungbeetle.entity.ModEntities;
import org.fynixx.dungbeetle.registry.DungBeetleBiomeModifiers;
import org.fynixx.dungbeetle.registry.DungBeetleTags;

public class AddAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD)) {
            addIfValid(builder, biome, DungBeetleTags.Biomes.HAS_DUNG_BEETLE, DungBeetleTags.Biomes.BLACKLIST_DUNG_BEETLE, MobCategory.CREATURE, ModEntities.DUNG_BEETLE.get(), 12, 2, 4);
        }
    }

    private void addIfValid(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> hasTag, TagKey<Biome> blacklistTag, MobCategory category, EntityType<?> entityType, int weight, int min, int max) {
        if (biome.is(hasTag) && !biome.is(blacklistTag)) {
            builder.getMobSpawnSettings().addSpawn(category, new MobSpawnSettings.SpawnerData(entityType, weight, min, max));
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return DungBeetleBiomeModifiers.ADD_ANIMALS_CODEC.get();
    }
}
