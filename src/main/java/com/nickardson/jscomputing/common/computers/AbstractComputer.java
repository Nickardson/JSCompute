package com.nickardson.jscomputing.common.computers;

/**
 * Basic implementation of a computer.
 */
public abstract class AbstractComputer implements IComputer {
    int id = 0;

    public AbstractComputer(int id) {
        setID(id);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public Object convert(Object object) {
        return object;
    }
}
