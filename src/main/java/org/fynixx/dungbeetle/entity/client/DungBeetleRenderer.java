package org.fynixx.dungbeetle.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DungBeetleRenderer extends GeoEntityRenderer<DungBeetleEntity> {
    public DungBeetleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DungBeetleModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DungBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "textures/entity/dung_beetle/dung_beetle.png");
    }

    @Override
    public void render(DungBeetleEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
