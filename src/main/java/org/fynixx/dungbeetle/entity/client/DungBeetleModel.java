package org.fynixx.dungbeetle.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.fynixx.dungbeetle.Dungbeetle;
import org.fynixx.dungbeetle.entity.custom.DungBeetleEntity;

public class DungBeetleModel<T extends DungBeetleEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Dungbeetle.MODID, "dung_beetle"), "main");
    private final ModelPart Body;

    public DungBeetleModel(ModelPart root) {
        this.Body = root.getChild("Body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Dung = Body.addOrReplaceChild("Dung", CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Beetle = Body.addOrReplaceChild("Beetle", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 4.0F));

        PartDefinition cube_r1 = Beetle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, -2.0F, -8.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, 0.0F, -10.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(20, 22).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, -1.0036F, 0.0F, 0.0F));

        PartDefinition cube_r2 = Beetle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -10.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 3.0F, 1.0036F, 0.0F, -3.1416F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(DungBeetleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(DungBeetleAnimations.ANIM_DUNG_BEETLE_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, DungBeetleAnimations.ANIM_DUNG_BEETLE_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return Body;
    }
}
