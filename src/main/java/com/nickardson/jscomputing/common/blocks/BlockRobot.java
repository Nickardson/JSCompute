package com.nickardson.jscomputing.common.blocks;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.tileentity.TileEntityRobot;
import com.nickardson.jscomputing.utility.BlockUtilities;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRobot extends AbstractBlockContainer {
    public static final String NAME = JSComputingMod.ASSET_ID + "Robot";

    public BlockRobot() {
        super(Material.ice);

        setCreativeTab(JSComputingMod.creativeTab)
            .setHardness(1f)
            .setStepSound(soundTypeAnvil)
            .setBlockName(NAME)
            .setBlockBounds(
                    3.0f / 16.0f,
                    6.0f / 16.0f,
                    3.0f / 16.0f,
                    13.0f / 16.0f,
                    11.0f / 16.0f,
                    13.0f / 16.0f);
    }

    public static int renderID = -1;

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, BlockUtilities.getMetadataFromYaw(player.rotationYaw), 2);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int metadata) {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityRobot();
    }
}
