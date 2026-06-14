package moe.katelyn.citadel.mixin;

import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(CrossCollisionBlock.class)
public abstract class CrossCollisionBlockMixin {
    @Mutable
    @Final
    @Shadow
    private Function<BlockState, VoxelShape> collisionShapes;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void modifyCollisionHeight(float postWidth, float postHeight, float wallWidth, float wallHeight, float collisionHeight, BlockBehaviour.Properties properties, CallbackInfo ci) {
        // Only modify if this is a fence
        if ((Object)this instanceof FenceBlock) {
            // Recalculate collision shapes with height 16
            this.collisionShapes = this.makeShapes(postWidth, 16.0F, wallWidth, 0.0F, 16.0F);
        }
    }

    @Shadow
    protected abstract Function<BlockState, VoxelShape> makeShapes(float postWidth, float postHeight, float wallWidth, float wallBottom, float wallTop);
}
