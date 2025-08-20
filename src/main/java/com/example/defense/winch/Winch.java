package com.example.defense.winch;

import com.example.defense.Defense;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Winch extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Defense.MODID, "winch"), "main");
	private final ModelPart roller;
	private final ModelPart axis;

	public Winch(ModelPart root) {
		super(RenderType::entityCutout);
		this.roller = root.getChild("roller");
		this.axis = root.getChild("axis");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition roller = partdefinition.addOrReplaceChild("roller", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 2.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(6.0F, -16.0F, -8.0F, 2.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition axis = partdefinition.addOrReplaceChild("axis", CubeListBuilder.create().texOffs(18, 0).addBox(-6.0F, -9.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		roller.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		axis.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}