package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.client.Perspective;
import com.github.exopandora.shouldersurfing.api.config.IPerspectiveConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;

public class PerspectiveConfig implements IPerspectiveConfig {
	@Override
	public boolean isThirdPersonReplaced() {
		return true;
	}
	
	@Override
	public boolean isFirstPersonEnabled() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null) return false;

        return player.gameMode() == GameType.CREATIVE;
    }
	
	@Override
	public boolean isThirdPersonFrontEnabled() {
		return false;
	}
	
	@Override
	public boolean isThirdPersonBackEnabled() {
		return false;
	}
	
	@Override
	public Perspective getDefaultPerspective() {
		return Perspective.SHOULDER_SURFING;
	}
	
	public void setDefaultPerspective(Perspective perspective) {
		return;
	}
	
	@Override
	public boolean isPerspectivePersistent() {
		return true;
	}
}
