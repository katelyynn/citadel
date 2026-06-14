package moe.katelyn.citadel.mixin.ui;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;
import java.util.Set;

@Mixin(KeyBindsList.class)
public abstract class KeyBindsListMixin {
    private static final Set<String> FILTERED_KEYS = Set.of(
            "key.sneak",
            "key.sprint",
            "key.drop",
            "key.inventory",
            "key.toggleGui",
            "key.advancements",
            "key.smoothCamera",
            "key.spectatorOutlines",
            "key.spectatorHotbar",
            "key.togglePerspective",
            "key.swapOffhand",
            "key.quickActions",
            "key.attack",
            "key.use",
            "key.playerlist",
            "key.pickItem",
            "key.saveToolbarActivator",
            "key.loadToolbarActivator",
            "key.hotbar.1",
            "key.hotbar.2",
            "key.hotbar.3",
            "key.hotbar.4",
            "key.hotbar.5",
            "key.hotbar.6",
            "key.hotbar.7",
            "key.hotbar.8",
            "key.hotbar.9",
            "key.toggleSpectatorShaderEffects",
            "key.socialInteractions"
    );

    @ModifyVariable(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;)V"),
            ordinal = 0
    )
    private KeyMapping[] filterKeyMappings(KeyMapping[] keyMappings) {
        return Arrays.stream(keyMappings)
                .filter(key -> !FILTERED_KEYS.contains(key.getName()) && !key.getName().startsWith("key.debug."))
                .toArray(KeyMapping[]::new);
    }
}
