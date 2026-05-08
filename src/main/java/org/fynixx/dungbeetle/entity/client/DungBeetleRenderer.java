package org.fynixx.dungbeetle.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;

public class DungBeetleRenderer extends MobRenderer<DungBeetleEntity, DungBeetleModel<DungBeetleEntity>> {
    public DungBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new DungBeetleModel<>(context.bakeLayer(DungBeetleModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(DungBeetleEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "textures/entity/dung_beetle/dung_beetle.png");
    }

    @Override
    public void render(DungBeetleEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f,0.45f,0.45f);
        } else {
            poseStack.scale(1f,1f,1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
