package moe.katelyn.citadel;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class CitadelTranslations extends FabricLanguageProvider {
    protected CitadelTranslations(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider holderLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("key.category.citadel.hero", "Abilities");
        translationBuilder.add("key.citadel.ability1", "Ability 1");
        translationBuilder.add("key.citadel.ability2", "Ability 2");
        translationBuilder.add("key.citadel.ability3", "Ability 3");
        translationBuilder.add("key.citadel.ability4", "Ability 4");
    }
}
