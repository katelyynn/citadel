package moe.katelyn.citadel.mixin.player;

import moe.katelyn.citadel.accessor.HumanoidRenderStateAccessor;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Inject(method = "extractRenderState*", at = @At("RETURN"))
    private void onExtractRenderState(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        ((HumanoidRenderStateAccessor) state).citadel$setPlayerUUID(entity.getUUID());
    }
}
