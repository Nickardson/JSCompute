package com.nickardson.jscomputing.common.network;

import io.netty.buffer.ByteBuf;

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

}
