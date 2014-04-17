package com.nickardson.jscomputing.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * A simple font renderer which draws a monospace font from an image file.
 */
public class ImageFontRenderer {
    ResourceLocation resource;

    float textureWidth;
    float textureHeight;
    int characterWidth;
    int characterHeight;

    public ImageFontRenderer(ResourceLocation resource, int textureWidth, int textureHeight, int characterWidth, int characterHeight) {
        this.resource = resource;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
    }

    /**
     * Should be called before starting to draw text.
     */
    public void begin() {
        Minecraft.getMinecraft().renderEngine.bindTexture(resource);
    }

    public void drawCharacter(char c, int xPos, int yPos) {
        int uStart = (c % 16) * characterWidth,
            vStart = (c / 16) * characterHeight;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(xPos, yPos, 0, uStart / textureWidth, vStart / textureHeight);
        tessellator.addVertexWithUV(xPos, yPos + characterHeight, 0, uStart / textureWidth, (vStart + characterHeight) / textureHeight);
        tessellator.addVertexWithUV(xPos + characterWidth, yPos + characterHeight, 0, (uStart + characterWidth) / textureWidth, (vStart + characterHeight) / textureHeight);
        tessellator.addVertexWithUV(xPos + characterWidth, yPos, 0, (uStart + characterWidth) / textureWidth, vStart / textureHeight);

        tessellator.draw();
    }

    public void drawString(char[] line, int xPos, int yPos) {
        int x = 0;
        for (char c : line) {
            if (c > 0) {
                drawCharacter(c, xPos + x * getCharacterWidth(), yPos);
            }
            x++;
        }
    }

    public void drawString(String line, int xPos, int yPos) {
        drawString(line.toCharArray(), xPos, yPos);
    }

    public ResourceLocation getResource() {
        return resource;
    }

    public void setResource(ResourceLocation resource) {
        this.resource = resource;
    }

    public float getTextureWidth() {
        return textureWidth;
    }

    public void setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public void setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
    }

    public int getCharacterWidth() {
        return characterWidth;
    }

    public void setCharacterWidth(int characterWidth) {
        this.characterWidth = characterWidth;
    }

    public int getCharacterHeight() {
        return characterHeight;
    }

    public void setCharacterHeight(int characterHeight) {
        this.characterHeight = characterHeight;
    }
}
