package com.nickardson.jscomputing.common.blocks;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.JavaScriptComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockComputer extends AbstractBlockContainer {
    public static String NAME = JSComputingMod.ASSET_ID + "Computer";

    @SideOnly(Side.CLIENT)
    protected IIcon blockIconFront;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconFrontOn;
    @SideOnly(Side.CLIENT)
    protected IIcon blockIconSide;

    public BlockComputer() {
        super(Material.piston);

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
        int look = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        ForgeDirection direction;
        switch (look)
        {
            case 0:
                direction = ForgeDirection.NORTH; break;
            case 1:
                direction = ForgeDirection.EAST; break;
            case 2:
                direction = ForgeDirection.SOUTH; break;
            case 3:
                direction = ForgeDirection.WEST; break;
            default:
                direction = ForgeDirection.NORTH;
        }

        world.setBlockMetadataWithNotify(x, y, z, direction.ordinal(), 2);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float relX, float relY, float relZ) {
        TileEntityComputer entity = (TileEntityComputer) getTileEntity(world, x, y, z);
        if (entity != null) {
            if (!entity.isOn()) {
                turnOn(entity);
            }
            player.openGui(JSComputingMod.instance, 0, world, x, y, z);
            return true;
        } else {
            return false;
        }
    }

    public void turnOn(TileEntityComputer entity) {
        if (!entity.isOn()) {
            entity.setOn(true);

            if (entity.getBlockMetadata() < 6) {
                entity.setBlockMetadata(entity.getBlockMetadata() + 6);
            }

            IComputer computer = new JavaScriptComputer(0, entity);
            entity.tempID = computer.getTempID();
            entity.setComputerID(computer.getID());
            computer.init();
        }
    }

    public void turnOff(TileEntityComputer entity) {
        if (entity.isOn()) {
            entity.setOn(false);

            if (entity.getBlockMetadata() >= 6) {
                entity.setBlockMetadata(entity.getBlockMetadata() - 6);
            }

            entity.getComputer().close();
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        System.out.println("Update");
        TileEntity e = getTileEntity(world, x, y, z);
        if (e != null && e instanceof TileEntityComputer) {
            TileEntityComputer computer = ((TileEntityComputer) e);

            if (computer.getBlockMetadata() >= 6) {
                computer.setBlockMetadata(computer.getBlockMetadata() - 6);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityComputer();
    }
}
