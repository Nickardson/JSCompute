package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerOff;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Tells the computer that it must shut down.
 */
public class ComputingEventShutDown implements IComputingEvent {
    TileEntityTerminalComputer entity;

    public ComputingEventShutDown(TileEntityTerminalComputer entity) {
        this.entity = entity;
    }

    @Override
    public void handle(IServerComputer computer) {
        JSComputingMod.computorz.turnOff(entity);

        final PacketComputerOff killPacket = new PacketComputerOff(computer.getID());
        JSComputingMod.instance.eventListener.queuedActions.add(new Runnable() {
            @Override
            public void run() {
                for (EntityPlayerMP player : ComputerManager.getPlayersWithContainer(ContainerTerminalComputer.class)) {
                    ContainerTerminalComputer container = (ContainerTerminalComputer) player.openContainer;

                    if (container.getTileEntity().getServerComputer() == null) {
                        player.closeScreen();
                        ChannelHandler.sendTo(killPacket, player);
                    }
                }
            }
        });
    }
}
