package moe.katelyn.citadel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@ModifyExpressionValue(
			method = "renderHandsWithItems",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;renderMainHand:Z",
					opcode = Opcodes.GETFIELD)
	)
	private boolean renderMainHand(boolean original) {
		return true;
	}

	@ModifyExpressionValue(
			method = "renderHandsWithItems",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;renderOffHand:Z",
					opcode = Opcodes.GETFIELD)
	)
	private boolean renderOffHand(boolean original) {
		return true;
	}
}

