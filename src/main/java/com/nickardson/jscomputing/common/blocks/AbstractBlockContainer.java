package com.nickardson.jscomputing.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class AbstractBlockContainer extends BlockContainer {
    protected AbstractBlockContainer(Material material) {
        super(material);
    }

    public TileEntity getTileEntity(World world, int x, int y, int z) {
        TileEntity e = world.getTileEntity(x, y, z);
        if (e != null) {
            return e;
        } else {
            return null;
        }
    }
}
