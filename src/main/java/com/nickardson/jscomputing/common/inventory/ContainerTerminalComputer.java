package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerTerminalComputer extends Container implements IContainerComputer {
    public IComputer getComputer() {
        return tileEntity.getComputer();
    }

    public TileEntityTerminalComputer getTileEntity() {
        return tileEntity;
    }

    protected TileEntityTerminalComputer tileEntity;

    public ContainerTerminalComputer(EntityPlayer player, TileEntityTerminalComputer computer) {
        this.tileEntity = computer;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }
}
