package sweetie.leonware.api.utils.render.fonts;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/render/fonts/Icons.class */
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

    @Generated
    Icons(final String letter) {
        this.letter = letter;
    }

    @Generated
    public String getLetter() {
        return this.letter;
    }

    public static Icons find(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
