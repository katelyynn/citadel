package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.client.CrosshairType;
import com.github.exopandora.shouldersurfing.api.client.CrosshairVisibility;
import com.github.exopandora.shouldersurfing.api.client.Perspective;
import com.github.exopandora.shouldersurfing.api.config.ICrosshairConfig;

import java.util.List;

public class CrosshairConfig implements ICrosshairConfig {
	@Override
	public CrosshairVisibility getCrosshairVisibility(Perspective perspective) {
		return CrosshairVisibility.ALWAYS;
	}
	
	@Override
	public CrosshairType getCrosshairType() {
		return CrosshairType.STATIC;
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairHoldItems() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairUseItems() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairHoldItemAnimations() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairUseItemAnimations() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairHoldItemDefaultComponents() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairUseItemDefaultComponents() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairHoldItemComponents() {
		return List.of();
	}
	
	@Override
	public List<? extends String> getAdaptiveCrosshairUseItemComponents() {
		return List.of();
	}
	
	@Override
	public boolean isObstructionIndicatorEnabled() {
		return true;
	}
	
	@Override
	public boolean isObstructionIndicatorOnlyShownWhenAiming() {
		return false;
	}
	
	@Override
	public double getObstructionIndicatorMaxDistanceToObstruction() {
		return 8.0;
	}
	
	@Override
	public int getObstructionIndicatorMinDistanceToCrosshair() {
		return 20;
	}
}
