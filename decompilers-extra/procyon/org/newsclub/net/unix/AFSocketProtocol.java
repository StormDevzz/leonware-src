// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

public enum AFSocketProtocol
{
    DEFAULT(0);
    
    private final int id;
    
    private AFSocketProtocol(final int id) {
        this.id = id;
    }
    
    int getId() {
        return this.id;
    }
}
