package net.fortuneblack05.typerando.Server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fortuneblack05.typerando.payloads.RollSummary;
import net.fortuneblack05.typerando.payloads.SpinResult;

public class TypePickerServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        PayloadTypeRegistry.playS2C().register(SpinResult.ID, SpinResult.CODEC);
        PayloadTypeRegistry.playS2C().register(RollSummary.ID, RollSummary.CODEC);
        System.out.println("[TypeRando] Server networking ready.");
    }
}