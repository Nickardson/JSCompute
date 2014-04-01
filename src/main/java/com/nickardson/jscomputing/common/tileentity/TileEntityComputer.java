package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComputer extends AbstractTileEntity {
    public static String NAME = JSComputingMod.ASSET_ID + "ComputerEntity";

    public static String NBT_KEY_COMPUTER_ID = "compid";

    /**
     * The ID of the corresponding computer.
     * @example
     * Computers.get(computerID);
     */
    public int tempID = 0;

    public TileEntityComputer() {

    }

    int computerID;
    public int getComputerID() {
        return computerID;
    }
    public void setComputerID(int data) {
        computerID = data;
        markDirty();
    }

    boolean on;
    public boolean isOn() {
        return on;
    }
    public void setOn(boolean data) {
        this.on = data;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger(NBT_KEY_COMPUTER_ID, computerID);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        computerID = tag.getInteger(NBT_KEY_COMPUTER_ID);
    }
}
