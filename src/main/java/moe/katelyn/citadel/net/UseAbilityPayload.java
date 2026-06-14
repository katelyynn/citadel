package moe.katelyn.citadel.net;

import moe.katelyn.citadel.Citadel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record UseAbilityPayload(int index) implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "use_ability");
    public static final CustomPacketPayload.Type<UseAbilityPayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

    public static final StreamCodec<FriendlyByteBuf, UseAbilityPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UseAbilityPayload::index,
            UseAbilityPayload::new
    );

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
