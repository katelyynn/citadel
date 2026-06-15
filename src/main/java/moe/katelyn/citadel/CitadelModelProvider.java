package moe.katelyn.citadel;

import moe.katelyn.citadel.gen.ItemModelGenerator;
import moe.katelyn.citadel.hero.Mina;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import org.jspecify.annotations.NonNull;

public class CitadelModelProvider extends FabricModelProvider {
    public CitadelModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {
        ItemModelGenerator.registerMinaUmbrella(Mina.UMBRELLA, itemModelGenerators);
    }

    @Override
    public @NonNull String getName() {
        return "CitadelModelProvider";
    }
}
