package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IServerComputer;
import net.minecraft.entity.player.EntityPlayer;

public class PacketComputerInput extends PacketText {
    @SuppressWarnings("UnusedDeclaration")
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
        IServerComputer computer = PacketUtilities.getOpenComputer(thePlayer);
        if (computer != null) {
            computer.input(text);
        }
    }
}
