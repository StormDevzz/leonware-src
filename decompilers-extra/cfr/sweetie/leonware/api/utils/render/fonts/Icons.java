/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.render.fonts;

import lombok.Generated;

public enum Icons {
    COORDS("W"),
    SIGNAL("B"),
    SPEED("V"),
    WLAN("N"),
    PERFORMANCE("Y"),
    QUIT("P"),
    MULTIPLAYER("A"),
    OPTIONS("S"),
    SINGLEPLAYER("U"),
    STEP_B("K"),
    STEP_F("L"),
    PAUSE("O"),
    PLAY("I"),
    FOLDER("F"),
    RIGHTR("R"),
    REFRESH("G"),
    DOCUMENT("D"),
    TRASH("T"),
    CROSS("C");

    private final String letter;

    public static Icons find(String name) {
        try {
            return Icons.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Generated
    public String getLetter() {
        return this.letter;
    }

    @Generated
    private Icons(String letter) {
        this.letter = letter;
    }
}

