package net.andrewyernau.feel_the_wild.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.andrewyernau.feel_the_wild.entity.animations.ModAnimationDefinitions;

import net.andrewyernau.feel_the_wild.entity.custom.QuetzalEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class QuetzalModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart rightwing;
    private final ModelPart leftwing;
    private final ModelPart leftleg;
    private final ModelPart rightleg;

    public QuetzalModel(ModelPart root) {
        this.torso = root.getChild("torso");
        this.head = torso.getChild("head");
        this.body = torso.getChild("body");
        this.tail = body.getChild("tail");
        this.rightwing = torso.getChild("rightwing");
        this.leftwing = torso.getChild("leftwing");
        this.leftleg = torso.getChild("leftleg");
        this.rightleg = torso.getChild("rightleg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));

        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(13, 4).addBox(-1.0F, -1.35F, -1.1F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-0.5F, -1.0F, -1.9F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
                .texOffs(14, 10).addBox(-0.5F, -0.5F, -2.9F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F))
                .texOffs(5, 3).addBox(0.0F, -2.8F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -0.1F, -2.5F));

        PartDefinition crest3_r1 = head.addOrReplaceChild("crest3_r1", CubeListBuilder.create().texOffs(8, 6).addBox(-0.2F, -2.8F, -2.1F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

        PartDefinition crest2_r1 = head.addOrReplaceChild("crest2_r1", CubeListBuilder.create().texOffs(8, 6).addBox(0.2F, -2.8F, -2.1F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.192F));

        PartDefinition body = torso.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(12, 0).addBox(-1.0F, 3.5F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -2.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 4.05F, 1.5F));

        PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create().texOffs(8, 12).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition rightwing = torso.addOrReplaceChild("rightwing", CubeListBuilder.create().texOffs(0, 7).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 1.5F, -1.0F));

        PartDefinition leftwing = torso.addOrReplaceChild("leftwing", CubeListBuilder.create().texOffs(21, 7).addBox(0.0F, -0.05F, -2.0F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.55F, -1.0F));

        PartDefinition leftleg = torso.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(14, 13).addBox(0.5F, 5.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.5F, -2.0F));

        PartDefinition rightleg = torso.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(14, 13).addBox(-1.5F, 5.5F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.5F, -2.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationDefinitions.QUETZAL_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((QuetzalEntity)entity).idleAnimationState, ModAnimationDefinitions.QUETZAL_IDLE,ageInTicks,1f);
        this.animate(((QuetzalEntity)entity).flyingAnimationState, ModAnimationDefinitions.QUETZAL_FLYFLAPPING,ageInTicks,1f);

    }

    public void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return torso;
    }

}