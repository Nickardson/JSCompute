package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IComputer;
import net.minecraft.tileentity.TileEntity;

public interface IContainerComputer {
    public IComputer getComputer();
    public TileEntity getTileEntity();
}
