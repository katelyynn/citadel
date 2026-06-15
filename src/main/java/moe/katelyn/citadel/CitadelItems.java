package moe.katelyn.citadel;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class CitadelItems {
    public static <T extends Item> T register(String name, Function<Item.Properties, T> factory, Item.Properties settings) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Citadel.MOD_ID, name));

        T item = factory.apply(settings.setId(key));

        Registry.register(BuiltInRegistries.ITEM, key, item);

        return item;
    }

    public static void init() {}
}
