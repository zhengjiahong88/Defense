package com.example.defense.winch;

import com.example.defense.Defense;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WinchBlockEntityType {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Defense.MODID);

    public static final RegistryObject<BlockEntityType<WinchBlockEntity>> WINCH = BLOCK_ENTITY_TYPES.register("winch", () -> BlockEntityType.Builder.of(WinchBlockEntity::new, Defense.WINCH.get()).build(null));
}