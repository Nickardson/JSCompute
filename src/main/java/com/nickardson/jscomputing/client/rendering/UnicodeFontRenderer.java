package com.nickardson.jscomputing.client.rendering;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.Effect;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;

public class UnicodeFontRenderer {
    private final UnicodeFont font;

    @SuppressWarnings("unchecked")
    public UnicodeFontRenderer(Font awtFont, Effect[] effects) {
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(Color.white));
        font.getEffects().addAll(Arrays.asList(effects));

        font.addGlyphs(0, 255);

        try {
            font.loadGlyphs();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public UnicodeFontRenderer(Font awtFont) {
        this(awtFont, new Effect[0]);
    }

    /**
     * Draws a string.
     * @param text The text to draw.
     * @param x X position
     * @param y Y position
     * @param defaultColor The hex color, ie 0xFF0000.
     * @param shadow Whether to replace all colors with black.
     * @return The width of the draw string.
     */
    public double drawString(String text, int x, int y, int defaultColor, boolean shadow) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        x *= RenderUtilities.SCALE;
        y *= RenderUtilities.SCALE;

        boolean blend = glIsEnabled(GL_BLEND);
        boolean lighting = glIsEnabled(GL_LIGHTING);
        boolean texture = glIsEnabled(GL_TEXTURE_2D);
        if(!blend)
            glEnable(GL_BLEND);
        if(lighting)
            glDisable(GL_LIGHTING);
        if(texture)
            glDisable(GL_TEXTURE_2D);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glPushMatrix();
        glScaled(1 / RenderUtilities.SCALE, 1 / RenderUtilities.SCALE, 1 / RenderUtilities.SCALE);

        double xOffset = 0;
        if (text.contains(CharacterCode.SECTION)) {
            // Split the text into subsections, seperated by colorcodes.  The first of which may or may not have a color code, but all sections after will.
            String[] sections = text.split(CharacterCode.SECTION);

            boolean firstIsPlain = !text.startsWith(CharacterCode.SECTION);

            for (int i = 0; i < sections.length; i++) {
                String str = sections[i];

                if (str.isEmpty()) {
                    continue;
                }

                // Get the color this section should be renderered in.
                Color color;
                if (str.length() > 6 && str.charAt(0) == '#') {
                    try {
                        color = new Color(
                                Integer.valueOf( str.substring( 1, 3 ), 16 ),
                                Integer.valueOf( str.substring( 3, 5 ), 16 ),
                                Integer.valueOf( str.substring( 5, 7 ), 16 ) );
                    } catch (java.lang.NumberFormatException ex) {
                        color = new Color(defaultColor);
                    }

                    // Remove the hex color.
                    sections[i] = sections[i].substring(7);
                } else {
                    CharacterCode code = CharacterCode.fromString(CharacterCode.SECTION + str.charAt(0));

                    // Being the first with an unstyled text, or being a reset character puts us back to the default color.
                    if ((firstIsPlain && i == 0) || code == CharacterCode.Reset) {
                        color = new Color(defaultColor);
                    } else {
                        color = code.getColor();
                    }

                    if (i != 0 || !firstIsPlain) {
                        // Remove the color identifier from the string.
                        sections[i] = sections[i].substring(1);
                    }
                }

                if (shadow) {
                    color = Color.BLACK;
                }

                font.drawString((float)(x + xOffset), y, sections[i], new org.newdawn.slick.Color(color.getRed(), color.getGreen(), color.getBlue()));

                xOffset += getWidth(sections[i]) * RenderUtilities.SCALE;
            }
        } else {
            // If there's no special color code magical trickery, just do a simple render.
            font.drawString(x, y, text, new org.newdawn.slick.Color(shadow ? 0x000000 : defaultColor));
        }
        glPopMatrix();

        if(texture)
            glEnable(GL_TEXTURE_2D);
        if(lighting)
            glEnable(GL_LIGHTING);
        if(!blend)
            glDisable(GL_BLEND);

        if (text.startsWith(CharacterCode.SECTION)) {
            return (int) xOffset;
        } else {
            return getWidth(text);
        }
    }

    /**
     * Draws a string.
     * @param string The text to draw.
     * @param x X position
     * @param y Y position
     * @param color The hex color, ie 0xFF0000.
     * @return The width of the draw string.
     */
    public double drawString(String string, int x, int y, int color) {
        return drawString(string, x, y, color, false);
    }

    /**
     * Draws a string.
     * @param string The text to draw.
     * @param x X position
     * @param y Y position
     * @param color The color.
     * @return The width of the draw string.
     */
    public double drawString(String string, int x, int y, Color color) {
      return drawString(string, x, y, color.getRGB());
    }

    /**
     * Draws a string.
     * @param string The text to draw.
     * @param x X position
     * @param y Y position
     * @param color The color.
     * @return The width of the draw string.
     */
    public double drawString(String string, int x, int y, CharacterCode color) {
        return drawString(string, x, y, color.getColor());
    }

    /**
     * Draws a string, with a black shadow behind it.
     * @param string The text to draw.
     * @param x X position
     * @param y Y position
     * @param color The color.
     * @return The width of the draw string.
     */
    public double drawStringWithShadow(String string, int x, int y, int color) {
        drawString(string, (int) (x + (1 / RenderUtilities.SCALE)), (int) (y + (1 / RenderUtilities.SCALE)), color, true);
        return drawString(string, x, y, color, false);
    }

    /**
     * Gets the width of the given character.
     * @param c The character to check the width of.
     * @return The width of the character, in pixels.
     */
    public double getWidth(char c) {
        return getWidth(Character.toString(c));
    }

    /**
     * Gets the width of the given string.
     * @param string The string to check the width of.
     * @return The width of the string, in pixels.
     */
    public double getWidth(String string) {
        return font.getWidth(CharacterCode.strip(string));
    }

    /**
     * Gets the height of the given string.
     * @param string The string to check the height of.
     * @return The height of the string, in pixels.
     */
    public double getHeight(String string) {
        return font.getHeight(string);
    }

    /**
     * Loads a font from an inputstream.
     * @param source The stream from which the font will be read
     * @param size The size, in pixels of the font.
     * @param mods The font's modifiers, i.e. Font.PLAIN.
     * @return The FontRenderer.
     */
    public static UnicodeFontRenderer fromInputStream(InputStream source, int size, int mods) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, source);
            return new UnicodeFontRenderer(font.deriveFont(mods, size));
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Loads a System font.
     * @param name The name of the font.
     * @param size The size, in pixels of the font.
     * @param mods The font's modifiers, i.e. Font.PLAIN.
     * @return The FontRenderer.
     */
    public static UnicodeFontRenderer fromSystem(String name, int size, int mods) {
        return new UnicodeFontRenderer(new Font(name, mods, size));
    }
}
