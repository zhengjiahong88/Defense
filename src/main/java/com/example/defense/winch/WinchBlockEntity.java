package com.example.defense.winch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WinchBlockEntity extends BlockEntity {
    public WinchBlockEntity(BlockPos pos, BlockState state) {
        super(WinchBlockEntityType.WINCH.get(), pos, state);
    }
}