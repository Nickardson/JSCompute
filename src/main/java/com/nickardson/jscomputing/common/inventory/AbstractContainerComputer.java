package com.nickardson.jscomputing.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public abstract class AbstractContainerComputer extends Container implements IContainerComputer {
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return getTileEntity().getDistanceFrom(player.posX, player.posY, player.posZ) < 6 * 6;
    }
}
