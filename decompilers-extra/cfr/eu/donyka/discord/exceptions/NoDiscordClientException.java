/*
 * Decompiled with CFR 0.152.
 */
package eu.donyka.discord.exceptions;

public class NoDiscordClientException
extends Exception {
    public NoDiscordClientException() {
        super("No Discord client found. Please make sure Discord is running.");
    }

    public NoDiscordClientException(String message) {
        super(message);
    }
}

