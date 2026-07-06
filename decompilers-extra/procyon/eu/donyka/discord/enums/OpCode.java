// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.enums;

import lombok.Generated;

public enum OpCode
{
    HANDSHAKE(0), 
    FRAME(1), 
    CLOSE(2), 
    PING(3), 
    PONG(4);
    
    private final int id;
    
    private OpCode(final int id) {
        this.id = id;
    }
    
    @Generated
    public int getId() {
        return this.id;
    }
}
