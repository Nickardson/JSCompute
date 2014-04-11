package com.nickardson.jscomputing.client.rendering;

import com.nickardson.jscomputing.JSComputingMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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

    public static void bindImage(ResourceLocation image) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
    }

    public static Color DEFAULT_IMAGE_COLOR = Color.white;

    // Bound Basic draw
    public static void drawBoundImage(double x, double y, double w, double h) {
        drawBoundImage(x, y, w, h, DEFAULT_IMAGE_COLOR);
    }
    public static void drawBoundImage(double x, double y, double w, double h, Color col) {
        drawBoundImage(x, y, w, h, 0, col);
    }

    // Bound Draw with rotation
    public static void drawBoundImage(double x, double y, double w, double h, double r) {
        drawBoundImage(x, y, w, h, r, DEFAULT_IMAGE_COLOR);
    }
    public static void drawBoundImage(double x, double y, double w, double h, double r, Color col) {
        drawBoundImage(x, y, w, h, r, 0, 0, col);
    }

    // Bound Draw with UV
    public static void drawBoundImage(double x, double y, double w, double h, double rot, double u, double v) {
        drawBoundImage(x, y, w, h, rot, u, v, DEFAULT_IMAGE_COLOR);
    }
    public static void drawBoundImage(double x, double y, double w, double h, double rot, double u, double v, Color col) {
        drawBoundImageInternal(x, y, w, h, rot, u, v, col);
    }

    public static void glColor(Color color) {
        glColor4d(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0, color.getAlpha() / 255.0);
    }

    private static void drawBoundImageInternal(double x, double y, double width, double height, double rotation, double u, double v, Color color) {
        glColor(color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        GL11.glTranslated(x + width / 2, y + height / 2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        GL11.glTranslated(-(x + width / 2), -(y + height / 2), 0);
        drawTexturedModalRect(x, y, u, v, width, height);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    private static void drawTexturedModalRect(double x, double y, double u, double v, double width, double height)
    {
        float uScale = (float) (1.0F / width);
        float vScale = (float) (1.0F / height);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, u * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, 0, ((u + width) * uScale), ((v + height) * vScale));
        tessellator.addVertexWithUV(x + width, y, 0, ((u + width) * uScale), v * vScale);
        tessellator.addVertexWithUV(x, y, 0, u * uScale, v * vScale);

        tessellator.draw();
    }
}
