package moe.katelyn.citadel.hud;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;

public class FakePlayer extends AbstractClientPlayer {
    public FakePlayer(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }
}
