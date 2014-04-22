package com.nickardson.jscomputing.common.inventory;

import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class ContainerTerminalComputer extends AbstractContainerComputer {
    private TileEntityTerminalComputer tileEntity;

    public ContainerTerminalComputer(TileEntityTerminalComputer computer) {
        this.tileEntity = computer;
    }

    @Override
    public TileEntityTerminalComputer getTileEntity() {
        return tileEntity;
    }

    @Override
    public IComputer getComputer() {
        return tileEntity.getComputer();
    }

    @Override
    public IClientComputer getClientComputer() {
        return tileEntity.getClientComputer();
    }

    @Override
    public IServerComputer getServerComputer() {
        return tileEntity.getServerComputer();
    }
}
