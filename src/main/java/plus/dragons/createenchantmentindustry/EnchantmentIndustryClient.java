package plus.dragons.createenchantmentindustry;


import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;
import plus.dragons.createenchantmentindustry.entry.CeiBlockPartials;
import plus.dragons.createenchantmentindustry.foundation.config.CeiConfigs;
import plus.dragons.createenchantmentindustry.foundation.events.ClientEvents;
import plus.dragons.createenchantmentindustry.foundation.ponder.CeiPonderPlugin;
import plus.dragons.createenchantmentindustry.foundation.ponder.CeiPonderScenes;
public class EnchantmentIndustryClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CeiBlockPartials.register();
		PonderIndex.addPlugin(new CeiPonderPlugin());

		BaseConfigScreen.setDefaultActionFor(EnchantmentIndustry.ID, screen -> screen
				.withButtonLabels(null, null, "Gameplay Settings")
				.withSpecs(null, null, CeiConfigs.SERVER_SPEC)
		);

		ClientEvents.register();
	}
}
