package plus.dragons.createenchantmentindustry.foundation.config;


import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;

import net.createmod.catnip.config.ConfigBase;

import org.apache.commons.lang3.tuple.Pair;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import plus.dragons.createenchantmentindustry.EnchantmentIndustry;

public class CeiConfigs {

    public static CeiServerConfig SERVER;
    public static ForgeConfigSpec SERVER_SPEC;
	private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

	public static CeiServerConfig server() {
		return SERVER;
	}

	private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
		Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});

		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		CONFIGS.put(side, config);
		return config;
	}

	public static void register() {
		SERVER = register(CeiServerConfig::new, ModConfig.Type.SERVER);

		for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			ForgeConfigRegistry.INSTANCE.register(EnchantmentIndustry.ID, pair.getKey(), pair.getValue().specification);

		ModConfigEvents.loading(EnchantmentIndustry.ID).register(CeiConfigs::onLoad);
		ModConfigEvents.reloading(EnchantmentIndustry.ID).register(CeiConfigs::onReload);
	}

	public static void onLoad(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig
					.getSpec())
				config.onLoad();
	}

	public static void onReload(ModConfig modConfig) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == modConfig
					.getSpec())
				config.onReload();
	}

}
