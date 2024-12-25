package net.panther.endersteel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.panther.endersteel.client.EvasionChargesOverlay;
import net.panther.endersteel.client.GazingVoidOverlay;

public class EnderSteelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new GazingVoidOverlay());
        HudRenderCallback.EVENT.register(new EvasionChargesOverlay());
    }
}
