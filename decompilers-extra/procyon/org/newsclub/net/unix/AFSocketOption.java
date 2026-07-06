// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.SocketOption;

public final class AFSocketOption<T> implements SocketOption<T>
{
    private final String name;
    private final Class<T> type;
    private final int level;
    private final int optionName;
    
    public AFSocketOption(final String name, final Class<T> type, final int level, final int optionName) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.optionName = optionName;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public Class<T> type() {
        return this.type;
    }
    
    int level() {
        return this.level;
    }
    
    int optionName() {
        return this.optionName;
    }
    
    @Override
    public String toString() {
        return this.getClass() + ":" + this.name;
    }
}
