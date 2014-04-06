package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTerminalComputer extends AbstractTileEntity {
    public static String NAME = JSComputingMod.ASSET_ID + "ComputerEntity";

    /**
     * NBT key name for Computer ID
     */
    public static String NBT_KEY_COMPUTER_ID = "compid";

    /**
     * The ID of the corresponding computer.
     * @example
     * Computers.get(computerID);
     */
    public int computerID = -1;

    /**
     * Gets the ID of the computer this TileEntity represents.
     */
    public int getComputerID() {
        return computerID;
    }

    /**
     * Changes the ID of the computer this TileEntity represents, and that of the computers themselves.
     * @param data The new ID.
     */
    public void setComputerID(int data) {
        ComputerManager.changeID(computerID, data);
        computerID = data;
        markDirty();
    }

    /**
     * Gets the computer corresponding to this TileEntity, on the relevant side.
     * @return The matching computer.
     */
    public IComputer getComputer() {
        return ComputerManager.getComputer(computerID);
    }

    /**
     * Gets the computer corresponding to this TileEntity, on the Client side.
     * @return The matching computer.
     */
    public IClientComputer getClientComputer() {
        return ComputerManager.getClientComputer(computerID);
    }

    /**
     * Gets the computer corresponding to this TileEntity, on the Server side.
     * @return The matching computer.
     */
    public IServerComputer getServerComputer() {
        return ComputerManager.getServerComputer(computerID);
    }

    boolean on;

    /**
     * Gets whether the computer is on.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Sets whether the computer is on.
     */
    public void setOn(boolean data) {
        this.on = data;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (computerID >= 0) {
            tag.setInteger(NBT_KEY_COMPUTER_ID, computerID);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey(NBT_KEY_COMPUTER_ID)) {
            setComputerID(tag.getInteger(NBT_KEY_COMPUTER_ID));
        }
    }
}
