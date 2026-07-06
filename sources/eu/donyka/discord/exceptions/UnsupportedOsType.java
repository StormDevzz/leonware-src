package eu.donyka.discord.exceptions;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/exceptions/UnsupportedOsType.class */
public class UnsupportedOsType extends Exception {
    public UnsupportedOsType(String osType) {
        super("Unsupported OS type: " + osType);
    }
}
