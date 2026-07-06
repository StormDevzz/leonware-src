// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.DatagramSocketImpl;

abstract class DatagramSocketImplShim extends DatagramSocketImpl
{
    protected DatagramSocketImplShim() {
    }
    
    @Deprecated
    @Override
    protected final void finalize() {
        try {
            this.close();
        }
        catch (final Exception ex) {}
    }
}
