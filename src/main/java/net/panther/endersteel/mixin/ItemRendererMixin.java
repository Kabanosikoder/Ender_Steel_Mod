package net.panther.endersteel.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode; // <-- keep this per your mappings
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.ModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow @Final private ItemModels models;

    @Unique private static final ThreadLocal<ModelTransformationMode> ENDERSTEEL$MODE = new ThreadLocal<>();

    // Capture the current mode at the start of renderItem
    @Inject(method = "renderItem*", at = @At("HEAD"))
    private void endersteel$captureMode(ItemStack stack,
                                        ModelTransformationMode mode,
                                        boolean leftHanded,
                                        MatrixStack matrices,
                                        VertexConsumerProvider consumers,
                                        int light, int overlay, int seed,
                                        CallbackInfo ci) {
        ENDERSTEEL$MODE.set(mode);
    }

    // Clear it afterwards to avoid leaking between calls
    @Inject(method = "renderItem*", at = @At("RETURN"))
    private void endersteel$clearMode(ItemStack stack,
                                      ModelTransformationMode mode,
                                      boolean leftHanded,
                                      MatrixStack matrices,
                                      VertexConsumerProvider consumers,
                                      int light, int overlay, int seed,
                                      CallbackInfo ci) {
        ENDERSTEEL$MODE.remove();
    }

    // Swap the baked model based on the captured mode
    @Inject(method = "getModel*", at = @At("HEAD"), cancellable = true)
    private void endersteel$swapModel(ItemStack stack, World world, LivingEntity entity, int seed,
                                      CallbackInfoReturnable<BakedModel> cir) {
        if (stack.getItem() != ModItems.VOID_MACE) return;

        ModelTransformationMode mode = ENDERSTEEL$MODE.get();
        boolean use2D = mode == ModelTransformationMode.GUI
                || mode == ModelTransformationMode.GROUND
                || mode == ModelTransformationMode.FIXED;

        Identifier id = use2D
                ? Identifier.of(EnderSteel.MOD_ID, "void_mace")
                : Identifier.of(EnderSteel.MOD_ID, "void_mace_3d");

        cir.setReturnValue(this.models.getModel(id));
    }
}
