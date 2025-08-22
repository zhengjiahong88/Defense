package com.example.defense;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "defense");

    public static final RegistryObject<BlockEntityType<WinchBlockEntity>> WINCH = BLOCK_ENTITIES.register("winch", () -> BlockEntityType.Builder.of(WinchBlockEntity::new, Defense.WINCH.get()).build(null));
}
