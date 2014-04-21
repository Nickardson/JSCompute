package com.nickardson.jscomputing.utility;

import org.lwjgl.input.Keyboard;

public class ComputerUtilities {
    /**
     * Gets whether the given LWJGL Key ID should be consumed by the computer.
     * @param key The LWJGL Key ID of the key to check.
     * @return Whether the key is valid input.
     */
    public static boolean isValidComputerKey(int key) {
        return key != Keyboard.KEY_ESCAPE;
    }
}
