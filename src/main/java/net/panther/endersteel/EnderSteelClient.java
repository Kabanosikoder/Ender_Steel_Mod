package net.panther.endersteel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.panther.endersteel.client.EvasionChargesOverlay;
import net.panther.endersteel.client.GazingVoidOverlay;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public class EnderSteelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
        HudRenderCallback.EVENT.register(new EvasionChargesOverlay());

            ModelLoadingPlugin.register(ctx -> {
                ctx.addModels();
                ModelIdentifier.ofInventoryVariant(Identifier.of("endersteel", "void_mace"));
                ModelIdentifier.ofInventoryVariant(Identifier.of("endersteel", "void_mace_3d"));
            });

    }
}
