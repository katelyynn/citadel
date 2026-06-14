package moe.katelyn.citadel.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void onCreateAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        AttributeSupplier.Builder builder = LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.05F)
                .add(Attributes.ATTACK_SPEED, 4.0)
                .add(Attributes.LUCK, 2.0)
                .add(Attributes.BLOCK_INTERACTION_RANGE, 6.0)
                .add(Attributes.BLOCK_BREAK_SPEED, 1.5)
                .add(Attributes.SUBMERGED_MINING_SPEED, 1.2)
                .add(Attributes.SNEAKING_SPEED, 0.1)
                .add(Attributes.MINING_EFFICIENCY, 2.0)
                .add(Attributes.SWEEPING_DAMAGE_RATIO, 1.0)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 6.0E7)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE, 6.0E7);

        cir.setReturnValue(builder);
        cir.cancel();
    }
}
