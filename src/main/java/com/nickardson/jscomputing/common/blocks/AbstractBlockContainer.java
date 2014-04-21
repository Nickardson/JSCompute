package com.nickardson.jscomputing.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * A block, which has a TileEntity.
 */
public abstract class AbstractBlockContainer extends BlockContainer {
    protected AbstractBlockContainer(Material material) {
        super(material);
    }

    public TileEntity getTileEntity(World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity != null) {
            return entity;
        }
        return null;
    }
}
