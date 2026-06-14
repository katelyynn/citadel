package moe.katelyn.citadel.net;

import moe.katelyn.citadel.Citadel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record DoubleJumpPayload() implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "double_jump");
    public static final Type<DoubleJumpPayload> TYPE = new Type<>(IDENTIFIER);

    public static final StreamCodec<FriendlyByteBuf, DoubleJumpPayload> CODEC = StreamCodec.unit(new DoubleJumpPayload());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
