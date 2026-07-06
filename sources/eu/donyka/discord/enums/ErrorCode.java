package eu.donyka.discord.enums;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/enums/ErrorCode.class */
public enum ErrorCode {
    SUCCESS(0),
    PIPE_CLOSED(1),
    READ_CORRUPT(2),
    UNKNOWN(-1),
    USER_LOGOUT(1000);

    private final int id;

    @Generated
    public int getId() {
        return this.id;
    }

    ErrorCode(int id) {
        this.id = id;
    }
}
