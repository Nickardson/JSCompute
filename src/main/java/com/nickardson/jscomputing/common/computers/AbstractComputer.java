package com.nickardson.jscomputing.common.computers;

/**
 * Basic implementation of a computer.
 */
public abstract class AbstractComputer implements IComputer {
    int id = 0;

    public AbstractComputer(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }
}
