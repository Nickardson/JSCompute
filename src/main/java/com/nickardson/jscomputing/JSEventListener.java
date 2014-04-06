package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class JSEventListener {

    public Queue<Runnable> queuedActions = new ArrayDeque<Runnable>();

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (event.side.isClient()) {
            for (IClientComputer computer : ComputerManager.getClientComputers().values()) {
                computer.tick();
            }
        } else {
            for (IServerComputer computer : ComputerManager.getServerComputers().values()) {
                computer.tick();
            }

            Runnable run = queuedActions.poll();
            if (run != null) {
                run.run();
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        System.out.println(ComputerManager.getWorldDirectory());
    }
}
