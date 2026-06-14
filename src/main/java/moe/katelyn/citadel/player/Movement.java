package moe.katelyn.citadel.player;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Movement {
    private static boolean wantsDoubleJump = false;
    private static boolean wantsDash = false;
    private static int dashTicks = 0;
    private static int doubleJumpTicks = 0;
    private static int doubleJumpDelay = 0;

    public static void requestDoubleJump() {
        wantsDoubleJump = true;
    }

    public static void requestDash() {
        wantsDash = true;
    }

    public static void tick(LocalPlayer player) {
        if (dashTicks > 0) {
            dashTicks--;
            checkY(player);
        }

        if (wantsDoubleJump) {
            wantsDoubleJump = false;
            doubleJumpDelay = 0;
            doubleJumpTicks = 0;
            doubleJump(player);
        }

        if (doubleJumpDelay > 0) {
            doubleJumpDelay--;
            if (doubleJumpDelay == 0) {
                doubleJumpTicks = 1;
            }
        }

        if (doubleJumpTicks > 0) {
            doubleJumpTicks--;
            checkY(player);
        }

        if (wantsDash) {
            wantsDash = false;
            dash(player);
        }
    }

    private static void checkY(LocalPlayer player) {
        Vec3 motion = player.getDeltaMovement();
        if (motion.y < 0.0f) {
            player.setDeltaMovement(motion.x, 0.0f, motion.z);
        }
    }

    private static void doubleJump(LocalPlayer player) {
        if (player.fallDistance == 0 && dashTicks == 0) return;
        dashTicks = 0;

        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x, 0.7f, motion.z);

        doubleJumpDelay = 9;
    }

    private static void dash(LocalPlayer player) {
        Vec2 move = player.input.getMoveVector();
        float forward = move.y;
        float strafe = move.x;

        boolean onGround = player.onGround();

        if (Math.abs(forward) < 1.0E-5F && Math.abs(strafe) < 1.0E-5F) {
            return;
        }

        float yaw = player.getYRot() * ((float)Math.PI / 180f);
        float sin = Mth.sin(yaw);
        float cos = Mth.cos(yaw);
        float len = Mth.sqrt(forward * forward + strafe * strafe);

        double dirX = (strafe * cos - forward * sin) / len;
        double dirZ = (forward * cos + strafe * sin) / len;
        double power = 0.8;

        if (onGround) {
            power = 1.0;
        }

        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(dirX * power, 0.0f, dirZ * power);

        dashTicks = 7;
    }
}
