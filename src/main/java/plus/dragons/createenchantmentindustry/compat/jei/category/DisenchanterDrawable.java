package plus.dragons.createenchantmentindustry.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.gui.CustomLightingSettings;

import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import mezz.jei.api.gui.drawable.IDrawable;
import net.createmod.catnip.gui.ILightingSettings;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import plus.dragons.createenchantmentindustry.entry.CeiBlocks;

import static com.simibubi.create.compat.jei.category.animations.AnimatedKinetics.defaultBlockElement;

public class DisenchanterDrawable implements IDrawable {
    public static final ILightingSettings DEFAULT_LIGHTING = CustomLightingSettings.builder()
            .firstLightRotation(12.5f, 45.0f)
            .secondLightRotation(-20.0f, 50.0f)
            .build();


    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        var matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset + 20, 100);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        GuiGameElement.of(CeiBlocks.DISENCHANTER.getDefaultState())
                .lighting(DEFAULT_LIGHTING)
                .scale(20)
                .render(guiGraphics);
        matrixStack.popPose();
    }
}
