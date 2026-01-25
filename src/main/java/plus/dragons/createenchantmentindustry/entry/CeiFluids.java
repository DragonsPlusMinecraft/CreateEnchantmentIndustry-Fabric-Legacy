package plus.dragons.createenchantmentindustry.entry;

import static net.minecraft.world.item.Items.BUCKET;
import static net.minecraft.world.item.Items.GLASS_BOTTLE;
import static plus.dragons.createenchantmentindustry.EnchantmentIndustry.REGISTRATE;
import static plus.dragons.createenchantmentindustry.EnchantmentIndustry.UNIT_PER_MB;

import javax.annotation.Nullable;

import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import plus.dragons.createdragonlib.fluid.FluidLavaReaction;
import plus.dragons.createenchantmentindustry.EnchantmentIndustry;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.ExperienceFluid;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.HyperExperienceFluid;

public class CeiFluids {

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB.key());
    }

    public static final ResourceLocation EXPERIENCE_STILL_RL = EnchantmentIndustry.genRL("fluid/experience_still");
    public static final ResourceLocation EXPERIENCE_FLOW_RL = EnchantmentIndustry.genRL("fluid/experience_flow");

    public static final FluidEntry<ExperienceFluid> EXPERIENCE = REGISTRATE.virtualFluid("experience",
            EXPERIENCE_STILL_RL, EXPERIENCE_FLOW_RL,
			properties -> new ExperienceFluid(properties, true),
			properties -> new ExperienceFluid(properties, false))
            .lang("Liquid Experience")
			.fluidAttributes(()->new FluidVariantAttributeHandler(){
				@Override
				public Component getName(FluidVariant fluidVariant) {
					return Component.translatable("fluid.create_enchantment_industry.experience");
				}
				@Override
				public int getLuminance(FluidVariant variant) {
					return 15;
				}
			})
            .tag(CeiTags.FluidTag.BLAZE_ENCHANTER_INPUT.tag, CeiTags.FluidTag.PRINTER_INPUT.tag)
			.onRegisterAfter(Registries.ITEM, exp -> {
				Fluid source = exp.getSource();
				FluidStorage.combinedItemApiProvider(Items.EXPERIENCE_BOTTLE).register(context ->
						new FullItemFluidStorage(context, bucket -> ItemVariant.of(GLASS_BOTTLE), FluidVariant.of(source), 10 * UNIT_PER_MB));
			})
            .register();

    public static final ResourceLocation HYPER_EXPERIENCE_STILL_RL = EnchantmentIndustry.genRL("fluid/hyper_experience_still");
    public static final ResourceLocation HYPER_EXPERIENCE_FLOW_RL = EnchantmentIndustry.genRL("fluid/hyper_experience_flow");

    public static final FluidEntry<HyperExperienceFluid> HYPER_EXPERIENCE = REGISTRATE.virtualFluid("hyper_experience",
            HYPER_EXPERIENCE_STILL_RL, HYPER_EXPERIENCE_FLOW_RL,
			properties -> new HyperExperienceFluid(properties, true),
			properties -> new HyperExperienceFluid(properties, false))
            .lang("Liquid Hyper Experience")
			.fluidAttributes(()->new FluidVariantAttributeHandler(){
				@Override
				public Component getName(FluidVariant fluidVariant) {
					return Component.translatable("fluid.create_enchantment_industry.hyper_experience");
				}
				@Override
				public int getLuminance(FluidVariant variant) {
					return 15;
				}
			})
            .tag(CeiTags.FluidTag.BLAZE_ENCHANTER_INPUT.tag, CeiTags.FluidTag.PRINTER_INPUT.tag)
			.onRegisterAfter(Registries.ITEM, hyperExp -> {
				Fluid source = hyperExp.getSource();
				FluidStorage.combinedItemApiProvider(CeiItems.HYPER_EXP_BOTTLE.get()).register(context ->
						new FullItemFluidStorage(context, bucket -> ItemVariant.of(GLASS_BOTTLE), FluidVariant.of(source), 10 * UNIT_PER_MB));
			})
            .register();

    public static final ResourceLocation INK_STILL_RL = EnchantmentIndustry.genRL("fluid/ink_still");
    public static final ResourceLocation INK_FLOW_RL = EnchantmentIndustry.genRL("fluid/ink_flow");

    public static final FluidEntry<SimpleFlowableFluid.Flowing> INK = REGISTRATE
            .fluid("ink", INK_STILL_RL, INK_FLOW_RL)
			.lang("Ink")
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.flowSpeed(4)
					.blastResistance(100f))
            .fluidAttributes(()->new FluidVariantAttributeHandler(){
				@Override
				public Component getName(FluidVariant fluidVariant) {
					return Component.translatable("fluid.create_enchantment_industry.ink");
				}
				@Override
				public int getViscosity(FluidVariant variant, @Nullable Level world) {
					return 1000;
				}
				@Override
				public boolean isLighterThanAir(FluidVariant variant) {
					return false;
				}
			})
            .tag(CeiTags.FluidTag.INK.tag, CeiTags.FluidTag.PRINTER_INPUT.tag, FluidTags.WATER)
            .source(SimpleFlowableFluid.Source::new)
            .bucket()
            .build()
			.onRegisterAfter(Registries.ITEM, ink -> {
				Fluid source = ink.getSource();
				FluidStorage.combinedItemApiProvider(source.getBucket()).register(context ->
						new FullItemFluidStorage(context, bucket -> ItemVariant.of(BUCKET), FluidVariant.of(source), FluidConstants.BUCKET));
				FluidStorage.combinedItemApiProvider(Items.BUCKET).register(context ->
						new EmptyItemFluidStorage(context, bucket -> ItemVariant.of(source.getBucket()), source, FluidConstants.BUCKET));
			})
            .register();

    public static void register() {
    }

    public static void handleInkEffect(ServerLevel world) {
		for(var entity: world.getAllEntities()){
			if(entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && !livingEntity.isSpectator()){
				if (entity.tickCount % 20 != 0) return;
				if (livingEntity.isEyeInFluid(CeiTags.FluidTag.INK.tag)) {
					livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, true, false, false));
				}
			}
		}
    }

    public static void registerLavaReaction() {
        FluidLavaReaction.register(FluidVariant.of(INK.get()),
            Blocks.OBSIDIAN.defaultBlockState(),
            Blocks.BLACKSTONE.defaultBlockState(),
            Blocks.BLACKSTONE.defaultBlockState()
        );
    }
}
