package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.client.world.phys.PickOrigin;
import com.github.exopandora.shouldersurfing.api.client.world.phys.PickVector;
import com.github.exopandora.shouldersurfing.api.config.IObjectPickerConfig;

public class ObjectPickerConfig implements IObjectPickerConfig {
	@Override
	public double getCustomRaytraceDistance() {
		return 400.0;
	}
	
	@Override
	public boolean isCustomRaytraceDistanceEnabled() {
		return true;
	}
	
	@Override
	public PickOrigin getEntityPickOrigin() {
		return PickOrigin.CAMERA;
	}
	
	@Override
	public PickOrigin getBlockPickOrigin() {
		return PickOrigin.CAMERA;
	}
	
	@Override
	public PickVector getPickVector() {
		return PickVector.CAMERA;
	}
}
