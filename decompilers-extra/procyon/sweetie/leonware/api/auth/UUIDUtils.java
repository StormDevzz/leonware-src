// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.auth;

import java.util.UUID;

public class UUIDUtils
{
    public static UUID parseUuid(final String string) {
        try {
            return UUID.fromString(string);
        }
        catch (final IllegalArgumentException e) {
            return uuidFromUnformatted(string);
        }
    }
    
    private static UUID uuidFromUnformatted(final String input) {
        if (input.length() != 32) {
            throw new IllegalArgumentException("\u0425\u0423\u0419");
        }
        final long mostSigBits = Long.parseUnsignedLong(input.substring(0, 16), 16);
        final long leastSigBits = Long.parseUnsignedLong(input.substring(16, 32), 16);
        return new UUID(mostSigBits, leastSigBits);
    }
    
    public static UUID generateOfflinePlayerUuid(final String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
    }
}
