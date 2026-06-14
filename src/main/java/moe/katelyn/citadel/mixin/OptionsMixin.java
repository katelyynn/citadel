package moe.katelyn.citadel.mixin;

import com.github.exopandora.shouldersurfing.api.client.Perspective;
import com.github.exopandora.shouldersurfing.client.ShoulderSurfing;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Options.class)
public class OptionsMixin {
	@Shadow
	private CameraType cameraType;

	@Inject(at = @At("HEAD"), method = "getCameraType")
	private void init(CallbackInfoReturnable<CameraType> cir) {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return;

		ShoulderSurfing instance = ShoulderSurfing.getInstance();

		if (player.gameMode() != GameType.CREATIVE && !instance.isShoulderSurfing()) {
			instance.changePerspective(Perspective.SHOULDER_SURFING);
		}
	}
}

