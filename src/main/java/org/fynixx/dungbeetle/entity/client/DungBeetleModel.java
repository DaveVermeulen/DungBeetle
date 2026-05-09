package org.fynixx.dungbeetle.entity.client;

import net.minecraft.resources.ResourceLocation;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;
import software.bernie.geckolib.model.GeoModel;

public class DungBeetleModel extends GeoModel<DungBeetleEntity> {
    @Override
    public ResourceLocation getModelResource(DungBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "geo/dung_beetle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DungBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "textures/entity/dung_beetle/dung_beetle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DungBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "animations/dung_beetle.animation.json");
    }
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

}
