/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.net.DatagramSocketImpl;

abstract class DatagramSocketImplShim
extends DatagramSocketImpl {
    protected DatagramSocketImplShim() {
    }

    @Deprecated
    protected final void finalize() {
        try {
            this.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

