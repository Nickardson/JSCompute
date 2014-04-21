package com.nickardson.jscomputing.common.computers.events;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import com.nickardson.jscomputing.common.network.ChannelHandler;
import com.nickardson.jscomputing.common.network.PacketComputerOff;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import com.nickardson.jscomputing.utility.NetworkUtilities;
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
        entity.setOn(false);

        final PacketComputerOff packet = new PacketComputerOff(computer.getID());

        JSComputingMod.instance.eventListener.queue(new Runnable() {
            @Override
            public void run() {
                // If the server computer for the player's open container doesn't exist, send the kill packet to the client.
                for (EntityPlayerMP player : NetworkUtilities.getPlayersWithContainer(IContainerComputer.class)) {
                    if (((IContainerComputer) player.openContainer).getComputer() == null) {
                        player.closeScreen();
                        ChannelHandler.sendTo(packet, player);
                    }
                }
            }
        });
    }
}
