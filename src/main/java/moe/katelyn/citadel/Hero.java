package moe.katelyn.citadel;

import net.minecraft.resources.Identifier;

import java.util.List;

public class Hero {
    private final Identifier id;
    private final List<Ability> abilities;

    public Hero(Identifier id, List<Ability> abilities) {
        this.id = id;
        this.abilities = abilities;
    }

    public Identifier getId() { return id; }
    public List<Ability> getAbilities() { return abilities; }

    public String getTranslation() {
        return "hero." + id.getNamespace() + "." + id.getPath();
    }
}
