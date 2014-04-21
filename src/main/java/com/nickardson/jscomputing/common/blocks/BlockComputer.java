package com.nickardson.jscomputing.common.blocks;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.GuiHandler;
import com.nickardson.jscomputing.common.computers.*;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.utility.BlockUtilities;
import com.nickardson.jscomputing.utility.NetworkUtilities;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockComputer extends AbstractBlockContainer {
    public static String NAME = JSComputingMod.ASSET_ID + "Computer";

    @SideOnly(Side.CLIENT)
    protected IIcon blockIconFront;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconFrontOn;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconSide;

    public BlockComputer() {
        super(Material.iron);

        setCreativeTab(JSComputingMod.creativeTab)
            .setHardness(1f)
            .setStepSound(soundTypePiston)
            .setBlockName(NAME);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIconFront = register.registerIcon(JSComputingMod.ASSET_ID + ":" + NAME);
        blockIconFrontOn = register.registerIcon(JSComputingMod.ASSET_ID + ":" + NAME + "On");
        blockIconSide = register.registerIcon(JSComputingMod.ASSET_ID + ":" + NAME + "Side");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata) {
        boolean on = false;
        if (metadata >= 6) {
            metadata -= 6;
            on = true;
        } else {
            // We are in hand, or in inventory.
            if (metadata == 0) {
                metadata = 3;
                on = true;
            }
        }

        if (side == metadata) {
            return on ? blockIconFrontOn : blockIconFront;
        } else {
            return blockIconSide;
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float subX, float subY, float subZ, int wat) {
        return super.onBlockPlaced(world, x, y, z, side, subX, subY, subZ, wat);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        world.setBlockMetadataWithNotify(x, y, z, BlockUtilities.getMetadataFromYaw(player.rotationYaw), 2);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float relX, float relY, float relZ) {
        TileEntityTerminalComputer entity = (TileEntityTerminalComputer) getTileEntity(world, x, y, z);

        if (entity == null) {
            return false;
        }

        if (!entity.isOn()) {
            entity.setOn(true);

            if (entity.getBlockMetadata() < 6) {
                entity.setBlockMetadata(entity.getBlockMetadata() + 6);
            }
        }

        player.openGui(JSComputingMod.instance, GuiHandler.GUI_TERMINAL_COMPUTER, world, x, y, z);

        IServerComputer serverComputer = entity.getServerComputer();
        if (serverComputer != null && NetworkUtilities.isServerWorld(world)) {
            serverComputer.onPlayerOpenGui();
            return true;
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityTerminalComputer();
    }
}
