package com.nickardson.jscomputing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class JSComputingCreativeTab extends CreativeTabs {
    public JSComputingCreativeTab() {
        super("JSComputing");
    }

    @Override
    public Item getTabIconItem() {
        return Item.getItemFromBlock(JSComputingMod.computorz);
    }
}
