package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.client.CameraDistanceAttributeMode;
import com.github.exopandora.shouldersurfing.api.client.ViewBobbingMode;
import com.github.exopandora.shouldersurfing.api.config.ICameraConfig;

import java.util.List;

public class CameraConfig implements ICameraConfig {
	@Override
	public double getOffsetX() {
		return -1.1;
	}
	
	@Override
	public double getOffsetY() {
		return 0.0;
	}
	
	@Override
	public double getOffsetZ() {
		return 4.0;
	}
	
	@Override
	public List<Double> getOffsetXPresets() {
		return List.of();
	}
	
	@Override
	public List<Double> getOffsetYPresets() {
		return List.of();
	}
	
	@Override
	public List<Double> getOffsetZPresets() {
		return List.of();
	}
	
	@Override
	public double getMinOffsetX() {
		return -3.0;
	}
	
	@Override
	public double getMinOffsetY() {
		return -1.0;
	}
	
	@Override
	public double getMinOffsetZ() {
		return -3.0;
	}
	
	@Override
	public double getMaxOffsetX() {
		return 3.0;
	}
	
	@Override
	public double getMaxOffsetY() {
		return 1.5;
	}
	
	@Override
	public double getMaxOffsetZ() {
		return 5.0;
	}
	
	@Override
	public boolean isOffsetXUnlimited() {
		return false;
	}
	
	@Override
	public boolean isOffsetYUnlimited() {
		return false;
	}
	
	@Override
	public boolean isOffsetZUnlimited() {
		return false;
	}
	
	@Override
	public double getPassengerOffsetXMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getPassengerOffsetYMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getPassengerOffsetZMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getSprintOffsetXMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getSprintOffsetYMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getSprintOffsetZMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getAimingOffsetXMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getAimingOffsetYMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getAimingOffsetZMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getFallFlyingOffsetXMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getFallFlyingOffsetYMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getFallFlyingOffsetZMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getClimbingOffsetXMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getClimbingOffsetYMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getClimbingOffsetZMultiplier() {
		return 1.0;
	}
	
	@Override
	public double getPassengerOffsetXModifier() {
		return 1.0;
	}
	
	@Override
	public double getPassengerOffsetYModifier() {
		return 1.0;
	}
	
	@Override
	public double getPassengerOffsetZModifier() {
		return 1.0;
	}
	
	@Override
	public double getSprintOffsetXModifier() {
		return 0.0;
	}
	
	@Override
	public double getSprintOffsetYModifier() {
		return 0.0;
	}
	
	@Override
	public double getSprintOffsetZModifier() {
		return 0.0;
	}
	
	@Override
	public double getAimingOffsetXModifier() {
		return 0.0;
	}
	
	@Override
	public double getAimingOffsetYModifier() {
		return 0.0;
	}
	
	@Override
	public double getAimingOffsetZModifier() {
		return 0.0;
	}
	
	@Override
	public double getFallFlyingOffsetXModifier() {
		return 0.0;
	}
	
	@Override
	public double getFallFlyingOffsetYModifier() {
		return 0.0;
	}
	
	@Override
	public double getFallFlyingOffsetZModifier() {
		return 0.0;
	}
	
	@Override
	public double getClimbingOffsetXModifier() {
		return 0.0;
	}
	
	@Override
	public double getClimbingOffsetYModifier() {
		return 0.0;
	}
	
	@Override
	public double getClimbingOffsetZModifier() {
		return 0.0;
	}
	
	@Override
	public CameraDistanceAttributeMode getCameraDistanceAttributeMode() {
		return CameraDistanceAttributeMode.RELATIVE;
	}
	
	@Override
	public double keepCameraOutOfHeadMultiplier() {
		return 0.75;
	}
	
	@Override
	public double getCameraStepSize() {
		return 0.025;
	}
	
	@Override
	public double getCameraTransitionSpeedMultiplier() {
		return 0.25;
	}
	
	@Override
	public double getCenterCameraWhenLookingDownAngle() {
		return 0.0;
	}
	
	@Override
	public boolean isOffsetDynamic() {
		return false;
	}
	
	@Override
	public boolean isCameraDecoupled() {
		return false;
	}
	
	@Override
	public boolean isCameraOrientedOnTeleport() {
		return true;
	}
	
	@Override
	public boolean isFovOverrideEnabled() {
		return true;
	}
	
	@Override
	public float getFovOverride() {
		return 75.0f;
	}
	
	@Override
	public ViewBobbingMode getViewBobbingMode() {
		return ViewBobbingMode.INHERIT;
	}
	
	@Override
	public boolean isCameraTurningWithPlayer() {
		return true;
	}
	
	@Override
	public int getCameraTurningWithPlayerDelay() {
		return 20;
	}
	
	@Override
	public double getCameraDragXMultiplier() {
		return 0.0;
	}
	
	@Override
	public double getCameraDragYMultiplier() {
		return 0.0;
	}
	
	@Override
	public double getCameraDragZMultiplier() {
		return 0.0;
	}
	
	@Override
	public double getCameraSwayXMaxAngle() {
		return 0.0;
	}
	
	@Override
	public double getCameraSwayZMaxAngle() {
		return 0.0;
	}
	
	@Override
	public double getCameraSwayXMaxVelocity() {
		return 5.0;
	}
	
	@Override
	public double getCameraSwayZMaxVelocity() {
		return 5.0;
	}
	
	public void toggleOffsetXPreset() {
		return;
	}
	
	public void toggleOffsetYPreset() {
		return;
	}
	
	public void toggleOffsetZPreset() {
		return;
	}
	
	public void swapShoulder() {
		return;
	}
	
	public void toggleCameraCoupling() {
		return;
	}
}
