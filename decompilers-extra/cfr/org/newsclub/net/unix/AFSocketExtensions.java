/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

public interface AFSocketExtensions {
    public int getAncillaryReceiveBufferSize();

    public void setAncillaryReceiveBufferSize(int var1);

    public void ensureAncillaryReceiveBufferSize(int var1);
}

