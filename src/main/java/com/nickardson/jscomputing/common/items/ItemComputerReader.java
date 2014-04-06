package com.nickardson.jscomputing.common.items;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemComputerReader extends Item {
    public ItemComputerReader() {
        setUnlocalizedName("computerReader");
        setCreativeTab(JSComputingMod.creativeTab);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float subX, float subY, float subZ) {
        TileEntity e = world.getTileEntity(x, y, z);
        if (!world.isRemote && e instanceof TileEntityTerminalComputer) {
            TileEntityTerminalComputer computer = (TileEntityTerminalComputer) e;
            player.addChatMessage(new ChatComponentText("ID: " + computer.computerID));

            NBTTagCompound nbt = new NBTTagCompound();
            computer.writeToNBT(nbt);
            player.addChatMessage(new ChatComponentText("NBT: " + nbt));

            player.addChatMessage(new ChatComponentText("ClientComputer: " + computer.getClientComputer()));
            player.addChatMessage(new ChatComponentText("ServerComputer: " + computer.getServerComputer()));
        }
        return true;
    }
}
