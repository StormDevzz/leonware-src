package sweetie.leonware.api.utils.player;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/GrimNoFallSystem.class */
public final class GrimNoFallSystem {
    private static int takenFallDamage = 0;

    @Generated
    private GrimNoFallSystem() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void updateFallDamage() {
        if (takenFallDamage < 5) {
            takenFallDamage++;
        }
    }

    public static int getTakenFallDamage() {
        return takenFallDamage;
    }

    public static void reset() {
        takenFallDamage = 0;
    }
}
