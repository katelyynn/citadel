package moe.katelyn.citadel;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import moe.katelyn.citadel.hud.AbilityHud;
import moe.katelyn.citadel.hud.AbilityItem;
import moe.katelyn.citadel.hud.PlayerList;
import moe.katelyn.citadel.net.*;
import moe.katelyn.citadel.player.Movement;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.lwjgl.glfw.GLFW;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CitadelClient implements ClientModInitializer {
    private static final Map<UUID, String> playerHeroes = new ConcurrentHashMap<>();

    public static String getPlayerHero(UUID uuid) {
        return playerHeroes.get(uuid);
    }

    KeyMapping.Category HERO_BINDS = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "hero")
    );

    private static KeyMapping ability1;
    private static KeyMapping ability2;
    private static KeyMapping ability3;
    private static KeyMapping ability4;

    private static AbilityHud abilityHud;
    private static PlayerList playerList;

    private static KeyMapping dash;
    private static KeyMapping doubleJump;

    private Identifier lastHeroId = null;
    private long lastHeroCheck = 0;

    public static KeyMapping getAbilityByIndex(int index) {
        return switch (index) {
            case 0 -> ability1;
            case 1 -> ability2;
            case 2 -> ability3;
            case 3 -> ability4;
            default -> null;
        };
    }

    public static KeyMapping getAbility1() { return ability1; }
    public static KeyMapping getAbility2() { return ability2; }
    public static KeyMapping getAbility3() { return ability3; }
    public static KeyMapping getAbility4() { return ability4; }

    public static String getKeybindString(int index) {
        KeyMapping key = getAbilityByIndex(index);
        return key != null ? key.getTranslatedKeyMessage().getString() : "?";
    }

    public static AbilityHud getInstance() {
        if (abilityHud == null) {
            abilityHud = new AbilityHud();
        }
        return abilityHud;
    }

    @Override
    public void onInitializeClient() {
        ability1 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.ability1",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_1,
                this.HERO_BINDS
        ));

        ability2 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.ability2",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_2,
                this.HERO_BINDS
        ));

        ability3 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.ability3",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_3,
                this.HERO_BINDS
        ));

        ability4 = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.ability4",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_4,
                this.HERO_BINDS
        ));

        dash = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.dash",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_SHIFT,
                this.HERO_BINDS
        ));

        doubleJump = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.citadel.double_jump",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_SPACE,
                this.HERO_BINDS
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            long window = client.getWindow().handle();
            boolean isAlt = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS;

            while (ability1.consumeClick()) {
                if (isAlt) {
                    handleAlt(client.player, 0);
                } else {
                    ClientPlayNetworking.send(new UseAbilityPayload(0));
                }
            }

            while (ability2.consumeClick()) {
                if (isAlt) {
                    handleAlt(client.player, 1);
                } else {
                    ClientPlayNetworking.send(new UseAbilityPayload(1));
                }
            }

            while (ability3.consumeClick()) {
                if (isAlt) {
                    handleAlt(client.player, 2);
                } else {
                    ClientPlayNetworking.send(new UseAbilityPayload(2));
                }
            }

            while (ability4.consumeClick()) {
                if (isAlt) {
                    handleAlt(client.player, 3);
                } else {
                    ClientPlayNetworking.send(new UseAbilityPayload(3));
                }
            }

            while (dash.consumeClick()) {
                Movement.requestDash();
            }

            while (doubleJump.consumeClick()) {
                Movement.requestDoubleJump();
            }
        });

        getInstance();
        playerList = new PlayerList();

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_hud"), ((graphics, deltaTracker) -> getInstance().render(graphics, deltaTracker)));
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "player_list"), ((graphics, deltaTracker) -> playerList.render(graphics, deltaTracker)));

        ClientPlayNetworking.registerGlobalReceiver(Citadel.AbilityUpdatePayload.TYPE, (payload, context) -> {
            Citadel.LOGGER.info("Received update request on client with id {}", payload.id());
            getInstance().updateAbility(
                    payload.index(),
                    payload.id(),
                    payload.cooldown(),
                    payload.level(),
                    payload.unlocked()
            );
        });

        ClientPlayNetworking.registerGlobalReceiver(RequestHeroDataPayload.TYPE, (payload, context) -> {
            playerHeroes.clear();
            playerHeroes.putAll(payload.heroes());
            Citadel.LOGGER.info("playerHeroes (local): {}", playerHeroes);
        });

        ClientPlayNetworking.registerGlobalReceiver(HeroUpdatePayload.TYPE, (payload, context) -> {
            playerHeroes.putAll(payload.heroes());
            Citadel.LOGGER.info("playerHeroes (local): {}", playerHeroes);
        });
    }

    private void handleAlt(Player player, int index) {
        AbilityItem widget = getInstance().getWidget(index);

        Ability ability = widget.getAbility();

        boolean unlocked = widget.isUnlocked();
        int level = widget.getLevel();

        if (!unlocked) {
            Citadel.LOGGER.info("handleAlt: {} is not unlocked, proceeding to unlock", ability.getName());
            ClientPlayNetworking.send(new Citadel.UnlockAbilityPayload(ability.getId().toString()));
        } else if (level < 3) {
            Citadel.LOGGER.info("handleAlt: {} is unlocked, proceeding to upgrade", ability.getName());
            ClientPlayNetworking.send(new Citadel.UpgradeAbilityPayload(ability.getId().toString()));
        }
    }
}
