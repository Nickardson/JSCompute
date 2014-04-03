package com.nickardson.jscomputing.client.rendering;

import java.awt.Color;

public enum CharacterCode {
    Red         ("c", CodeType.Color),
    Dark_Red    ("4", CodeType.Color),
    Gold        ("6", CodeType.Color),
    Yellow      ("e", CodeType.Color),
    Green       ("a", CodeType.Color),
    Dark_Green  ("2", CodeType.Color),
    Blue        ("9", CodeType.Color),
    Dark_Blue   ("1", CodeType.Color),
    Aqua        ("b", CodeType.Color),
    Dark_Aqua   ("3", CodeType.Color),
    Purple      ("d", CodeType.Color),
    Dark_Purple ("5", CodeType.Color),
    White       ("f", CodeType.Color),
    Gray        ("7", CodeType.Color),
    Dark_Gray   ("8", CodeType.Color),
    Black       ("0", CodeType.Color),
    
    Obfuscated  ("k", CodeType.Format),
    Bold        ("l", CodeType.Format),
    Strikethrough("m", CodeType.Format),
    Underline   ("n", CodeType.Format),
    Italic      ("o", CodeType.Format),
    Reset       ("r", CodeType.Format),
    None        ("z", CodeType.Format);
    
    public static int[] colorCode = new int[32];
    static {
        for (int i = 0; i < 32; ++i)
        {
            int huehuehue = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + huehuehue;
            int g = (i >> 1 & 1) * 170 + huehuehue;
            int b = (i & 1) * 170 + huehuehue;

            if (i == 6)
                r += 85;
            
            // Darken for shadows.
            if (i >= 16)
            {
                r /= 4;
                g /= 4;
                b /= 4;
            }

            colorCode[i] = (r & 255) << 16 | (g & 255) << 8 | b & 255;
        }
    }
    
    private final String code;
    private final CodeType type;
    public static String SECTION = "\247"; // §
    private CharacterCode(String code, CodeType type) {
        this.code = code;
        this.type = type;
    }
    
    public String getCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return SECTION + code;
    }
    
    public Color getColor() {
        return getColor(false);
    }
    public Color getColor(boolean isShadow) {
        int index = "0123456789abcdefklmnor".indexOf(getCode()) + (isShadow ? 16 : 0);
        if (index < 0) {
            index = 0;
        }
        return new Color(colorCode[index]);
    }
    
    public CodeType getType() {
        return type;
    }
    
    /**
     * Encase a string in a character code, finishing with a reset.
     * @param str
     * @param code
     * @return 
     */
    public static String encase(String str, CharacterCode code) {
        return code + str + CharacterCode.Reset;
    }
    
    public static String strip(String str) {
        String out = "";
        
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i+1).equals(CharacterCode.SECTION)) {
                i++;
                if (str.length() > i && str.substring(i, i+1).equals("#")) {
                    i += 6;
                }
            }
            else {
                out += str.substring(i, i+1);
            }
        }
        
        return out;
    }
    
    /**
     * Turns a string into a CharacterCode.
     * @param str The string, 
     * @return 
     */
    public static CharacterCode fromString(String str) {
        for (CharacterCode code : CharacterCode.values()) {
            if (code.toString().equals(str))
                return code;
        }
        
        return CharacterCode.None;
    }
    
    public static String rgb(String hex) {
        return CharacterCode.SECTION + "#" + hex;
    }

    public static String hex(String hex) {
        return CharacterCode.SECTION + "#" + hex;
    }

    public enum CodeType {
        Color,
        Format
    }
}