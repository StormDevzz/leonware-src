/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.enums;

import lombok.Generated;

public enum ErrorCode {
    SUCCESS(0),
    PIPE_CLOSED(1),
    READ_CORRUPT(2),
    UNKNOWN(-1),
    USER_LOGOUT(1000);

    private final int id;

    private ErrorCode(int id) {
        this.id = id;
    }

    @Generated
    public int getId() {
        return this.id;
    }
}

