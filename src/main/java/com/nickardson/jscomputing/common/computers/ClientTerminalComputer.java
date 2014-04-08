package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class ClientTerminalComputer extends AbstractTerminalComputer implements IClientComputer {

    private TileEntityTerminalComputer entity;

    public ClientTerminalComputer(int id, TileEntityTerminalComputer computer) {
        super(computer.getComputerID());

        this.entity = computer;
    }

    @Override
    public int getID() {
        return super.getID();
    }

    @Override
    public void render() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void start() {
        ComputerManager.addClientComputer(this);
    }

    @Override
    public void stop() {

    }

    @Override
    public void updateLines(char[][] lines) {

    }

    @Override
    public void sendLines() {

    }
}
