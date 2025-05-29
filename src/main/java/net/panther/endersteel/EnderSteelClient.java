package net.panther.endersteel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.client.EvasionChargesOverlay;
import net.panther.endersteel.client.GazingVoidOverlay;

public class EnderSteelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register HUD overlays
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
        HudRenderCallback.EVENT.register(new EvasionChargesOverlay());
        
        // Register model predicate for the Void Mace
        ModelPredicateProviderRegistry.register(
            Identifier.of("endersteel", "void_mace"),
            (stack, world, entity, seed) -> {
                // Model predicates here if needed
                return 0.0F;
            }
        );
    }
}
