package org.newsclub.net.unix;

import java.net.DatagramSocketImpl;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/DatagramSocketImplShim.class */
abstract class DatagramSocketImplShim extends DatagramSocketImpl {
    protected DatagramSocketImplShim() {
    }

    @Deprecated
    protected final void finalize() {
        try {
            close();
        } catch (Exception e) {
        }
    }
}
