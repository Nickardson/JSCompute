package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketScreenUpdate extends PacketCharArray {

    int id = 0;

    public PacketScreenUpdate() {
    }

    public PacketScreenUpdate(int id, byte[][] array) {
        super(array);
        this.id = id;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        id = bytes.readInt();

        super.readBytes(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(id);

        super.writeBytes(bytes);
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {
        // Send updates to all players with the related computer's GUI open.
        if (thePlayer.openContainer instanceof IContainerComputer) {
            IContainerComputer container = (IContainerComputer) thePlayer.openContainer;
            IComputer computer = container.getComputer();
            if (computer instanceof IScreenedComputer) {
                if (computer.getID() == id) {
                    ((IScreenedComputer) computer).updateLines(array);
                }
            }
        }
    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {

    }
}
