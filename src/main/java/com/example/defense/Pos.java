package com.example.defense;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public class Pos {
    private final int X;
    private final int Y;

    public Pos(int x, int y) {
        X = x;
        Y = y;
    }

    public Pos(BlockPos blockPos, BlockPos origin) {
        Vec3i correction = blockPos.subtract(origin);
        X = correction.getX();
        Y = correction.getY();
    }

    public boolean inRange(Pos size) {
        return 0 <= X && X < size.X && 0 <= Y && Y < size.Y;
    }

    public boolean recorded(boolean[][] records) {
        return records[X][Y];
    }

    public void record(boolean[][] records) {
        records[X][Y] = true;
    }

    public BlockPos block(BlockPos origin) {
        return origin.offset(X, Y, 0);
    }
}
