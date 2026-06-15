package moe.katelyn.citadel.mixin.player;

import moe.katelyn.citadel.CitadelClient;
import moe.katelyn.citadel.accessor.HumanoidRenderStateAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Final
    @Shadow
    public ModelPart rightArm;

    @Final
    @Shadow
    public ModelPart leftArm;

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void onPoseRightArm(HumanoidRenderState state, CallbackInfo ci) {
        UUID uuid = ((HumanoidRenderStateAccessor) state).citadel$getPlayerUUID();
        if (uuid == null) return;

        Identifier heroId = Identifier.parse(CitadelClient.getPlayerHero(uuid));
        if (heroId == null) return;

        String heroPath = heroId.getPath();

        switch (heroPath) {
            case "mina":
                rightArm.xRot = 0.2f;
                rightArm.yRot = 0.0f;
                rightArm.zRot = 0.0f;
                ci.cancel();
                break;
            case "infernus":
                rightArm.xRot = -0.8f;
                rightArm.yRot = 0.0f;
                rightArm.zRot = 0.0f;
                ci.cancel();
                break;
            case "graves":
                rightArm.xRot = -0.1f;
                rightArm.yRot = 0.0f;
                rightArm.zRot = 0.0f;
                ci.cancel();
                break;
        }
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    private void onPoseLeftArm(HumanoidRenderState state, CallbackInfo ci) {
        UUID uuid = ((HumanoidRenderStateAccessor) state).citadel$getPlayerUUID();
        if (uuid == null) return;

        Identifier heroId = Identifier.parse(CitadelClient.getPlayerHero(uuid));
        if (heroId == null) return;

        String heroPath = heroId.getPath();

        switch (heroPath) {
            case "mina":
                leftArm.xRot = -0.6f;
                leftArm.yRot = 0.0f;
                leftArm.zRot = 0.0f;
                ci.cancel();
                break;
            case "infernus":
                leftArm.xRot = 0.0f;
                leftArm.yRot = 0.0f;
                leftArm.zRot = 0.0f;
                ci.cancel();
                break;
            case "graves":
                leftArm.xRot = -0.2f;
                leftArm.yRot = 0.0f;
                leftArm.zRot = 0.2f;
                ci.cancel();
                break;
        }
    }
}
