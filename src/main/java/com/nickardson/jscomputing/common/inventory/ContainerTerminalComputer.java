package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IComputer;
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

    public ContainerTerminalComputer(TileEntityTerminalComputer computer) {
        this.tileEntity = computer;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.getDistanceFrom(player.posX, player.posY, player.posZ) < 6 * 6;
    }
}
