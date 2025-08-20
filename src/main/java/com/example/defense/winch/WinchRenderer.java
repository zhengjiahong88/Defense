package com.example.defense.winch;

import com.example.defense.Defense;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WinchRenderer implements BlockEntityRenderer<WinchBlockEntity> {
    private final Winch model;  // 現在可以直接使用 Winch
    private final ResourceLocation texture;

    public WinchRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new Winch(Winch.createBodyLayer().bakeRoot());
        this.texture = ResourceLocation.fromNamespaceAndPath(Defense.MODID, "textures/block/winch.png");
    }

    @Override
    public void render(@NotNull WinchBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(-1, -1, 1);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(texture));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1, 1, 1, 1);

        poseStack.popPose();
    }
}