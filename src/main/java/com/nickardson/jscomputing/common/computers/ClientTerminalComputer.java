package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.tileentity.TileEntityTerminalComputer;

public class ClientTerminalComputer extends AbstractTerminalComputer implements IClientComputer {

    public ClientTerminalComputer(TileEntityTerminalComputer computer) {
        super(computer.getComputerID());
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

    private boolean linesUpdated = false;

    @Override
    public void updateLines(byte[][] lines) {
        linesUpdated = true;
        setLines(lines);
    }

    public boolean pollUpdated() {
        if (linesUpdated) {
            linesUpdated = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendLines() {

    }
}
