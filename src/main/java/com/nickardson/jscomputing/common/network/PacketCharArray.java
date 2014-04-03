package com.nickardson.jscomputing.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public abstract class PacketCharArray implements IPacket {
    int width = 0;
    int height = 0;

    char[][] array;

    public PacketCharArray() {
    }

    public PacketCharArray(char[][] array) {
        this.array = array;
        this.width = array[0].length;
        this.height = array.length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char[][] getArray() {
        return array;
    }

    @Override
    public void readBytes(ByteBuf bytes) {
        width = bytes.readByte();
        height = bytes.readByte();

        array = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                array[y][x] = bytes.readChar();
            }
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes) {
        width = array[0].length;
        height = array.length;

        bytes.writeByte(width);
        bytes.writeByte(height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bytes.writeChar(array[y][x]);
            }
        }
    }
}
