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

    public TileEntityTerminalComputer() {
    }

    public int getComputerID() {
        return computerID;
    }
    public void setComputerID(int data) {
        if (data == computerID) {
            return;
        }

        IClientComputer client = ComputerManager.getClientComputer(computerID);
        IServerComputer server = ComputerManager.getServerComputer(computerID);

        ComputerManager.removeComputer(computerID);

        computerID = data;

        if (client != null) {
            client.setID(computerID);
            ComputerManager.addClientComputer(client);
        }
        if (server != null) {
            server.setID(computerID);
            ComputerManager.addServerComputer(server);
        }

        markDirty();
    }

    public IComputer getComputer() {
        return ComputerManager.getComputer(computerID);
    }

    public IClientComputer getClientComputer() {
        return ComputerManager.getClientComputer(computerID);
    }

    public IServerComputer getServerComputer() {
        return ComputerManager.getServerComputer(computerID);
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
