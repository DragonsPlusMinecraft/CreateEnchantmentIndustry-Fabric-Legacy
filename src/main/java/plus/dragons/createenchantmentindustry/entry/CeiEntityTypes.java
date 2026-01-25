package plus.dragons.createenchantmentindustry.entry;

import static plus.dragons.createenchantmentindustry.EnchantmentIndustry.REGISTRATE;

import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.createmod.catnip.lang.Lang;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.MobCategory;

import plus.dragons.createenchantmentindustry.EnchantmentIndustry;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.HyperExperienceBottle;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.HyperExperienceOrb;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.HyperExperienceOrbRenderer;

public class CeiEntityTypes {

    public static final EntityEntry<HyperExperienceOrb> HYPER_EXPERIENCE_ORB =
		register("hyper_experience_orb",
			HyperExperienceOrb::new,
			() -> HyperExperienceOrbRenderer::new,
			MobCategory.MISC,
			6, 20, true,
			HyperExperienceOrb::build
		).lang("Hyper Experience Orb").register();

    public static final EntityEntry<HyperExperienceBottle> HYPER_EXPERIENCE_BOTTLE =
		register("hyper_experience_bottle",
			HyperExperienceBottle::new,
			() -> ThrownItemRenderer<HyperExperienceBottle>::new,
			MobCategory.MISC,
			4, 10, true,
			HyperExperienceBottle::build
		).lang("Thrown Bottle O' Hyper Enchanting").register();

	private static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityFactory<T> factory,
		NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer,
		MobCategory group, int range, int updateFrequency, boolean sendVelocity,
		NonNullConsumer<FabricEntityTypeBuilder<T>> propertyBuilder) {
		String id = Lang.asId(name);
		EnchantmentIndustry.LOGGER.info("CEI: " + name + " got converted to " + id);
		return (CreateEntityBuilder<T, ?>) REGISTRATE
			.entity(id, factory, group)
			.properties(b -> b.trackRangeChunks(range)
				.trackedUpdateRate(updateFrequency)
				.forceTrackedVelocityUpdates(sendVelocity))
			.properties(propertyBuilder)
			.renderer(renderer);
	}

	public static void register() {
	}
}
