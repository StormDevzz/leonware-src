/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.enums;

import lombok.Generated;

public enum OpCode {
    HANDSHAKE(0),
    FRAME(1),
    CLOSE(2),
    PING(3),
    PONG(4);

    private final int id;

    private OpCode(int id) {
        this.id = id;
    }

    @Generated
    public int getId() {
        return this.id;
    }
}

