package plus.dragons.createenchantmentindustry.content.contraptions.fluids.experience;

import static plus.dragons.createenchantmentindustry.EnchantmentIndustry.UNIT_PER_MB;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.fluids.VirtualFluid;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExperienceFluid extends VirtualFluid {

    protected final int xpRatio;

    public ExperienceFluid(int xpRatio, Properties properties, boolean source) {
        super(properties, source);
        this.xpRatio = xpRatio;
    }

    public ExperienceFluid(Properties properties, boolean source) {
        this(1, properties, source);
    }

    public ExperienceOrb convertToOrb(Level level, double x, double y, double z, int fluidAmount) {
        return new ExperienceOrb(level, x, y, z, fluidAmount);
    }

    public void drop(ServerLevel level, Vec3 pos, int realFluidAmount) {
		var fluidAmount = realFluidAmount / UNIT_PER_MB;
        while(fluidAmount > 0) {
            int orbSize = ExperienceOrb.getExperienceValue(fluidAmount);
            fluidAmount -= orbSize;
            if (!ExperienceOrb.tryMergeToExisting(level, pos, orbSize)) {
                level.addFreshEntity(this.convertToOrb(level, pos.x, pos.y, pos.z, orbSize));
            }
        }
    }

    public void awardOrDrop(@Nullable Player player, ServerLevel level, Vec3 pos, Vec3 speed, int realAmount) {
		var amount = realAmount / UNIT_PER_MB;
        var orb = this.convertToOrb(level, pos.x, pos.y, pos.z, amount);
        if (player == null) {
            if (!ExperienceOrb.tryMergeToExisting(level, pos, orb.value)) {
                orb.setDeltaMovement(speed);
                level.addFreshEntity(orb);
            }
        } else {
            int left = orb.repairPlayerItems(player, orb.value);
            if (left > 0) {
                player.giveExperiencePoints(left);
                this.applyAdditionalEffects(player, left);
            }
        }
    }

    public void applyAdditionalEffects(LivingEntity entity, int expAmount) {

    }

    public int getXpRatio() {
        return xpRatio;
    }

}
