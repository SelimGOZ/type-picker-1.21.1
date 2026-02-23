package net.fortuneblack05.typerando.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.ArrayList;
import java.util.List;

public record RollSummary(List<String> names, List<Integer> typeIds) implements CustomPayload {
    public static final Id<RollSummary> ID = new Id<>(TypePickerPayloads.ROLL_SUMMARY_ID);

    public static final PacketCodec<RegistryByteBuf, RollSummary> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        int size = payload.names().size();
                        buf.writeVarInt(size);
                        for (int i = 0; i < size; i++) {
                            buf.writeString(payload.names().get(i));
                            buf.writeVarInt(payload.typeIds().get(i));
                        }
                    },
                    buf -> {
                        int size = buf.readVarInt();
                        List<String> names = new ArrayList<>(size);
                        List<Integer> ids = new ArrayList<>(size);
                        for (int i = 0; i < size; i++) {
                            names.add(buf.readString());
                            ids.add(buf.readVarInt());
                        }
                        return new RollSummary(names, ids);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}