package com.nickardson.jscomputing.common;

import com.nickardson.jscomputing.client.gui.GuiTerminalComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    /**
     * The GUI of a TileEntityTerminalComputer.
     */
    public static final int GUI_TERMINAL_COMPUTER = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity e = world.getTileEntity(x, y, z);

        switch (ID) {
            case GUI_TERMINAL_COMPUTER:
                return new ContainerTerminalComputer((TileEntityTerminalComputer) e);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity e = world.getTileEntity(x, y, z);

        switch (ID) {
            case GUI_TERMINAL_COMPUTER:
                return new GuiTerminalComputer((TileEntityTerminalComputer) e);
            default:
                return null;
        }
    }
}
