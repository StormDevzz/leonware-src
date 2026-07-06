// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.render.fonts;

import lombok.Generated;

public enum Icons
{
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
    
    public static Icons find(final String name) {
        try {
            return valueOf(name);
        }
        catch (final IllegalArgumentException e) {
            return null;
        }
    }
    
    @Generated
    public String getLetter() {
        return this.letter;
    }
    
    @Generated
    private Icons(final String letter) {
        this.letter = letter;
    }
}
