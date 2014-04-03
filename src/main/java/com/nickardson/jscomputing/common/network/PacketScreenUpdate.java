package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import net.minecraft.entity.player.EntityPlayer;

public class PacketScreenUpdate extends PacketCharArray {

    public PacketScreenUpdate() {
    }

    public PacketScreenUpdate(char[][] array) {
        this.array = array;
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {
        if (thePlayer.openContainer instanceof IContainerComputer) {
            IContainerComputer container = (IContainerComputer) thePlayer.openContainer;
            if (container.getComputer() instanceof IScreenedComputer) {
                ((IScreenedComputer) container.getComputer()).setLines(array);
            }
        }
    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {

    }
}
