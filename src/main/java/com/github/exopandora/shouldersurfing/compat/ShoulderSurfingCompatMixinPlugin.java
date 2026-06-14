package com.github.exopandora.shouldersurfing.compat;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.List;
import java.util.Set;

public abstract class ShoulderSurfingCompatMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
		
	}
	
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	
	}
	
	protected static void addCommonCompatMixins(List<String> mixins) {
		addWildfireGenderMixins(mixins);
	}
	
	private static void addWildfireGenderMixins(List<String> mixins) {
		if (Mods.WILDFIRE_GENDER.isLoaded()) {
			mixins.add("wildfiregender.BreastRenderCommandMixin");
		}
	}
}
