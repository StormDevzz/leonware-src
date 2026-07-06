// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.exceptions;

public class NoDiscordClientException extends Exception
{
    public NoDiscordClientException() {
        super("No Discord client found. Please make sure Discord is running.");
    }
    
    public NoDiscordClientException(final String message) {
        super(message);
    }
}
