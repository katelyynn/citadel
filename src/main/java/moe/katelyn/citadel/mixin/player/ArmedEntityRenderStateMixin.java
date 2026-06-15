package moe.katelyn.citadel.mixin.player;

import moe.katelyn.citadel.Citadel;
import moe.katelyn.citadel.CitadelClient;
import moe.katelyn.citadel.accessor.HumanoidRenderStateAccessor;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin {
    @Inject(method = "extractArmedEntityRenderState", at = @At("RETURN"), cancellable = true)
    private static void onExtractRenderState(LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks, CallbackInfo ci) {
        UUID uuid = entity.getUUID();
        String hero = CitadelClient.getPlayerHero(uuid);
        Citadel.LOGGER.info("entity {}, hero string is {}", uuid, hero);
        if (hero == null) return;

        Identifier heroId = Identifier.tryParse(hero);
        if (heroId == null) return;

        Citadel.LOGGER.info("entity {}, hero id is {}", uuid, heroId);

        String heroPath = heroId.getPath();
        Citadel.LOGGER.info("entity {}, hero path is {}", uuid, heroPath);

        state.leftHandItemStack = ItemStack.EMPTY;
        state.rightHandItemStack = ItemStack.EMPTY;

        switch (heroPath) {
            case "mina":
                state.leftHandItemStack = new ItemStack(Items.AMETHYST_SHARD, 1);
                break;
            case "infernus":
                state.rightHandItemStack = new ItemStack(Items.BLAZE_ROD, 1);
                break;
        }

        ci.cancel();
    }
}
