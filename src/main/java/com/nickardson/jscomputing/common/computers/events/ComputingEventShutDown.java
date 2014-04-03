package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ComputingEventShutDown implements IComputingEvent {

    public ComputingEventShutDown() {
        ;
    }

    @Override
    public void handle(IComputer computer) {
        for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
            ContainerTerminalComputer container = (ContainerTerminalComputer) player.openContainer;

            if (container.getComputer() == null) {
                player.closeScreen();
            }
        }
    }
}
