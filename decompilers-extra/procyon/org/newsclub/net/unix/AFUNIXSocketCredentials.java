// 
// Decompiled by Procyon v0.6.0
// 

package org.newsclub.net.unix;

import java.net.Socket;
import java.io.IOException;
import java.rmi.server.RemoteServer;
import java.util.Arrays;
import java.util.UUID;
import java.io.Serializable;

public final class AFUNIXSocketCredentials implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final AFUNIXSocketCredentials SAME_PROCESS;
    private long pid;
    private long uid;
    private long[] gids;
    private UUID uuid;
    
    AFUNIXSocketCredentials() {
        this.pid = -1L;
        this.uid = -1L;
        this.gids = null;
        this.uuid = null;
    }
    
    public long getPid() {
        return this.pid;
    }
    
    public long getUid() {
        return this.uid;
    }
    
    public long getGid() {
        return (this.gids == null) ? -1L : ((this.gids.length == 0) ? -1L : this.gids[0]);
    }
    
    public long[] getGids() {
        return (long[])((this.gids == null) ? null : ((long[])this.gids.clone()));
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    void setUUID(final String uuidStr) {
        this.uuid = UUID.fromString(uuidStr);
    }
    
    void setGids(final long[] gids) {
        this.gids = gids.clone();
    }
    
    public boolean isEmpty() {
        return this.pid == -1L && this.uid == -1L && (this.gids == null || this.gids.length == 0) && this.uuid == null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append('[');
        if (this == AFUNIXSocketCredentials.SAME_PROCESS) {
            sb.append("(same process)]");
            return sb.toString();
        }
        if (this.pid != -1L) {
            sb.append("pid=");
            sb.append(this.pid);
            sb.append(';');
        }
        if (this.uid != -1L) {
            sb.append("uid=");
            sb.append(this.uid);
            sb.append(';');
        }
        if (this.gids != null) {
            sb.append("gids=");
            sb.append(Arrays.toString(this.gids));
            sb.append(';');
        }
        if (this.uuid != null) {
            sb.append("uuid=");
            sb.append(this.uuid);
            sb.append(';');
        }
        if (sb.charAt() == ';') {
            sb.setLength();
        }
        sb.append(']');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + Arrays.hashCode(this.gids);
        result = 31 * result + (int)(this.pid ^ this.pid >>> 32);
        result = 31 * result + (int)(this.uid ^ this.uid >>> 32);
        result = 31 * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AFUNIXSocketCredentials other = (AFUNIXSocketCredentials)obj;
        if (!Arrays.equals(this.gids, other.gids)) {
            return false;
        }
        if (this.pid != other.pid) {
            return false;
        }
        if (this.uid != other.uid) {
            return false;
        }
        if (this.uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        }
        else if (!this.uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }
    
    public static AFUNIXSocketCredentials remotePeerCredentials() {
        try {
            RemoteServer.getClientHost();
        }
        catch (final Exception e) {
            return null;
        }
        final Socket sock = NativeUnixSocket.currentRMISocket();
        if (!(sock instanceof AFUNIXSocket)) {
            return null;
        }
        final AFUNIXSocket socket = (AFUNIXSocket)sock;
        try {
            return socket.getPeerCredentials();
        }
        catch (final IOException e2) {
            return null;
        }
    }
    
    static {
        SAME_PROCESS = new AFUNIXSocketCredentials();
    }
}
