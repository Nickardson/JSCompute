package com.nickardson.jscomputing;

import com.nickardson.jscomputing.common.computers.ComputerManager;
import com.nickardson.jscomputing.common.computers.IClientComputer;
import com.nickardson.jscomputing.common.computers.IServerComputer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

public class JSEventListener {

    public Queue<Runnable> onNextTick = new ArrayDeque<Runnable>();

    public void queue(Runnable runnable) {
        onNextTick.add(runnable);
    }

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

            while (!onNextTick.isEmpty()) {
                onNextTick.poll().run();
            }
        }
    }
}
