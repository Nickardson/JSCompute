package com.nickardson.jscomputing.common.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityTerminalComputer extends AbstractTileEntity implements IInventory {
    public static String NAME = JSComputingMod.ASSET_ID + "ComputerEntity";

    public static String NBT_KEY_COMPUTER_ID = "compid";

    /**
     * The ID of the corresponding computer.
     * @example
     * Computers.get(computerID);
     */
    public int computerID = -1;

    private ItemStack[] inv;

    public TileEntityTerminalComputer() {
        inv = new ItemStack[0];
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
        update();
        closeInventory();
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
