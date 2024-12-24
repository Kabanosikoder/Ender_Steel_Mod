package net.panther.endersteel;

import net.fabricmc.api.ClientModInitializer;
import net.panther.endersteel.client.GazingVoidOverlay;

public class EnderSteelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GazingVoidOverlay.register();
    }
}
