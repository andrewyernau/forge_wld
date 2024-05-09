package net.andrewyernau.wildlifemod.entity.client;// Made with Blockbench 4.9.4

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class QuetzalModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart quetzal;
	private final  ModelPart head;

	public QuetzalModel(ModelPart root) {
		this.quetzal = root.getChild("torso");
		this.head= quetzal.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition torso = partdefinition.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose
				.offset(0.0F, 16.0F, 0.0F));

		PartDefinition back = torso.addOrReplaceChild("back", CubeListBuilder.create(), PartPose
				.offset(0.0F, 8.0F, 0.0F));

		PartDefinition tail = back.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose
				.offsetAndRotation(0.0F, -2.9F, 1.2F, 0.8727F, 0.0F, 0.0F));

		PartDefinition tail_r1 = tail.addOrReplaceChild("tail_r1", CubeListBuilder.create()
				.texOffs(8, 12).addBox(-1.5F, -3.4F, 1.2F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.9F, -1.2F, 0.2269F, 0.0F, 0.0F));

		PartDefinition lowetorso = back.addOrReplaceChild("lowetorso", CubeListBuilder.create()
				.texOffs(12, 0).addBox(-1.5F, -1.25F, -1.7F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.25F, 0.0F));

		PartDefinition leg1 = back.addOrReplaceChild("leg1", CubeListBuilder.create()
				.texOffs(14, 13).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.5F, -0.5F));

		PartDefinition leg0 = back.addOrReplaceChild("leg0", CubeListBuilder.create()
				.texOffs(14, 13).addBox(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -1.5F, -0.5F));

		PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(13, 4).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-0.5F, -1.0F, -1.9F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.2F))
		.texOffs(14, 10).addBox(-0.5F, -0.5F, -2.9F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(5, 3).addBox(0.0F, -2.8F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.1F, -2.8F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create()
				.texOffs(8, 6).addBox(-0.2F, -2.8F, -2.1F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1396F));

		PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create()
				.texOffs(8, 6).addBox(0.2F, -2.8F, -2.1F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.192F));

		PartDefinition wing1 = torso.addOrReplaceChild("wing1", CubeListBuilder.create()
				.texOffs(0, 7).addBox(-0.5F, 0.9F, -1.5F, 1.0F, 6.1F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.9F, -2.8F, -0.1745F, 3.1416F, 0.0F));

		PartDefinition wing0 = torso.addOrReplaceChild("wing0", CubeListBuilder.create()
				.texOffs(0, 7).addBox(-0.5F, 0.9F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.9F, -2.8F, -0.1745F, 3.1416F, 0.0F));

		PartDefinition body = torso.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.5F, 1.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -3.0F, 0.4363F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		quetzal.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return quetzal;
	}
}