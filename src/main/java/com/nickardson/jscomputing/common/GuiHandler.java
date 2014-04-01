package com.nickardson.jscomputing.common;

import com.nickardson.jscomputing.client.gui.GuiComputer;
import com.nickardson.jscomputing.common.inventory.ContainerComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity e = world.getTileEntity(x, y, z);
        if (e instanceof TileEntityComputer) {
            return new ContainerComputer(player, (TileEntityComputer) e);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity e = world.getTileEntity(x, y, z);
        if (e instanceof TileEntityComputer) {
            return new GuiComputer(player, (TileEntityComputer) e);
        }
        return null;
    }
}
