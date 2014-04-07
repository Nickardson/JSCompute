package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTerminalComputer extends AbstractTileEntity implements IInventory {
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

    private ItemStack[] inv;
    public TileEntityTerminalComputer() {
        inv = new ItemStack[0];
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

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return JSComputingMod.ASSET_ID + "ComputerInventory";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return //worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 8 * 8;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }
}
