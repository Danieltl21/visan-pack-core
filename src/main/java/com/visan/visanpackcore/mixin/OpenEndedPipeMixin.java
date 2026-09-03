package com.visan.visanpackcore.mixin;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.visan.visanpackcore.VisanPackCore;

@Mixin(value = OpenEndedPipe.class, remap = false)
public class OpenEndedPipeMixin {

    @Inject(method = "provideFluidToSpace", at = @At("HEAD"), cancellable = true)
    private void visanpackcore$overrideProvideFluidToSpace(FluidStack fluid, boolean simulate,
            CallbackInfoReturnable<Boolean> cir) {
        OpenEndedPipe pipe = (OpenEndedPipe) (Object) this;
        Level level = pipe.getWorld();
        if (level == null)
            return;

        BlockPos pos = pipe.getOutputPos();
        BlockState state = level.getBlockState(pos);
        net.minecraft.world.level.material.FluidState worldFluidState = state.getFluidState();

        net.minecraft.world.level.material.Fluid spilledFluid = fluid.getFluid();
        net.minecraft.world.level.material.Fluid worldFluid = worldFluidState.getType();

        net.minecraft.resources.ResourceLocation spilledName = net.minecraft.core.registries.BuiltInRegistries.FLUID
                .getKey(spilledFluid);
        net.minecraft.resources.ResourceLocation worldName = net.minecraft.core.registries.BuiltInRegistries.FLUID
                .getKey(worldFluid);

        if (spilledName == null || worldName == null)
            return;

        // Lava pipe -> Dye world
        if (spilledName.getPath().equals("lava") && worldName.getNamespace().equals("create_dragons_plus")
                && worldName.getPath().contains("dye")) {
            if (!simulate) {
                boolean isSource = worldFluidState.isSource();
                level.setBlockAndUpdate(pos, isSource ? net.minecraft.world.level.block.Blocks.STONE.defaultBlockState()
                        : net.minecraft.world.level.block.Blocks.COBBLESTONE.defaultBlockState());
                com.visan.visanpackcore.VisanPackCore.LOGGER
                        .info("[VisanPackCore] Handled Lava pipe -> Dye world (isSource={})", isSource);
            }
            cir.setReturnValue(true);
        }
        // Dye pipe -> Lava world
        else if (spilledName.getNamespace().equals("create_dragons_plus") && spilledName.getPath().contains("dye")
                && worldName.getPath().equals("lava")) {
            if (!simulate) {
                boolean isSource = worldFluidState.isSource();
                level.setBlockAndUpdate(pos,
                        isSource ? net.minecraft.world.level.block.Blocks.OBSIDIAN.defaultBlockState()
                                : net.minecraft.world.level.block.Blocks.COBBLESTONE.defaultBlockState());
                com.visan.visanpackcore.VisanPackCore.LOGGER
                        .info("[VisanPackCore] Handled Dye pipe -> Lava world (isSource={})", isSource);
            }
            cir.setReturnValue(true);
        }
        // Lava pipe -> Dragon's breath world
        else if (spilledName.getPath().equals("lava") && worldName.getNamespace().equals("create_dragons_plus")
                && worldName.getPath().contains("dragon_breath")) {
            if (!simulate) {
                boolean isSource = worldFluidState.isSource();
                level.setBlockAndUpdate(pos,
                        isSource ? net.minecraft.world.level.block.Blocks.OBSIDIAN.defaultBlockState()
                                : net.minecraft.world.level.block.Blocks.END_STONE.defaultBlockState());
                com.visan.visanpackcore.VisanPackCore.LOGGER
                        .info("[VisanPackCore] Handled Lava pipe -> Dragon's Breath world (isSource={})", isSource);
            }
            cir.setReturnValue(true);
        }
    }
}
