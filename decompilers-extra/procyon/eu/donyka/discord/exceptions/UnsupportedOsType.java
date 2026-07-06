// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.exceptions;

public class UnsupportedOsType extends Exception
{
    public UnsupportedOsType(final String osType) {
        super("Unsupported OS type: " + osType);
    }
}
