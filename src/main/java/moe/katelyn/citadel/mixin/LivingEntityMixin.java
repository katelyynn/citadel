package moe.katelyn.citadel.mixin;

import moe.katelyn.citadel.CitadelClient;
import moe.katelyn.citadel.hero.Mina;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void cancelFallDamage(double fallDistance, float damageModifier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "getItemHeldByArm", at = @At("HEAD"), cancellable = true)
    private void onGetItemHeldByArm(HumanoidArm arm, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player player)) return;

        Identifier heroId = Identifier.tryParse(CitadelClient.getPlayerHero(player.getUUID()));
        if (heroId == null) return;

        String heroPath = heroId.getPath();

        switch (heroPath) {
            case "mina":
                if (arm == HumanoidArm.LEFT) {
                    cir.setReturnValue(new ItemStack(Items.AMETHYST_SHARD, 1));
                } else {
                    cir.setReturnValue(new ItemStack(Mina.UMBRELLA, 1));
                }
                break;
            case "infernus":
                if (arm == HumanoidArm.LEFT) {
                    cir.setReturnValue(ItemStack.EMPTY);
                } else {
                    cir.setReturnValue(new ItemStack(Items.AMETHYST_SHARD, 1));
                }
                break;
        }
    }
}
