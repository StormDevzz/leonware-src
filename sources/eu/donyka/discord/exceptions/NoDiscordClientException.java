package eu.donyka.discord.exceptions;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/exceptions/NoDiscordClientException.class */
public class NoDiscordClientException extends Exception {
    public NoDiscordClientException() {
        super("No Discord client found. Please make sure Discord is running.");
    }

    public NoDiscordClientException(String message) {
        super(message);
    }
}
