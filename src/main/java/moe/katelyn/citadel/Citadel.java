package moe.katelyn.citadel;

import io.netty.buffer.ByteBuf;
import moe.katelyn.citadel.net.*;
import moe.katelyn.citadel.player.Movement;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Citadel implements ModInitializer {
	public static final String MOD_ID = "citadel";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final AttachmentType<PlayerHero> PLAYER_HERO = AttachmentRegistry.create(
			Identifier.fromNamespaceAndPath(MOD_ID, "player_hero"),
			builder -> builder
					.initializer(PlayerHero::new)
					.persistent(PlayerHero.CODEC)
					.copyOnDeath()
					.syncWith(PlayerHero.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
	);

	public record UnlockAbilityPayload(String id) implements CustomPacketPayload {
		public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(MOD_ID, "unlock_ability");
		public static final CustomPacketPayload.Type<UnlockAbilityPayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

		public static final StreamCodec<FriendlyByteBuf, UnlockAbilityPayload> CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				UnlockAbilityPayload::id,
				UnlockAbilityPayload::new
		);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record AbilityUpdatePayload(int index, String id, int cooldown, int level, boolean unlocked) implements CustomPacketPayload {
		public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(MOD_ID, "ability_update");
		public static final CustomPacketPayload.Type<AbilityUpdatePayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

		public static final StreamCodec<FriendlyByteBuf, AbilityUpdatePayload> CODEC = StreamCodec.composite(
				ByteBufCodecs.INT, AbilityUpdatePayload::index,
				ByteBufCodecs.STRING_UTF8, AbilityUpdatePayload::id,
				ByteBufCodecs.INT, AbilityUpdatePayload::cooldown,
				ByteBufCodecs.INT, AbilityUpdatePayload::level,
				ByteBufCodecs.BOOL, AbilityUpdatePayload::unlocked,
				AbilityUpdatePayload::new
		);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record UpgradeAbilityPayload(String id) implements CustomPacketPayload {
		public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(MOD_ID, "upgrade_ability");
		public static final CustomPacketPayload.Type<UpgradeAbilityPayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

		public static final StreamCodec<FriendlyByteBuf, UpgradeAbilityPayload> CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				UpgradeAbilityPayload::id,
				UpgradeAbilityPayload::new
		);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	public record SelectHeroPayload(String id) implements CustomPacketPayload {
		public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath(MOD_ID, "select_hero");
		public static final CustomPacketPayload.Type<SelectHeroPayload> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

		public static final StreamCodec<FriendlyByteBuf, SelectHeroPayload> CODEC = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				SelectHeroPayload::id,
				SelectHeroPayload::new
		);

		@Override
		public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}

	@Override
	public void onInitialize() {
		CitadelHeroRegistry.registerAll();
		registerNetworking();
		registerTicks();
		registerCommands();
	}

	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
			CitadelCommands.register(dispatcher);
		});
	}

	public static void syncPlayerHero(ServerPlayer player) {
		PlayerHero playerHero = player.getAttached(PLAYER_HERO);
		if (playerHero != null && playerHero.isDirty()) {
			player.setAttached(PLAYER_HERO, playerHero);
			playerHero.setClean();
		}
	}

	private void registerNetworking() {
		// C2S -> serverbound
		// S2C -> clientbound

		PayloadTypeRegistry.serverboundPlay().register(UseAbilityPayload.TYPE, UseAbilityPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(UpgradeAbilityPayload.TYPE, UpgradeAbilityPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(UnlockAbilityPayload.TYPE, UnlockAbilityPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SelectHeroPayload.TYPE, SelectHeroPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(DashPayload.TYPE, DashPayload.CODEC);
		PayloadTypeRegistry.serverboundPlay().register(DoubleJumpPayload.TYPE, DoubleJumpPayload.CODEC);

		PayloadTypeRegistry.clientboundPlay().register(AbilityUpdatePayload.TYPE, AbilityUpdatePayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(HeroUpdatePayload.TYPE, HeroUpdatePayload.CODEC);
		PayloadTypeRegistry.clientboundPlay().register(RequestHeroDataPayload.TYPE, RequestHeroDataPayload.CODEC);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.getPlayer();

			PlayerHero playerHero = player.getAttached(PLAYER_HERO);

			if (playerHero == null) {
				playerHero = new PlayerHero();
				player.setAttached(PLAYER_HERO, playerHero);
			}

			player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.12f);
			player.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(0.45f);

			Map<UUID, String> allHeroData = new HashMap<>();
			for (ServerPlayer online : server.getPlayerList().getPlayers()) {
				PlayerHero hero = online.getAttached(PLAYER_HERO);
				allHeroData.put(online.getUUID(), hero.getHero().toString());
			}

			allHeroData.put(player.getUUID(), playerHero.getHero().toString());

			Citadel.LOGGER.info("Sending all hero data to client {}", allHeroData);
			ServerPlayNetworking.send(player, new RequestHeroDataPayload(allHeroData));

			String newHeroId = playerHero.getHero().toString();
			for (ServerPlayer online : server.getPlayerList().getPlayers()) {
				if (online != player) {
					Map<UUID, String> update = Map.of(player.getUUID(), newHeroId);
					ServerPlayNetworking.send(online, new HeroUpdatePayload(update));
				}
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(DashPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
		});

		ServerPlayNetworking.registerGlobalReceiver(DoubleJumpPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
		});

		ServerPlayNetworking.registerGlobalReceiver(UseAbilityPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			PlayerHero playerHero = player.getAttached(PLAYER_HERO);

			if (playerHero == null) {
				player.sendSystemMessage(Component.literal("no hero data found"));
				return;
			}

			Hero hero = CitadelHeroRegistry.get(playerHero.getHero());
			if (hero.getAbilities().isEmpty()) {
				player.sendSystemMessage(Component.literal("no abilities found"));
				return;
			}

			if (payload.index() < 0 || payload.index() >= hero.getAbilities().size()) {
				player.sendSystemMessage(Component.literal("invalid ability index"));
				return;
			}

			Ability ability = hero.getAbilities().get(payload.index());
			Identifier id = ability.getId();

			if (!playerHero.isAbilityAvailable(id)) {
				player.sendSystemMessage(Component.literal("ability unavailable"));
				return;
			}

			if (playerHero.isOnCooldown(id)) {
				int remaining = playerHero.getRemainingCooldown(id);
				player.sendSystemMessage(Component.literal("ability on cooldown for " + (remaining / 20) + " seconds"));
				return;
			}

			int level = playerHero.getUpgrade(id);
			ability.execute(player, level);

			Upgrade upgrade = ability.getUpgrade(level);
			playerHero.setCooldown(id, upgrade.getCooldown());

			ServerPlayNetworking.send(player, new AbilityUpdatePayload(
					payload.index(),
					hero.getId().toString(),
					upgrade.getCooldown(),
					level,
					true
			));

			syncPlayerHero(player);
		});

		ServerPlayNetworking.registerGlobalReceiver(UpgradeAbilityPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			PlayerHero playerHero = player.getAttached(PLAYER_HERO);

			Identifier ability = Identifier.tryParse(payload.id());

			if (playerHero == null || ability == null) return;

			if (!playerHero.isAbilityAvailable(ability)) {
				player.sendSystemMessage(Component.literal("cannot upgrade - not unlocked"));
				return;
			}

			if (playerHero.tryUpgrade(ability)) {
				int level = playerHero.getUpgrade(ability);

				Hero hero = CitadelHeroRegistry.get(playerHero.getHero());
				int abilityIndex = -1;
				for (int i = 0; i < hero.getAbilities().size(); i++) {
					if (hero.getAbilities().get(i).getId().equals(ability)) {
						abilityIndex = i;
						break;
					}
				}

				if (abilityIndex != -1) {
					ServerPlayNetworking.send(player, new AbilityUpdatePayload(
							abilityIndex,
							hero.getId().toString(),
							playerHero.getRemainingCooldown(ability),
							level,
							true
					));
				}

				player.sendSystemMessage(Component.literal("ability upgraded to level " + level));
				syncPlayerHero(player);
			} else {
				player.sendSystemMessage(Component.literal("ability already at max level"));
			}
		});

		ServerPlayNetworking.registerGlobalReceiver(SelectHeroPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			Identifier heroId = Identifier.tryParse(payload.id());

			if (heroId == null) {
				player.sendSystemMessage(Component.literal("invalid hero"));
				return;
			}

			Hero hero = CitadelHeroRegistry.get(heroId);
			PlayerHero playerHero = player.getAttached(PLAYER_HERO);
			playerHero.changeHero(heroId);

			playerHero.getAbilities().clear();

			syncPlayerHero(player);

			player.sendSystemMessage(Component.literal("you are now " + heroId.getPath()));
		});

		ServerPlayNetworking.registerGlobalReceiver(UnlockAbilityPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			PlayerHero playerHero = player.getAttached(PLAYER_HERO);

			Identifier abilityId = Identifier.tryParse(payload.id());
			if (playerHero == null || abilityId == null) return;

			if (!playerHero.isAbilityAvailable(abilityId)) {
				playerHero.unlockAbility(abilityId);
				syncPlayerHero(player);

				Hero hero = CitadelHeroRegistry.get(playerHero.getHero());
				int abilityIndex = -1;
				for (int i = 0; i < hero.getAbilities().size(); i++) {
					if (hero.getAbilities().get(i).getId().equals(abilityId)) {
						abilityIndex = i;
						break;
					}
				}

				if (abilityIndex != -1) {
					ServerPlayNetworking.send(player, new AbilityUpdatePayload(
							abilityIndex,
							hero.getId().toString(),
							playerHero.getRemainingCooldown(abilityId),
							playerHero.getUpgrade(abilityId),
							true
					));
					Citadel.LOGGER.info("Ability {} should be unlocked now for hero {} with index {}, list: {}", abilityId, hero.getId(), abilityIndex, playerHero.getAbilitiesAsStrings());
				}
			}
		});
	}

	private void registerTicks() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				PlayerHero data = player.getAttached(PLAYER_HERO);
				if (data != null) {
					data.tickCooldowns();

					if (data.isDirty()) {
						syncPlayerHero(player);
					}
				}
			}
		});
	}
}