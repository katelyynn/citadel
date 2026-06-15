package moe.katelyn.citadel.gen;

import moe.katelyn.citadel.Citadel;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Optional;

public class ItemModelGenerator {
    private static ModelTemplate item(String parent, TextureSlot textureSlot) {
        return new ModelTemplate(Optional.of(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "item/" + parent)), Optional.empty(), textureSlot);
    }

    public static final ModelTemplate MINA_UMBRELLA = item("mina_umbrella", TextureSlot.LAYER0);

    public static void registerMinaUmbrella(Item item, ItemModelGenerators generators) {
        generators.itemModelOutput.accept(item, ItemModelUtils.plainModel(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "item/mina_umbrella")));
    }
}
