package moe.katelyn.citadel.hero;

import moe.katelyn.citadel.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.html.parser.Entity;
import java.util.List;

import static moe.katelyn.citadel.CitadelHeroRegistry.DEFAULT_HERO;

public class Infernus {
    public static void register() {
        Identifier id = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "infernus");

        List<Ability> abilities = List.of(
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "napalm"),
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
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/napalm.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "flame_dash"),
                        List.of(
                                new Upgrade((38 * 20)),
                                new Upgrade((38 * 20)),
                                new Upgrade((26 * 20)),
                                new Upgrade((26 * 20))
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("flame_dash"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/flame_dash.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "afterburn"),
                        List.of(
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0)
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("afterburn"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/afterburn.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "test4"),
                        List.of(
                                new Upgrade((165 * 20)),
                                new Upgrade((165 * 20)),
                                new Upgrade((90 * 20)),
                                new Upgrade((90 * 20))
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("test4"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/afterburn34.png")
                )
        );

        CitadelHeroRegistry.register(id, new Hero(id, abilities));
    }
}
