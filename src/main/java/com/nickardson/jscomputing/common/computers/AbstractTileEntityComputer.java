package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.tileentity.AbstractTileEntity;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractTileEntityComputer extends AbstractTileEntity {
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

    boolean on;

    /**
     * Gets whether the computer is on.
     */
    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;

        if (!on) {
            IClientComputer clientComputer = ComputerManager.getClientComputer(getComputerID());
            if (clientComputer != null) {
                clientComputer.stop();
                ComputerManager.removeClientComputer(clientComputer.getID());
            }

            IServerComputer serverComputer = ComputerManager.getServerComputer(getComputerID());
            if (serverComputer != null) {
                serverComputer.stop();
                ComputerManager.removeServerComputer(serverComputer.getID());
            }
        }
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
