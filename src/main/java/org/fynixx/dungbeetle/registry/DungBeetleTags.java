package org.fynixx.dungbeetle.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.fynixx.dungbeetle.Dungbeetle;
import org.jetbrains.annotations.NotNull;

public class DungBeetleTags {
    public static class Biomes {
        public static final TagKey<Biome> HAS_DUNG_BEETLE = tag("has_dung_beetle");
        public static final TagKey<Biome> BLACKLIST_DUNG_BEETLE = tag("blacklist/blacklist_dung_beetle");

        private static @NotNull TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, name));
        }
    }
}
