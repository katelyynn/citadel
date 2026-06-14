package moe.katelyn.citadel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.*;

public class PlayerHero {
    public static final Codec<PlayerHero> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("hero").orElse(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "none"))
                            .forGetter(PlayerHero::getHero),

                    Codec.STRING.listOf().xmap(
                            list -> {
                                Set<Identifier> set = new HashSet<>();
                                for (String s : list) {
                                    Identifier id = Identifier.tryParse(s);
                                    if (id != null) set.add(id);
                                }
                                return set;
                            },
                            set -> set.stream().map(Identifier::toString).toList()
                    ).fieldOf("abilities").forGetter(data -> data.abilities),

                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("levels").forGetter(data -> data.levels),

                    Codec.unboundedMap(Identifier.CODEC, Codec.INT)
                            .fieldOf("cooldowns").forGetter(data -> data.cooldowns)
            ).apply(instance, PlayerHero::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerHero> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, PlayerHero::getHero,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), PlayerHero::getAbilitiesAsStrings,
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.INT), PlayerHero::getLevels,
            ByteBufCodecs.map(HashMap::new, Identifier.STREAM_CODEC, ByteBufCodecs.INT), PlayerHero::getCooldowns,
            PlayerHero::createForNetwork
    );

    private Identifier hero;
    private final Set<Identifier> abilities;
    private final Map<Identifier, Integer> levels;
    private final Map<Identifier, Integer> cooldowns;

    private transient boolean dirty = false;

    public PlayerHero() {
        this.hero = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "none");
        this.abilities = new HashSet<>();
        this.levels = new HashMap<>();
        this.cooldowns = new HashMap<>();
    }

    public PlayerHero(Identifier hero, Set<Identifier> abilities, Map<Identifier, Integer> levels, Map<Identifier, Integer> cooldowns) {
        this.hero = hero != null ? hero : Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "none");
        this.abilities = new HashSet<>(abilities);
        this.levels = new HashMap<>(levels);
        this.cooldowns = new HashMap<>(cooldowns);
        this.dirty = true;
    }

    public static PlayerHero createForNetwork(Identifier hero, List<String> abilitiesAsString, Map<Identifier, Integer> levels, Map<Identifier, Integer> cooldowns) {
        Set<Identifier> abilities = new HashSet<>();
        for (String s : abilitiesAsString) {
            Identifier id = Identifier.tryParse(s);
            if (id != null) abilities.add(id);
        }
        return new PlayerHero(hero, abilities, levels, cooldowns);
    }

    public boolean isDirty() { return dirty; }

    public void setClean() { this.dirty = false; }

    private void markDirty() { this.dirty = true; }

    public Identifier getHero() { return hero; }
    public void setHero(Identifier id) { this.hero = id; markDirty(); }

    public Map<Identifier, Integer> getLevels() { return levels; }
    public Map<Identifier, Integer> getCooldowns() { return cooldowns; }

    public boolean isAbilityAvailable(Identifier id) {
        return abilities.contains(id);
    }

    public void unlockAbility(Identifier id) {
        abilities.add(id);
        levels.putIfAbsent(id, 0);
        markDirty();

        Citadel.LOGGER.info("PlayerHero: Unlocked ability {}, list is now {}", id, getAbilitiesAsStrings());
    }

    public void unlockAbilities(Set<Identifier> ids) {
        for (Identifier id : ids) {
            unlockAbility(id);
        }
    }

    public Set<Identifier> getAbilities() {
        return new HashSet<>(abilities);
    }

    public List<String> getAbilitiesAsStrings() {
        List<String> strings = new ArrayList<>();
        for (Identifier id : abilities) {
            strings.add(id.toString());
        }
        return strings;
    }

    public int getUpgrade(Identifier id) {
        if (!isAbilityAvailable(id)) return -1;

        return levels.getOrDefault(id, 0);
    }

    public boolean canUpgrade(Identifier id) {
        if (!isAbilityAvailable(id)) return false;

        int current = levels.getOrDefault(id, 0);
        return current < 3;
    }

    public void setUpgrade(Identifier id, int level) {
        if (!canUpgrade(id)) return;

        levels.put(id, Math.min(level, 3));
        markDirty();
    }

    public boolean tryUpgrade(Identifier id) {
        if (!canUpgrade(id)) return false;

        int current = levels.getOrDefault(id, 0);
        levels.put(id, current + 1);
        markDirty();
        return true;
    }

    public int getRemainingCooldown(Identifier id) {
        if (!isAbilityAvailable(id)) return -1;

        return cooldowns.getOrDefault(id, 0);
    }

    public boolean isOnCooldown(Identifier id) {
        return isAbilityAvailable(id) && cooldowns.getOrDefault(id, 0) > 0;
    }

    public void setCooldown(Identifier id, int ticks) {
        if (ticks <= 0) cooldowns.remove(id);
        else cooldowns.put(id, ticks);
        markDirty();
    }

    public void tickCooldowns() {
        boolean changed = false;

        Iterator<Map.Entry<Identifier, Integer>> iterator = cooldowns.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Identifier, Integer> entry = iterator.next();
            int newValue = entry.getValue() - 1;
            if (newValue <= 0) {
                iterator.remove();
            } else {
                entry.setValue(newValue);
            }
            changed = true;
        }

        if (changed) {
            markDirty();
        }
    }

    public void changeHero(Identifier id) {
        this.hero = id;
        this.abilities.clear();
        this.levels.clear();
        this.cooldowns.clear();
        markDirty();

        Citadel.LOGGER.info("Changed hero to {}, abilities size is {}", id, abilities.size());
    }
}
