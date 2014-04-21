package com.nickardson.jscomputing.client.rendering;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.client.rendering.tileentity.TileEntityRobotRenderer;
import com.nickardson.jscomputing.common.blocks.BlockRobot;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class BlockRendingHandler implements ISimpleBlockRenderingHandler {
    int RENDER_ID;
    TileEntityRobotRenderer robotRenderer;

    public BlockRendingHandler(int RENDER_ID) {
        this.RENDER_ID = RENDER_ID;

        BlockRobot.renderID = RENDER_ID;

        robotRenderer = new TileEntityRobotRenderer();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (block.equals(JSComputingMod.Blocks.robot)) {
            if (metadata == 0) {
                metadata = 2;
            }
            robotRenderer.draw(null, metadata * 90);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RENDER_ID;
    }
}
