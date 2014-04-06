package com.nickardson.jscomputing.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractTileEntity extends TileEntity {
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    /**
     * Sends a block update
     */
    public void update() {
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * Sends a block update
     * 1 - Notifies neighbors of block change
     * 2 - Marks the block to be updated
     * 4 - Sets the block ID or metadata
     *
     * 1 | 2 = 3 - Marks the block to be updated AND Notifies neighbors of block change
     *
     */
    public void setBlockMetadata(int meta) {
        getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 2);
    }
}
