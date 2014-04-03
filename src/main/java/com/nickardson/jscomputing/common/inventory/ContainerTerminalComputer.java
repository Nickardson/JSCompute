package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerTerminalComputer extends Container implements IContainerComputer {
    public IComputer getComputer() {
        return computer.getComputer();
    }

    public TileEntityTerminalComputer getTileEntity() {
        return computer;
    }

    protected TileEntityTerminalComputer computer;

    public ContainerTerminalComputer(EntityPlayer player, TileEntityTerminalComputer computer) {
        this.computer = computer;
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return computer.isUseableByPlayer(player);
    }
}
