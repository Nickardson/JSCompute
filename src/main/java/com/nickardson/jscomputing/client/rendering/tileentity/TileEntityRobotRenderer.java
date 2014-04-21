package com.nickardson.jscomputing.client.rendering.tileentity;

import com.nickardson.jscomputing.JSComputingMod;
import com.nickardson.jscomputing.common.tileentity.TileEntityRobot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class TileEntityRobotRenderer extends TileEntitySpecialRenderer {
    private ModelRobot model;
    private ResourceLocation texture;

    public TileEntityRobotRenderer() {
        this.model = new ModelRobot();
        this.texture = new ResourceLocation(JSComputingMod.ASSET_ID, "textures/entities/robot.png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float v) {
        TileEntityRobot entity = (TileEntityRobot) tileEntity;
        int rotation = entity.getWorldObj().getBlockMetadata(entity.xCoord, entity.yCoord, entity.zCoord);

        glPushMatrix();
        {
            glTranslated(x, y, z);
            draw(entity, rotation * 90);
        }
        glPopMatrix();
    }

    public void draw(TileEntityRobot entity, float rotation) {
        glColor3f(1, 1, 1);
        glPushMatrix();
        {
            glTranslated(0.5, 0.5, 0.5);
            glRotated(180, 0, 0, 1);
            glRotated(rotation, 0, 1, 0);

            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            model.render(null, 0, 0, 0, 0, 0, 1 / 16f);
        }
        glPopMatrix();
    }
}
