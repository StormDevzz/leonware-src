/*
 * Decompiled with CFR 0.152.
 */
package sweetie.leonware.api.auth;

import java.util.UUID;

public class UUIDUtils {
    public static UUID parseUuid(String string) {
        try {
            return UUID.fromString(string);
        }
        catch (IllegalArgumentException e) {
            return UUIDUtils.uuidFromUnformatted(string);
        }
    }

    private static UUID uuidFromUnformatted(String input) {
        if (input.length() != 32) {
            throw new IllegalArgumentException("\u0425\u0423\u0419");
        }
        long mostSigBits = Long.parseUnsignedLong(input.substring(0, 16), 16);
        long leastSigBits = Long.parseUnsignedLong(input.substring(16, 32), 16);
        return new UUID(mostSigBits, leastSigBits);
    }

    public static UUID generateOfflinePlayerUuid(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
    }
}

