package moe.katelyn.citadel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import moe.katelyn.citadel.net.HeroUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static moe.katelyn.citadel.Citadel.syncPlayerHero;

public class CitadelCommands {
    private static final SuggestionProvider<CommandSourceStack> HERO_SUGGESTIONS = (context, builder) -> {
        List<String> heroes = CitadelHeroRegistry.getAll()
                .keySet().stream()
                .map(Identifier::toString)
                .toList();
        return SharedSuggestionProvider.suggest(heroes, builder);
    };

    private static final SuggestionProvider<CommandSourceStack> TEAM_SUGGESTIONS = (context, builder) -> {
        List<String> teams = CitadelTeamRegistry.getAll()
                .keySet().stream()
                .map(Identifier::toString)
                .toList();
        return SharedSuggestionProvider.suggest(teams, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("citadel")
                        .then(Commands.literal("hero")
                                .then(Commands.literal("list")
                                        .executes(CitadelCommands::listHeroes)
                                )
                                .then(Commands.literal("set")
                                        .then(Commands.argument("hero", StringArgumentType.greedyString())
                                                .suggests(HERO_SUGGESTIONS)
                                                .executes(CitadelCommands::setHero)
                                        )
                                )
                        )
                        .then(Commands.literal("team")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("team", StringArgumentType.greedyString())
                                                .suggests(TEAM_SUGGESTIONS)
                                                .executes(CitadelCommands::setTeam)
                                        )
                                )
                        )
        );
    };

    private static int listHeroes(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        List<Identifier> heroes = CitadelHeroRegistry.getAllAsList();

        if (heroes.isEmpty()) {
            source.sendSystemMessage(Component.literal("no heroes"));
            return 0;
        }

        source.sendSystemMessage(Component.literal("debug hero names:"));
        for (Identifier id : heroes) {
            Hero hero = CitadelHeroRegistry.get(id);
            source.sendSystemMessage(Component.literal(id.getPath()));
        }

        return heroes.size();
    }

    private static int setHero(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String heroName = StringArgumentType.getString(context, "hero");

        Identifier heroId;
        if (heroName.contains(":")) {
            heroId = Identifier.tryParse(heroName);
        } else {
            heroId = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, heroName);
        }

        if (heroId == null) {
            source.sendSystemMessage(Component.literal("invalid format"));
            return 0;
        }

        if (!CitadelHeroRegistry.contains(heroId)) {
            source.sendSystemMessage(Component.literal("hero not found"));
            return 0;
        }

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            return 0;
        }

        PlayerHero playerHero = player.getAttached(Citadel.PLAYER_HERO);
        Hero hero = CitadelHeroRegistry.get(heroId);

        playerHero.changeHero(heroId);

        syncPlayerHero(player);

        for (int i = 0; i < hero.getAbilities().size(); i++) {
            Ability ability = hero.getAbilities().get(i);
            Identifier abilityId = ability.getId();

            ServerPlayNetworking.send(player, new Citadel.AbilityUpdatePayload(
                    i,
                    heroId.toString(),
                    0,
                    0,
                    false
            ));
        }

        Map<UUID, String> update = Map.of(player.getUUID(), heroId.toString());
        for (ServerPlayer online : context.getSource().getServer().getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(online, new HeroUpdatePayload(update));
        }

        source.sendSystemMessage(Component.literal("Changed hero to " + heroId.getPath()));

        return 1;
    }

    private static int setTeam(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String teamName = StringArgumentType.getString(context, "team");

        Identifier teamId;
        if (teamName.contains(":")) {
            teamId = Identifier.tryParse(teamName);
        } else {
            teamId = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, teamName);
        }

        if (teamId == null) {
            source.sendSystemMessage(Component.literal("invalid format"));
            return 0;
        }

        CitadelTeamRegistry.Team team = CitadelTeamRegistry.Team.fromIdentifier(teamId);

        if (team == null) {
            source.sendSystemMessage(Component.literal("team not found"));
            return 0;
        }

        if (!(source.getEntity() instanceof ServerPlayer player)) {
            return 0;
        }

        CitadelTeamRegistry.setTeam(player, team);

        return 1;
    }
}
