package plus.dragons.createenchantmentindustry.foundation.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public class CeiPonderPlugin implements PonderPlugin {
	@Override
	public @NotNull String getModId() {
		return "create_enchantment_industry";
	}

	@Override
	public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
		CeiPonderScenes.register(helper);
	}

	@Override
	public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
		CeiPonderTags.register(helper);
	}
}
