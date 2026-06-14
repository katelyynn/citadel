package moe.katelyn.citadel;

import moe.katelyn.citadel.hero.Infernus;
import moe.katelyn.citadel.hero.Mina;
import moe.katelyn.citadel.hero.None;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitadelHeroRegistry {
    public static final Map<Identifier, Hero> HERO_REGISTRY = new HashMap<>();
    public static final Identifier DEFAULT_HERO = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "none");

    public static void register(Identifier id, Hero hero) {
        HERO_REGISTRY.put(id, hero);
        Citadel.LOGGER.info("registered hero with id {}", id);
    }

    public static Hero get(Identifier id) {
        return HERO_REGISTRY.getOrDefault(id, HERO_REGISTRY.get(DEFAULT_HERO));
    }

    public static boolean contains(Identifier id) {
        return HERO_REGISTRY.containsKey(id);
    }

    public static Map<Identifier, Hero> getAll() {
        return new HashMap<>(HERO_REGISTRY);
    }

    public static List<Identifier> getAllAsList() {
        return HERO_REGISTRY.keySet().stream().toList();
    }

    public static int size() {
        return HERO_REGISTRY.size();
    }

    public static void registerAll() {
        Citadel.LOGGER.info("registering heroes");

        None.register();
        Infernus.register();
        Mina.register();

        Citadel.LOGGER.info("finished registering heroes");
    }
}
