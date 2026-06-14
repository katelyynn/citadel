package moe.katelyn.citadel.mixin;

import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mannequin.class)
public interface MannequinMixin {
    @Invoker("setProfile")
    void callSetProfile(ResolvableProfile profile);
}
