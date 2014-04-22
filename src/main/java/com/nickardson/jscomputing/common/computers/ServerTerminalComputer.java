package com.nickardson.jscomputing.common.computers;

import com.nickardson.jscomputing.common.computers.events.ComputingEventEvent;
import com.nickardson.jscomputing.common.inventory.AbstractContainerComputer;
import com.nickardson.jscomputing.common.inventory.ContainerTerminalComputer;
import com.nickardson.jscomputing.javascript.api.fs.ComputerFileStorage;
import com.nickardson.jscomputing.javascript.api.fs.IFileStorage;
import com.nickardson.jscomputing.javascript.api.fs.JarFileStorage;
import com.nickardson.jscomputing.javascript.api.fs.WritableMultiFileStorage;
import org.lwjgl.input.Keyboard;
import org.mozilla.javascript.ScriptableObject;

/**
 * A ServerJavaScriptComputer with a physical TileEntity, and a basic DOS interface.
 */
public class ServerTerminalComputer extends AbstractJavaScriptServerTerminalComputer implements IKeyboardableComputer {
    public ServerTerminalComputer(int id, AbstractTileEntityComputer entityComputer) {
        super(id, entityComputer);
    }

    @Override
    public Class<? extends AbstractContainerComputer> getContainerClass() {
        return ContainerTerminalComputer.class;
    }

    @Override
    public void start() {
        ComputerFileStorage.ComputerFileStorageJSAPI computerFS = ComputerFileStorage.create(this);
        WritableMultiFileStorage.WritableMultiFileStorageJSAPI fs = WritableMultiFileStorage.create(this, new IFileStorage[] {
                JarFileStorage.create(this, "assets/jscomputing/js/bin", "bin"),
                JarFileStorage.create(this, "assets/jscomputing/js/lib", "lib"),
                computerFS
        }, computerFS);
        getScope().defineProperty("fs", fs, ScriptableObject.READONLY);

        super.start();
    }

    @Override
    public void stop() {
        super.stop();

        if (getTileEntity().getBlockMetadata() >= 6) {
            getTileEntity().setBlockMetadata(getTileEntity().getBlockMetadata() - 6);
        }
    }

    @Override
    public void onKey(int key, char character, boolean down) {
        ComputingEventEvent event = new ComputingEventEvent("key");

        event.put("id", key);
        event.put("character", character);
        event.put("down", down);
        event.put("key", Keyboard.getKeyName(key));

        triggerEvent(event);
    }
}
