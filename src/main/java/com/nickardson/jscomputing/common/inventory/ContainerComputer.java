package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.tileentity.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container {
    public TileEntityComputer getComputer() {
        return computer;
    }

    public void setComputer(TileEntityComputer computer) {
        this.computer = computer;
    }

    protected TileEntityComputer computer;

    public ContainerComputer(EntityPlayer player, TileEntityComputer computer) {
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
