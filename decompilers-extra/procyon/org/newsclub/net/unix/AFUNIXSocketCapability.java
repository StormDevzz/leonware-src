// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

@Deprecated
public enum AFUNIXSocketCapability
{
    CAPABILITY_PEER_CREDENTIALS(0), 
    CAPABILITY_ANCILLARY_MESSAGES(1), 
    CAPABILITY_FILE_DESCRIPTORS(2), 
    CAPABILITY_ABSTRACT_NAMESPACE(3), 
    CAPABILITY_DATAGRAMS(4), 
    CAPABILITY_NATIVE_SOCKETPAIR(5);
    
    private final int bitmask;
    
    private AFUNIXSocketCapability(final int bit) {
        this.bitmask = 1 << bit;
    }
    
    int getBitmask() {
        return this.bitmask;
    }
}
