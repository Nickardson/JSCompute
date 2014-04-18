package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketCursorUpdate implements IPacket {

    int id;

    boolean cursorVisible;

    int cursorX;
    int cursorY;

    public PacketCursorUpdate() {
    }

    public PacketCursorUpdate(int id, boolean cursorVisible, int cursorX, int cursorY) {
        this.id = id;
        this.cursorVisible = cursorVisible;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        this.id = bytes.readInt();
        this.cursorVisible = bytes.readBoolean();
        if (cursorVisible) {
            this.cursorX = bytes.readInt();
            this.cursorY = bytes.readInt();
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(id);
        bytes.writeBoolean(cursorVisible);
        if (cursorVisible) {
            bytes.writeInt(cursorX);
            bytes.writeInt(cursorY);
        }
    }

    @Override
    public void executeClient(EntityPlayer thePlayer) {
        // Send updates to all players with the related computer's GUI open.
        if (thePlayer.openContainer instanceof IContainerComputer) {
            IContainerComputer container = (IContainerComputer) thePlayer.openContainer;
            IComputer computer = container.getComputer();
            if (computer instanceof IScreenedComputer) {
                if (computer.getID() == id) {
                    IScreenedComputer c = (IScreenedComputer) computer;
                    c.setCursorVisible(cursorVisible);
                    if (cursorVisible) {
                        c.setCursor(cursorX, cursorY);
                    }
                }
            }
        }
    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {

    }
}
