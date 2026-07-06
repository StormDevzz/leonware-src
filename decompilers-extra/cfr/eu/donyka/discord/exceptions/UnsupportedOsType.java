/*
 * Decompiled with CFR 0.152.
 */
package eu.donyka.discord.exceptions;

public class UnsupportedOsType
extends Exception {
    public UnsupportedOsType(String osType) {
        super("Unsupported OS type: " + osType);
    }
}

