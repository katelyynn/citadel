package moe.katelyn.citadel.net;

import moe.katelyn.citadel.Citadel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DashPayload() implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "dash");
    public static final CustomPacketPayload.Type<DashPayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

    public static final StreamCodec<FriendlyByteBuf, DashPayload> CODEC = StreamCodec.unit(new DashPayload());

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
