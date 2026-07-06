// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.enums;

import lombok.Generated;

public enum ErrorCode
{
    SUCCESS(0), 
    PIPE_CLOSED(1), 
    READ_CORRUPT(2), 
    UNKNOWN(-1), 
    USER_LOGOUT(1000);
    
    private final int id;
    
    private ErrorCode(final int id) {
        this.id = id;
    }
    
    @Generated
    public int getId() {
        return this.id;
    }
}
