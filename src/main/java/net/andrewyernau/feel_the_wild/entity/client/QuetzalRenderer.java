package net.andrewyernau.feel_the_wild.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.andrewyernau.feel_the_wild.TheWild;
import net.andrewyernau.feel_the_wild.entity.custom.QuetzalEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class QuetzalRenderer extends MobRenderer<QuetzalEntity, QuetzalModel<QuetzalEntity>> {

    public QuetzalRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new QuetzalModel<>(pContext.bakeLayer(ModModelLayers.QUETZAL_LAYER)), 0.25f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull QuetzalEntity quetzalEntity) {
        return new ResourceLocation(TheWild.MOD_ID, "textures/entity/quetzal.png");
    }

    @Override
    public void render(QuetzalEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack,
                       @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}