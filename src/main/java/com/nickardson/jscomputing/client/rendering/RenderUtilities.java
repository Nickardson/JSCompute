package com.nickardson.jscomputing.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtilities {

    public static double SCALE = 1;

    public static void unscale() {
        SCALE = 1;
        glPushMatrix();
        double scale = 1.0 / getScale();
        glScaled(scale, scale, 1);
    }

    public static void rescale() {
        glPopMatrix();
        SCALE = getScale();
    }

    public static int getWidth() {
        return Minecraft.getMinecraft().displayWidth;
    }

    public static int getHeight() {
        return Minecraft.getMinecraft().displayHeight;
    }

    public static int getWindowWidth() {
        return getScaledResolution().getScaledWidth();
    }

    public static int getWindowHeight() {
        return getScaledResolution().getScaledWidth();
    }

    private static double getScale() {
        return getScaledResolution().getScaleFactor();
    }

    private static ScaledResolution getScaledResolution() {
        return new ScaledResolution(Minecraft.getMinecraft().gameSettings, getWidth(), getHeight());
    }

    public static void prepare2D() {
        unscale();
        setDepth(true);

        glDisable(GL_LIGHTING);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);
    }

    public static void unprepare2D() {
        rescale();
        setDepth(true);

        glLineWidth(1);
        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor3f(1, 1, 1);
    }

    public static void setDepth(boolean depth) {
        if (!depth) {
            glDepthMask(false);
            glDisable(GL_DEPTH_TEST);
        } else {
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
        }
    }

}
