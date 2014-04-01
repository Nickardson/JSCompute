package com.nickardson.jscomputing.javascript;

import java.util.HashMap;
import java.util.Map;

public class ComputerManager {
    private static Map<Integer, IComputer> computers = new HashMap<Integer, IComputer>();

    public static boolean has(int tempID) {
        return computers.containsKey(tempID);
    }

    public static IComputer get(int tempID) {
        if (has(tempID)) {
            return computers.get(tempID);
        } else {
            return null;
        }
    }

    public static void add(IComputer computer) {
        computers.put(computer.getTempID(), computer);
    }

    public static void remove(int tempID) {
        computers.remove(tempID);
    }

    public static void remove(IComputer computer) {
        remove(computer.getTempID());
    }
}
