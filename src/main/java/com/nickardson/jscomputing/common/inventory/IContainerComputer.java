package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import net.minecraft.tileentity.TileEntity;

public interface IContainerComputer {
    public IComputer getComputer();
    public IClientComputer getClientComputer();
    public IServerComputer getServerComputer();

    public TileEntity getTileEntity();
}
