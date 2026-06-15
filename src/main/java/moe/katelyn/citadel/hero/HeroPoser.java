package moe.katelyn.citadel.hero;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionf;

public class HeroPoser {
    public static void pose(AvatarRenderState state, Identifier hero) {
        state.bodyRot = -30.0f; // their body horizontally, 40 = left, -40 = right
        state.yRot = 0.0f; // their head looking horizontally
        state.xRot = 0.0f; // their head looking vertically
        state.scale = 1.4f;

        state.boundingBoxWidth = state.boundingBoxWidth / state.scale;
        state.boundingBoxHeight = state.boundingBoxHeight / state.scale;

        state.showHat = true;
        state.showJacket = true;
        state.showLeftSleeve = true;
        state.showLeftPants = true;
        state.showRightSleeve = true;
        state.showRightPants = true;

        switch (hero.getPath()) {
            default:
                break;
        }
    }

    public static Quaternionf rotate(Identifier hero) {
        Quaternionf rotation = null;

        switch (hero.getPath()) {
            default:
                rotation = new Quaternionf().rotateZ(0.0f * (float) Math.PI);
                break;
        }

        return rotation;
    }

    public static Quaternionf rotate2(Identifier hero) {
        Quaternionf rotation = null;

        switch (hero.getPath()) {
            default:
                rotation = new Quaternionf().rotateX(200.0f * (float) (Math.PI / 180.0));
                break;
        }

        return rotation;
    }
}
