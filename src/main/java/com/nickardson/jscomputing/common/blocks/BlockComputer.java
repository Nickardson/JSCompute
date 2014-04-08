package com.nickardson.jscomputing.common.blocks;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.GuiHandler;
import com.nickardson.jscomputing.common.computers.*;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
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
        TileEntityTerminalComputer entity = (TileEntityTerminalComputer) getTileEntity(world, x, y, z);

        if (entity != null) {
            if (!entity.isOn()) {
                turnOn(entity);
            }

            player.openGui(JSComputingMod.instance, GuiHandler.GUI_TERMINAL_COMPUTER, world, x, y, z);

            IServerComputer serverComputer = entity.getServerComputer();
            if (serverComputer != null && !world.isRemote) {
                serverComputer.onPlayerOpenGui();
                return true;
            }
        }
        return false;
    }

    /**
     * Turns this computer on, creating a new computer object.
     * @param entity The TileEntity representing the computer.
     */
    public void turnOn(TileEntityTerminalComputer entity) {
        entity.setOn(true);

        if (entity.getBlockMetadata() < 6) {
            entity.setBlockMetadata(entity.getBlockMetadata() + 6);
        }

        IComputer computer;

        int nextID = entity.getComputerID();
        if (!entity.getWorldObj().isRemote) {
            if (nextID == -1) {
                nextID = ComputerManager.getNextAvailableID();
            }
            computer = new ServerTerminalComputer(nextID, entity);
            entity.update();
        } else {
            computer = new ClientTerminalComputer(entity);
            // TODO: Set computer id
        }
        entity.setComputerID(computer.getID());
        computer.start();
    }

    /**
     * Turns this computer off, removing the existing computer if any.
     * @param entity The TileEntity representing the computer.
     */
    public void turnOff(TileEntityTerminalComputer entity) {
        entity.setOn(false);

        IClientComputer clientComputer = ComputerManager.getClientComputer(entity.getComputerID());
        if (clientComputer != null) {
            clientComputer.stop();
            ComputerManager.removeClientComputer(clientComputer.getID());
        }

        IServerComputer serverComputer = ComputerManager.getServerComputer(entity.getComputerID());
        if (serverComputer != null) {
            serverComputer.stop();
            ComputerManager.removeServerComputer(serverComputer.getID());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTerminalComputer();
    }
}
