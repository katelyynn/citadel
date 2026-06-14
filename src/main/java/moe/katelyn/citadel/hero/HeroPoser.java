package moe.katelyn.citadel.hero;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Quaternionf;

public class HeroPoser {
    public static void pose(AvatarRenderState state, Identifier hero) {
        state.bodyRot = 0.0f;
        state.yRot = -10.0f;
        state.xRot = 10.0f;
        state.scale = 1.4f;

        state.boundingBoxWidth = state.boundingBoxWidth / state.scale;
        state.boundingBoxHeight = state.boundingBoxHeight / state.scale;

        state.showHat = true;
        state.showJacket = true;
        state.showLeftSleeve = true;
        state.showLeftPants = true;
        state.showRightSleeve = true;
        state.showRightPants = true;

        state.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        state.rightArmPose = HumanoidModel.ArmPose.EMPTY;

        switch (hero.getPath()) {
            case "infernus":
                state.bodyRot = -30.0f;
                state.yRot = -20.0f;
                state.xRot = 30.0f;
                state.rightArmPose = HumanoidModel.ArmPose.TOOT_HORN;
                break;
            case "mina":
                state.bodyRot = -25.0f;
                state.yRot = 5.0f;
                state.xRot = 15.0f;
                break;
            default:
                break;
        }
    }

    public static Quaternionf rotate(Identifier hero) {
        Quaternionf rotation = null;

        switch (hero.getPath()) {
            case "infernus":
                rotation = new Quaternionf().rotateZ(150.0f * (float) Math.PI);
                break;
            default:
                rotation = new Quaternionf().rotateZ(0.0f * (float) Math.PI);
                break;
        }

        return rotation;
    }

    public static Quaternionf rotate2(Identifier hero) {
        Quaternionf rotation = null;

        switch (hero.getPath()) {
            case "infernus":
                rotation = new Quaternionf().rotateX(160.0f * (float) (Math.PI / 180.0));
                break;
            default:
                rotation = new Quaternionf().rotateX(150.0f * (float) (Math.PI / 180.0));
                break;
        }

        return rotation;
    }
}
