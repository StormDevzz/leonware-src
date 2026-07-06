// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.player;

import lombok.Generated;

public final class GrimNoFallSystem
{
    private static int takenFallDamage;
    
    public static void updateFallDamage() {
        if (GrimNoFallSystem.takenFallDamage < 5) {
            ++GrimNoFallSystem.takenFallDamage;
        }
    }
    
    public static int getTakenFallDamage() {
        return GrimNoFallSystem.takenFallDamage;
    }
    
    public static void reset() {
        GrimNoFallSystem.takenFallDamage = 0;
    }
    
    @Generated
    private GrimNoFallSystem() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        GrimNoFallSystem.takenFallDamage = 0;
    }
}
