package com.nickardson.jscomputing.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import net.minecraft.entity.player.EntityPlayer;

public abstract class PacketText implements IPacket {

    String text;

    public PacketText() {
    }

    public PacketText(String text) {
        this.text = text;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        text = PacketUtilities.readString(bytes);
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        PacketUtilities.writeString(bytes, text);
    }

}
