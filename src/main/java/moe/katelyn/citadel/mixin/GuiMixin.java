package moe.katelyn.citadel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
	@ModifyExpressionValue(
			method = "extractCrosshair",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"
			)
	)
	private boolean forceFirstPersonForCrosshair(boolean original) {
		return true;
	}

	@Inject(method = "extractHotbarAndDecorations", at = @At("HEAD"), cancellable = true)
	private void onExtractHotbarAndDecorations(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
		if (!showHud()) {
			ci.cancel();
		}
	}

	@Unique
    private static boolean showHud() {
		Minecraft client = Minecraft.getInstance();
		Player player = client.player;

		if (player == null) return true;
		if (player.gameMode() == null) return true;

		GameType gameMode = player.gameMode();

		return gameMode == GameType.CREATIVE || gameMode == GameType.SPECTATOR;
	}
}

