package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IScreenedComputer;
import com.nickardson.jscomputing.common.inventory.IContainerComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketScreenUpdate extends PacketCharArray {

    int id = 0;

    boolean showCursor;

    int cursorX;
    int cursorY;

    public PacketScreenUpdate() {
    }

    public PacketScreenUpdate(int id, byte[][] array, boolean showCursor, int cursorX, int cursorY) {
        super(array);
        this.id = id;
        this.showCursor = showCursor;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        id = bytes.readInt();
        showCursor = bytes.readBoolean();
        cursorX = bytes.readInt();
        cursorY = bytes.readInt();

        super.readBytes(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        bytes.writeInt(id);
        bytes.writeBoolean(showCursor);
        bytes.writeInt(cursorX);
        bytes.writeInt(cursorY);

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
                    IScreenedComputer c = (IScreenedComputer) computer;
                    c.setCursorVisible(showCursor);
                    c.setCursor(cursorX, cursorY);
                    c.updateLines(array);
                }
            }
        }
    }

    @Override
    public void executeServer(EntityPlayer thePlayer) {

    }
}
