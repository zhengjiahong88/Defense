package com.example.defense;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Queue;

public class WinchBlock extends Block implements EntityBlock {
    public static final IntegerProperty CHAIN_LENGTH = IntegerProperty.create("chain_length", 0, 6);
    public WinchBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(CHAIN_LENGTH, 0));
    }

    private boolean isChain(BlockState blockState) {
        return blockState.is(Blocks.CHAIN) && blockState.getValue(ChainBlock.AXIS) == Direction.Axis.Y;
    }

    private BlockPos targetPos(boolean b, BlockPos blockPos) {
        return b ? blockPos.above() : blockPos.below();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHAIN_LENGTH);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 16);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        level.scheduleTick(pos, this, 16);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof WinchBlockEntity winch)) return;

        boolean powered = level.hasNeighborSignal(pos);
        BlockPos below = pos.below();
        BlockState chain = level.getBlockState(below);
        if (powered && isChain(chain) && winch.getChainLength() < 6 || !powered && winch.getChainLength() > 0) {
            while (isChain(chain)) {
                below = below.below();
                chain = level.getBlockState(below);
            }
            boolean connection = chain.is(Blocks.IRON_BARS);
            if (connection) {
                Queue<Direction> directions = new LinkedList<>();
                directions.offer(Direction.UP);
                directions.offer(Direction.DOWN);
                directions.offer(Direction.WEST);
                directions.offer(Direction.EAST);
                Queue<BlockPos> bars = new LinkedList<>();
                bars.offer(below);
                boolean[][] records = new boolean[5][7];
                BlockPos origin = below.offset(-2, -6, 0);
                Pos size = new Pos(5, 7);
                new Pos(2, 6).record(records);
                while (!bars.isEmpty()) {
                    BlockPos bar = bars.poll();
                    for (Direction direction : directions) {
                        BlockPos newBlockPos = bar.relative(direction);
                        Pos newPos = new Pos(newBlockPos, origin);
                        if (newPos.inRange(size) && !newPos.recorded(records) && level.getBlockState(newBlockPos).is(Blocks.IRON_BARS)) {
                            BlockState target = level.getBlockState(targetPos(powered, newBlockPos));
                            if (target.is(Blocks.IRON_BARS) || target.is(Blocks.AIR)) {
                                newPos.record(records);
                                bars.offer(newBlockPos);
                            } else return;
                        }
                    }
                }
                for (int x = 0; x < 5; x++) for (int y = powered ? 6 : 0; powered ? y >= 0 : y < 7; y += powered ? -1 : 1) {
                    Pos newPos = new Pos(x, y);
                    if (newPos.recorded(records)) {
                        BlockPos newBlockPos = newPos.block(origin);
                        Pos target = new Pos(x, y + (powered ? 1 : -1));
                        if (!target.inRange(size) || !target.recorded(records))
                            level.setBlock(targetPos(powered, newBlockPos), level.getBlockState(newBlockPos), 3);
                        target = new Pos(x, y + (powered ? -1 : 1));
                        if (!target.inRange(size) || !target.recorded(records))
                            level.setBlock(newBlockPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            } else if (!powered && !chain.is(Blocks.AIR)) return;
            if (!powered) level.setBlock(below, Blocks.CHAIN.defaultBlockState().setValue(ChainBlock.AXIS, Direction.Axis.Y), 3);
            else if (!connection) level.setBlock(below.above(), Blocks.AIR.defaultBlockState(), 3);
            level.playSound(null, pos.below(), powered ? SoundEvents.CHAIN_BREAK : SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (powered) winch.add();
            else winch.sub();
            level.setBlock(pos, state.setValue(CHAIN_LENGTH, winch.getChainLength()), 3);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WinchBlockEntity(pos, state);
    }
}
