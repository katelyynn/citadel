package moe.katelyn.citadel;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CitadelTeamRegistry {
    public enum Team {
        AMBER("amber", 0xFFD4870C),
        SAPPHIRE("sapphire", 0xFF4E76C4),
        NEUTRAL("neutral", 0xFF909090);

        private final String id;
        private final int colour;

        Team(String id, int colour) {
            this.id = id;
            this.colour = colour;
        }

        public String getId() { return id; }
        public int getColour() { return colour; }

        public static Team fromId(String id) {
            for (Team team : values()) {
                if (team.id.equals(id)) {
                    return team;
                }
            }
            return NEUTRAL;
        }

        public static Team fromIdentifier(Identifier id) {
            return Team.fromId(id.getPath());
        }
    }

    private static final Map<UUID, Team> teams = new HashMap<>();

    public static Map<Identifier, Team> getAll() {
        Map<Identifier, Team> result = new LinkedHashMap<>();
        for (Team team : Team.values()) {
            result.put(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, team.id), team);
        }
        return result;
    }

    public static void setTeam(Player player, Team team) {
        setTeam(player.getUUID(), team);
    }

    public static void setTeam(UUID uuid, Team team) {
        teams.put(uuid, team);
    }

    public static Team getTeam(Player player) {
        return getTeam(player.getUUID());
    }

    public static Team getTeam(UUID uuid) {
        return teams.getOrDefault(uuid, Team.NEUTRAL);
    }

    public static void removeTeam(Player player) {
        teams.remove(player.getUUID());
    }

    public static Map<UUID, Team> getTeams() {
        return new HashMap<>(teams);
    }

    public static int count(Team team) {
        return (int) teams.values().stream().filter(t -> t == team).count();
    }
}
