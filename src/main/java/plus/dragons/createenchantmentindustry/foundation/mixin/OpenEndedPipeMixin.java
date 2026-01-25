package plus.dragons.createenchantmentindustry.foundation.mixin;

import com.simibubi.create.content.fluids.OpenEndedPipe;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.level.PonderLevel;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience.ExperienceFluid;
import plus.dragons.createenchantmentindustry.entry.CeiFluids;
import plus.dragons.createenchantmentindustry.foundation.advancement.CeiAdvancements;


@Mixin(value = OpenEndedPipe.class)
public class OpenEndedPipeMixin {

	@Shadow(remap = false)
	private Level world;

	@Shadow(remap = false)
	private BlockPos outputPos;

	@Shadow(remap = false)
	private AABB aoe;

	@Shadow(remap = false)
	private BlockPos pos;


	@Inject(method = "provideFluidToSpace", at = @At("HEAD"), cancellable = true, remap = false)
	private void inject(FluidStack fluid, TransactionContext ctx, CallbackInfoReturnable<Boolean> cir){
		if(fluid.getFluid().isSame(CeiFluids.EXPERIENCE.get()) || fluid.getFluid().isSame(CeiFluids.HYPER_EXPERIENCE.get())){
			if (world != null && world.isLoaded(this.outputPos)) {
				if (world instanceof PonderLevel){
					var speed = new Vec3(outputPos.getX() - pos.getX() + Math.random() * 0.1,
							outputPos.getY() - pos.getY() + Math.random() * 0.1,
							outputPos.getZ() - pos.getZ() + Math.random() * 0.1).scale(0.2);
					var orbPos = VecHelper.getCenterOf(outputPos);
					var orb = new ExperienceOrb(world, orbPos.x, orbPos.y, orbPos.z, 1);
					orb.setDeltaMovement(speed);
					world.addFreshEntity(orb);
					var pipeSource = (FluidTank) ((OpenEndedPipe) (Object) this).provideHandler();
					pipeSource.getFluid().setAmount(0);
					cir.setReturnValue(true);
				}
				if (!(world instanceof ServerLevel slevel))
					return;

				var players = world.getEntitiesOfClass(Player.class, aoe, LivingEntity::isAlive);
				var speed = new Vec3(outputPos.getX() - pos.getX(),
						outputPos.getY() - pos.getY(),
						outputPos.getZ() - pos.getZ()).scale(0.2);
				var orbPos = VecHelper.getCenterOf(outputPos);
				ExperienceFluid expfluid = (ExperienceFluid) fluid.getFluid();
				int amount = Math.toIntExact(fluid.getAmount());
				if (players.isEmpty()) {
					expfluid.awardOrDrop(null, slevel, orbPos, speed, amount);
				} else {
					int partial = amount / players.size();
					int left = amount % players.size();
					players.forEach(player -> {
						CeiAdvancements.A_SHOWER_EXPERIENCE.getTrigger().trigger((ServerPlayer) player);
						expfluid.awardOrDrop(player, slevel, orbPos, speed, partial);
					});
					if (left != 0) {
						var lucky = players.get(world.random.nextInt(players.size()));
						expfluid.awardOrDrop(lucky, slevel, orbPos, speed, left);
					}
				}
				var pipeSource = (FluidTank) ((OpenEndedPipe) (Object) this).provideHandler();
				pipeSource.getFluid().setAmount(0);
				cir.setReturnValue(true);
			}
		}
	}
}
