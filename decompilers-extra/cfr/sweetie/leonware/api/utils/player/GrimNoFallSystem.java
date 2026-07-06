/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.utils.player;

import lombok.Generated;

public final class GrimNoFallSystem {
    private static int takenFallDamage = 0;

    public static void updateFallDamage() {
        if (takenFallDamage < 5) {
            ++takenFallDamage;
        }
    }

    public static int getTakenFallDamage() {
        return takenFallDamage;
    }

    public static void reset() {
        takenFallDamage = 0;
    }

    @Generated
    private GrimNoFallSystem() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

