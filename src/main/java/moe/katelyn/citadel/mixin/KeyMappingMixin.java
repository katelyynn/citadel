package moe.katelyn.citadel.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import moe.katelyn.citadel.CitadelClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin {
	@Inject(method = "click", at = @At("HEAD"), cancellable = true)
	private static void onClick(InputConstants.Key key, CallbackInfo ci) {
		long handle = Minecraft.getInstance().getWindow().handle();

		if (CitadelClient.getInstance().isTabHeld(handle)) {
			ci.cancel();
		}
	}
}

