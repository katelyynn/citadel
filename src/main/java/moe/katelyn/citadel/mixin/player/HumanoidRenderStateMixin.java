package moe.katelyn.citadel.mixin.player;

import moe.katelyn.citadel.accessor.HumanoidRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(HumanoidRenderState.class)
public abstract class HumanoidRenderStateMixin implements HumanoidRenderStateAccessor {
    @Unique
    private UUID citadel_UUID;

    @Override
    public UUID citadel$getPlayerUUID() {
        return citadel_UUID;
    }

    @Override
    public void citadel$setPlayerUUID(UUID uuid) {
        this.citadel_UUID = uuid;
    }
}
