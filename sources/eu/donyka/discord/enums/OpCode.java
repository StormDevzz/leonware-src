package eu.donyka.discord.enums;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/enums/OpCode.class */
public enum OpCode {
    HANDSHAKE(0),
    FRAME(1),
    CLOSE(2),
    PING(3),
    PONG(4);

    private final int id;

    @Generated
    public int getId() {
        return this.id;
    }

    OpCode(int id) {
        this.id = id;
    }
}
