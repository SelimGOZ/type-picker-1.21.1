package net.fortuneblack05.typerando.payloads;

import net.fortuneblack05.typerando.payloads.TypePickerPayloads;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SpinResult(int typeId, int spinTicks) implements CustomPayload {
    public static final Id<SpinResult> ID = new Id<>(TypePickerPayloads.SPIN_RESULT_ID);

    public static final PacketCodec<RegistryByteBuf, SpinResult> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.typeId());
                        buf.writeVarInt(payload.spinTicks());
                    },
                    buf -> new SpinResult(buf.readVarInt(), buf.readVarInt())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}