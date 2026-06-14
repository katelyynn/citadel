package com.github.exopandora.shouldersurfing.client;

import com.github.exopandora.shouldersurfing.api.client.Perspective;
import com.github.exopandora.shouldersurfing.api.math.Vec2f;
import com.github.exopandora.shouldersurfing.config.Config;
import com.github.exopandora.shouldersurfing.mixin.ClientInputAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.github.exopandora.shouldersurfing.ShoulderSurfingCommon.MOD_ID;

public class InputHandler {
	private final ShoulderSurfing instance;
	
	public InputHandler(ShoulderSurfing instance) {
		this.instance = instance;
	}
	
	public void tick() {
		Options options = Minecraft.getInstance().options;
		
		while (options.keyTogglePerspective.consumeClick()) {
			this.instance.togglePerspective();
		}
	}
	
	public void updateMovementInput(ClientInput input) {
		Minecraft minecraft = Minecraft.getInstance();
		Entity cameraEntity = minecraft.getCameraEntity();
		
		if (this.instance.isFreeLooking() || cameraEntity == null || EventHooks.isForcingVanillaPlayerInput(cameraEntity)) {
			return;
		}
		
		Vec2f moveVector = new Vec2f(input.getMoveVector());
		
		if (this.instance.isShoulderSurfing() && minecraft.player != null && cameraEntity == minecraft.player && moveVector.lengthSquared() > 0) {
			ShoulderSurfingCamera camera = this.instance.getCamera();
			LocalPlayer player = minecraft.player;
			float yRot = player.getYRot();
			
			if (this.instance.isCameraDecoupled() && !this.instance.isLookFollowingCrosshairTarget()) {
				// Update player rotations according to keyboard inputs and camera rotation
				float cameraXRot = camera.getXRot();
				float cameraYRot = camera.getYRot();
				Vec2f rotated = moveVector.rotateDegrees(cameraYRot);
				float xRot = cameraXRot * 0.5F;
				float xRotO = player.getXRot();
				float yRotO = yRot;
				yRot = (float) Mth.wrapDegrees(Math.atan2(-rotated.x(), rotated.y()) * Mth.RAD_TO_DEG);
				float turningSpeedMultiplier = (float) Config.CLIENT.getPlayerConfig().getTurningSpeedMultiplier();
				xRot = xRotO + Mth.degreesDifference(xRotO, xRot) * turningSpeedMultiplier;
				yRot = yRotO + Mth.degreesDifference(yRotO, yRot) * turningSpeedMultiplier;
				player.setXRot(xRot);
				player.setYRot(yRot);
			}
			
			Vec2f rotated = moveVector.rotateDegrees(Mth.degreesDifference(yRot, camera.getYRot()));
			((ClientInputAccessor) input).setMoveVector(rotated.toVec2());
		}
	}
}
