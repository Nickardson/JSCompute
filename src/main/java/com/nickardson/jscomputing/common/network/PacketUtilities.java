package com.nickardson.jscomputing.common.network;

import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.io.UnsupportedEncodingException;

public class PacketUtilities {
    public static String readString(ByteBuf bytes) {
        try {
            return new String(bytes.readBytes(bytes.readableBytes()).array(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void writeString(ByteBuf bytes, String text) {
        try {
            bytes.writeBytes(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the Server-side computer the given player has open.
     * @param player The player to check.
     * @return The Server computer the player has open, or null if none.
     */
    public static IServerComputer getOpenComputer(EntityPlayer player) {
        if (player.openContainer instanceof ContainerTerminalComputer) {
            ContainerTerminalComputer containerTerminalComputer = (ContainerTerminalComputer) player.openContainer;

            return (IServerComputer) containerTerminalComputer.getComputer();
        }
        return null;
    }
}
