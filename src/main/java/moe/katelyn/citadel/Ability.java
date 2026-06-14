package moe.katelyn.citadel;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.BiConsumer;

public class Ability {
    private final Identifier id;
    private final Identifier icon;
    private final List<Upgrade> upgrades;
    private final BiConsumer<Player, Integer> action;

    public Ability(Identifier id, List<Upgrade> upgrades, BiConsumer<Player, Integer> action, Identifier icon) {
        this.id = id;
        this.upgrades = upgrades;
        this.action = action;
        this.icon = icon;
    }

    public void execute(Player player, int level) {
        int effectiveLevel = Math.min(level, upgrades.size());
        action.accept(player, effectiveLevel);
    }

    public Identifier getId() { return id; }
    public List<Upgrade> getUpgrades() { return upgrades; }
    public Upgrade getUpgrade(int level) { return upgrades.get(level); }

    public String getName() {
        return "ability." + id.getNamespace() + "." + id.getPath() + ".name";
    }

    public String getDescription() {
        return "ability." + id.getNamespace() + "." + id.getPath() + ".description";
    }

    public Identifier getIcon() { return icon; }

    public String getUpgradeDescription(int level) {
        return "ability." + id.getNamespace() + "." + id.getPath() + ".upgrade_" + level;
    }
}
