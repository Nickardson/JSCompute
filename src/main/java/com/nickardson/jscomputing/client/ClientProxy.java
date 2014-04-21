package com.nickardson.jscomputing.client;

import com.nickardson.jscomputing.client.rendering.BlockRendingHandler;
import com.nickardson.jscomputing.client.rendering.tileentity.TileEntityRobotRenderer;
import com.nickardson.jscomputing.common.CommonProxy;
import com.nickardson.jscomputing.common.tileentity.TileEntityRobot;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRobot.class, new TileEntityRobotRenderer());

        int id = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(id, new BlockRendingHandler(id));
    }
}
