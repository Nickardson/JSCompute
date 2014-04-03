package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import net.minecraft.entity.player.EntityPlayer;

public class PacketComputerInput extends PacketText {
    public PacketComputerInput() {
    }

    public PacketComputerInput(String text) {
        super(text);
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {

    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {
        if (thePlayer.openContainer instanceof ContainerTerminalComputer) {
            ContainerTerminalComputer containerTerminalComputer = (ContainerTerminalComputer) thePlayer.openContainer;

            IComputer c = containerTerminalComputer.getComputer();
            if (c != null) {
                c.eval(text);
            }
        }
    }
}
