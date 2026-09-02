package com.visan.visanpackcore.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry.InteractionInformation;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.simibubi.create.AllFluids", remap = false)
public class CreateAllFluidsMixin {

    @Inject(method = "registerFluidInteractions", at = @At("HEAD"), cancellable = true)
    private static void visanpackcore$overrideRegisterFluidInteractions(CallbackInfo ci) {
        // Find Create's fluids dynamically to avoid needing a compile-time dependency on Create
        Fluid honeyFluid = BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("create", "honey"));
        Fluid chocolateFluid = BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("create", "chocolate"));
        
        if (honeyFluid != null && honeyFluid != net.minecraft.world.level.material.Fluids.EMPTY) {
            // Lava flowing into Honey
            FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new InteractionInformation(
                honeyFluid.getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.STONE.defaultBlockState();
                    } else {
                        return Blocks.COBBLESTONE.defaultBlockState();
                    }
                }
            ));
            
            // Honey flowing into Lava
            FluidInteractionRegistry.addInteraction(honeyFluid.getFluidType(), new InteractionInformation(
                NeoForgeMod.LAVA_TYPE.value(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.OBSIDIAN.defaultBlockState();
                    } else {
                        return Blocks.COBBLESTONE.defaultBlockState();
                    }
                }
            ));
        }

        if (chocolateFluid != null && chocolateFluid != net.minecraft.world.level.material.Fluids.EMPTY) {
            // Lava flowing into Chocolate
            FluidInteractionRegistry.addInteraction(NeoForgeMod.LAVA_TYPE.value(), new InteractionInformation(
                chocolateFluid.getFluidType(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.STONE.defaultBlockState();
                    } else {
                        return Blocks.COBBLESTONE.defaultBlockState();
                    }
                }
            ));
            
            // Chocolate flowing into Lava
            FluidInteractionRegistry.addInteraction(chocolateFluid.getFluidType(), new InteractionInformation(
                NeoForgeMod.LAVA_TYPE.value(),
                fluidState -> {
                    if (fluidState.isSource()) {
                        return Blocks.OBSIDIAN.defaultBlockState();
                    } else {
                        return Blocks.COBBLESTONE.defaultBlockState();
                    }
                }
            ));
        }
        
        // Cancel the original method so Create doesn't register Limestone and Scoria
        ci.cancel();
    }

    @Inject(method = "getLavaInteraction", at = @At("HEAD"), cancellable = true)
    private static void visanpackcore$overrideGetLavaInteraction(FluidState fluidState, CallbackInfoReturnable<BlockState> cir) {
        Fluid fluid = fluidState.getType();
        ResourceLocation fluidName = BuiltInRegistries.FLUID.getKey(fluid);
        if (fluidName != null && fluidName.getNamespace().equals("create")) {
            String path = fluidName.getPath();
            if (path.equals("honey") || path.equals("chocolate")) {
                if (fluidState.isSource()) {
                    cir.setReturnValue(Blocks.STONE.defaultBlockState());
                } else {
                    cir.setReturnValue(Blocks.COBBLESTONE.defaultBlockState());
                }
            }
        }
    }
}
