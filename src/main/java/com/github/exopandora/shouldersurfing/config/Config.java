package com.github.exopandora.shouldersurfing.config;

import com.github.exopandora.shouldersurfing.api.config.IClientConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemUseAnimation;

public class Config {
	public static final ClientConfig CLIENT = new ClientConfig();

	public static class ClientConfig implements IClientConfig {
		private final CameraConfig cameraConfig;
		private final PerspectiveConfig perspectiveConfig;
		private final PlayerConfig playerConfig;
		private final ObjectPickerConfig objectPickerConfig;
		private final CrosshairConfig crosshairConfig;
		private final AudioConfig audioConfig;
		
		public ClientConfig() {
			this.audioConfig = new AudioConfig();
			this.cameraConfig = new CameraConfig();
			this.crosshairConfig = new CrosshairConfig();
			this.objectPickerConfig = new ObjectPickerConfig();
			this.perspectiveConfig = new PerspectiveConfig();
			this.playerConfig = new PlayerConfig();
		}
		
		@Override
		public CameraConfig getCameraConfig() {
			return this.cameraConfig;
		}
		
		@Override
		public PerspectiveConfig getPerspectiveConfig() {
			return this.perspectiveConfig;
		}
		
		@Override
		public PlayerConfig getPlayerConfig() {
			return this.playerConfig;
		}
		
		@Override
		public ObjectPickerConfig getObjectPickerConfig() {
			return this.objectPickerConfig;
		}
		
		@Override
		public CrosshairConfig getCrosshairConfig() {
			return this.crosshairConfig;
		}
		
		@Override
		public AudioConfig getAudioConfig() {
			return this.audioConfig;
		}
		
		protected static boolean isValidItemUseAnimation(Object id) {
			if (id == null) {
				return false;
			}
			
			for (ItemUseAnimation itemUseAnimation : ItemUseAnimation.values()) {
				if (itemUseAnimation.getSerializedName().equals(id)) {
					return true;
				}
			}
			
			return false;
		}
		
		protected static boolean isValidDataComponentId(Object id) {
			if (id == null) {
				return false;
			}
			Identifier location = Identifier.tryParse(id.toString());
			if (location == null) {
				return false;
			}
			return BuiltInRegistries.DATA_COMPONENT_TYPE.containsKey(location);
		}
		
		protected static boolean isValidItemWithSlot(Object id) {
			if (id == null) {
				return false;
			}
			String[] split = id.toString().split("@", 2);
			if (split.length < 2) {
				return false;
			}
			return Identifier.isValidNamespace(split[0]) && split[1] != null;
		}
		
		protected static boolean isValidDataComponentIdWithSlot(Object id) {
			if (id == null) {
				return false;
			}
			String[] split = id.toString().split("@", 2);
			if (split.length < 2) {
				return false;
			}
			return Identifier.isValidNamespace(split[0]) && isValidDataComponentId(split[1]);
		}
	}
}
