package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IComputer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;

public class JSEventListener {
    @SubscribeEvent
    public void onTick(TickEvent event) {
        for (IComputer computer : ComputerManager.getComputers().values()) {
            computer.tick();
        }
    }
}
