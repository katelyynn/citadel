package moe.katelyn.citadel.hero;

import moe.katelyn.citadel.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static moe.katelyn.citadel.CitadelHeroRegistry.DEFAULT_HERO;

public class None {
    public static void register() {
        Identifier id = DEFAULT_HERO;

        List<Ability> abilities = List.of(
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "test1"),
                        List.of(
                                new Upgrade(2),
                                new Upgrade(2),
                                new Upgrade(2),
                                new Upgrade(2)
                        ),
                        (player, level) -> {
                            Vec3 lookAngle = player.getLookAngle();

                            Snowball snowball = new Snowball(EntityType.SNOWBALL, player.level());
                            snowball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                            snowball.setOwner(player);

                            snowball.setDeltaMovement(lookAngle.x * 1.0f, lookAngle.y * 1.0f, lookAngle.z * 1.0f);

                            player.level().addFreshEntity(snowball);
                        },
                        null
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "test2"),
                        List.of(
                                new Upgrade(60),
                                new Upgrade(40),
                                new Upgrade(20),
                                new Upgrade(10)
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("test2"));
                        },
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/afterbu34rn.png")
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "test3"),
                        List.of(
                                new Upgrade(60),
                                new Upgrade(40),
                                new Upgrade(20),
                                new Upgrade(10)
                        ),
                        (player, level) -> {
                            player.sendSystemMessage(Component.literal("test3"));
                        },
                        null
                ),
                new Ability(
                        Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "rutger_rocket"),
                        List.of(
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0),
                                new Upgrade(0)
                        ),
                        (player, level) -> {
                            float power = 1.0f + ((level + 1) * 1.0f);

                            player.level().explode(
                                    null,
                                    player.getX(),
                                    player.getY() - 0.3f,
                                    player.getZ(),
                                    power,
                                    Level.ExplosionInteraction.NONE
                            );
                        },
                        Identifier.withDefaultNamespace("textures/item/firework_rocket.png")
                )
        );

        CitadelHeroRegistry.register(id, new Hero(id, abilities));
    }
}
