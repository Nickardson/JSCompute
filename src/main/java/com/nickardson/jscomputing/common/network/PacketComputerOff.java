package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketComputerOff implements IPacket {

    private int id;

    public PacketComputerOff() {
    }

    public PacketComputerOff(int id) {
        this.id = id;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        id = bytes.readInt();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(id);
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {
        //ComputerManager.getClientComputer(id).stop();
        //ComputerManager.getClientComputers().remove(id);
    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {

    }
}
