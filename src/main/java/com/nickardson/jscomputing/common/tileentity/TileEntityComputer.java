package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.javascript.ComputerManager;
import com.nickardson.jscomputing.javascript.IComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityComputer extends AbstractTileEntity implements IInventory {
    public static String NAME = JSComputingMod.ASSET_ID + "ComputerEntity";

    public static String NBT_KEY_COMPUTER_ID = "compid";

    /**
     * The ID of the corresponding computer.
     * @example
     * Computers.get(computerID);
     */
    public int tempID = 0;

    private ItemStack[] inv;

    public TileEntityComputer() {
        inv = new ItemStack[0];
    }

    int computerID;
    public int getComputerID() {
        return computerID;
    }
    public void setComputerID(int data) {
        computerID = data;
        markDirty();
        closeInventory();
    }

    public IComputer getComputer() {
        return ComputerManager.get(tempID);
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
