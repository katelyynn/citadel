package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.client.TurningMode;
import com.github.exopandora.shouldersurfing.api.config.IPlayerConfig;

public class PlayerConfig implements IPlayerConfig {
	@Override
	public double getHidePlayerWhenLookingUpAngle() {
		return 0.0;
	}
	
	@Override
	public boolean isPlayerTransparencyEnabled() {
		return true;
	}
	
	@Override
	public boolean isPlayerTransparentWhenAiming() {
		return false;
	}
	
	@Override
	public TurningMode getTurningModeWhenUsingItem() {
		return TurningMode.NEVER;
	}
	
	@Override
	public TurningMode getTurningModeWhenAttacking() {
		return TurningMode.NEVER;
	}
	
	@Override
	public TurningMode getTurningModeWhenInteracting() {
		return TurningMode.NEVER;
	}
	
	@Override
	public TurningMode getTurningModeWhenPicking() {
		return TurningMode.NEVER;
	}
	
	@Override
	public int getTurningLockTime() {
		return 4;
	}
	
	@Override
	public double getTurningSpeedMultiplier() {
		return 0.25;
	}
	
	@Override
	public boolean isPlayerXRotTurningWithCamera() {
		return false;
	}
	
	@Override
	public boolean isPlayerYRotTurningWithCamera() {
		return false;
	}
	
	@Override
	public double getPlayerYRotTurnAngleLimit() {
		return 90.0;
	}
}
