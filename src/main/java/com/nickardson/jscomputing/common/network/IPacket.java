package com.nickardson.jscomputing.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {
    public void readBytes(ByteBuf bytes);
    public void writeBytes(ByteBuf bytes);

    public void executeClient(EntityPlayer thePlayer);
    public void executeServer(EntityPlayer thePlayer);
}
