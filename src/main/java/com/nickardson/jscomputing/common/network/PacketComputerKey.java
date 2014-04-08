package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IKeyboardableComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketComputerKey implements IPacket {

    int key;
    char character;
    boolean state;

    @SuppressWarnings("UnusedDeclaration")
    public PacketComputerKey() {
    }

    public PacketComputerKey(int key, char character, boolean state) {
        this.key = key;
        this.character = character;
        this.state = state;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        key = bytes.readInt();
        character = bytes.readChar();
        state = bytes.readBoolean();
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(key);
        bytes.writeChar(character);
        bytes.writeBoolean(state);
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {

    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {
        IServerComputer computer = PacketUtilities.getOpenComputer(thePlayer);
        if (computer instanceof IKeyboardableComputer) {
            ((IKeyboardableComputer) computer).onKey(key, character, state);
        }
    }
}
