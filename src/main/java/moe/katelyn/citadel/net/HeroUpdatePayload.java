package moe.katelyn.citadel.net;

import moe.katelyn.citadel.Citadel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record HeroUpdatePayload(Map<UUID, String> heroes) implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "hero_update");
    public static final CustomPacketPayload.Type<HeroUpdatePayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

    public static final StreamCodec<FriendlyByteBuf, HeroUpdatePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), ByteBufCodecs.STRING_UTF8),
            HeroUpdatePayload::heroes,
            HeroUpdatePayload::new
    );

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
