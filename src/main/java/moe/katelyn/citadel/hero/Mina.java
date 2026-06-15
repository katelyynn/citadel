package moe.katelyn.citadel.hero;

import moe.katelyn.citadel.*;
import moe.katelyn.citadel.gen.ItemModelGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Mina {
    public static final Item UMBRELLA = CitadelItems.register("mina_umbrella", Item::new, new Item.Properties());

    public static void register() {
        Identifier id = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "mina");

        List<Ability> abilities = List.of(
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "rake"),
                        List.of(
                                new Upgrade((25 * 20)),
                                new Upgrade((25 * 20)),
                                new Upgrade((25 * 20)),
                                new Upgrade((25 * 20))
                        ),
                        (player, level) -> {
                            Vec3 lookAngle = player.getLookAngle();

                            Snowball snowball = new Snowball(EntityType.SNOWBALL, player.level());
                            snowball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                            snowball.setOwner(player);

                            snowball.setDeltaMovement(lookAngle.x * 1.0f, lookAngle.y * 1.0f, lookAngle.z * 1.0f);

                            player.level().addFreshEntity(snowball);
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/rake.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "retreat"),
                        List.of(
                                new Upgrade((38 * 20)),
                                new Upgrade((38 * 20)),
                                new Upgrade((26 * 20)),
                                new Upgrade((26 * 20))
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("retreat"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/retreat.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "love_bites"),
                        List.of(
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0)
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("love_bites"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/love_bites.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "nox_nostra"),
                        List.of(
                                new Upgrade((165 * 20)),
                                new Upgrade((165 * 20)),
                                new Upgrade((90 * 20)),
                                new Upgrade((90 * 20))
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("nox_nostra"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/nox_nostra.png")
                )
        );

        CitadelHeroRegistry.register(id, new Hero(id, abilities));
    }
}
