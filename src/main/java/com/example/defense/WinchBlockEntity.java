package com.example.defense;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WinchBlockEntity extends BlockEntity {
    private int chainLength;

    public WinchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WINCH.get(), pos, state); // 這裡要用登記好的 BlockEntityType
        chainLength = 0;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ChainLength", chainLength);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        chainLength = tag.getInt("ChainLength");
    }

    public int getChainLength() {
        return chainLength;
    }

    public void add() {
        chainLength++;
        setChanged();
    }

    public void sub() {
        chainLength--;
        setChanged();
    }
}
