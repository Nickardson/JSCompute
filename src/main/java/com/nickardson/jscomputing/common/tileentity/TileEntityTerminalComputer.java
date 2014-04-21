package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.*;
import com.nickardson.jscomputing.utility.NetworkUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class TileEntityTerminalComputer extends AbstractTileEntityComputer implements IComputerContainer, IInventory {
    public static String NAME = JSComputingMod.ASSET_ID + "ComputerEntity";

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

    /**
     * Sets whether the computer is on.
     */
    public void setOn(boolean on) {
        super.setOn(on);

        if (on) {
            IComputer computer;
            if (NetworkUtilities.isServerWorld(getWorldObj())) {
                int nextID = getComputerID();
                if (nextID == -1) {
                    nextID = ComputerManager.getNextAvailableID();
                }
                computer = new ServerTerminalComputer(nextID, this);
                this.update();
            } else {
                computer = new ClientTerminalComputer(this);
            }
            this.setComputerID(computer.getID());
            computer.start();
        }
    }

    private ItemStack[] inv;
    public TileEntityTerminalComputer() {
        inv = new ItemStack[0];
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
        return player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 8 * 8;
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
