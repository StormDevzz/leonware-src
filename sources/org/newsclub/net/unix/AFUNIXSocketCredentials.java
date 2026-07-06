package org.newsclub.net.unix;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RemoteServer;
import java.util.Arrays;
import java.util.UUID;

/* JADX INFO: loaded from: leonware-0.0.3.jar:org/newsclub/net/unix/AFUNIXSocketCredentials.class */
public final class AFUNIXSocketCredentials implements Serializable {
    private static final long serialVersionUID = 1;
    public static final AFUNIXSocketCredentials SAME_PROCESS = new AFUNIXSocketCredentials();
    private long pid = -1;
    private long uid = -1;
    private long[] gids = null;
    private UUID uuid = null;

    AFUNIXSocketCredentials() {
    }

    public long getPid() {
        return this.pid;
    }

    public long getUid() {
        return this.uid;
    }

    public long getGid() {
        if (this.gids == null || this.gids.length == 0) {
            return -1L;
        }
        return this.gids[0];
    }

    public long[] getGids() {
        if (this.gids == null) {
            return null;
        }
        return (long[]) this.gids.clone();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    void setUUID(String uuidStr) {
        this.uuid = UUID.fromString(uuidStr);
    }

    void setGids(long[] gids) {
        this.gids = (long[]) gids.clone();
    }

    public boolean isEmpty() {
        return this.pid == -1 && this.uid == -1 && (this.gids == null || this.gids.length == 0) && this.uuid == null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append('[');
        if (this == SAME_PROCESS) {
            sb.append("(same process)]");
            return sb.toString();
        }
        if (this.pid != -1) {
            sb.append("pid=");
            sb.append(this.pid);
            sb.append(';');
        }
        if (this.uid != -1) {
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
        if (sb.charAt(sb.length() - 1) == ';') {
            sb.setLength(sb.length() - 1);
        }
        sb.append(']');
        return sb.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + Arrays.hashCode(this.gids);
        return (31 * ((31 * ((31 * result) + ((int) (this.pid ^ (this.pid >>> 32))))) + ((int) (this.uid ^ (this.uid >>> 32))))) + (this.uuid == null ? 0 : this.uuid.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AFUNIXSocketCredentials other = (AFUNIXSocketCredentials) obj;
        if (!Arrays.equals(this.gids, other.gids) || this.pid != other.pid || this.uid != other.uid) {
            return false;
        }
        if (this.uuid == null) {
            if (other.uuid != null) {
                return false;
            }
            return true;
        }
        if (!this.uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    public static AFUNIXSocketCredentials remotePeerCredentials() {
        try {
            RemoteServer.getClientHost();
            Socket sock = NativeUnixSocket.currentRMISocket();
            if (!(sock instanceof AFUNIXSocket)) {
                return null;
            }
            AFUNIXSocket socket = (AFUNIXSocket) sock;
            try {
                return socket.getPeerCredentials();
            } catch (IOException e) {
                return null;
            }
        } catch (Exception e2) {
            return null;
        }
    }
}
