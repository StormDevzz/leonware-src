/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.auth;

import java.util.UUID;
import lombok.Generated;

public class Session {
    private final String username;
    private final UUID uuid;
    private final String token;
    private final String type;

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public UUID getUuid() {
        return this.uuid;
    }

    @Generated
    public String getToken() {
        return this.token;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        Session other = (Session)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        UUID this$uuid = this.getUuid();
        UUID other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !((Object)this$uuid).equals(other$uuid)) {
            return false;
        }
        String this$token = this.getToken();
        String other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) {
            return false;
        }
        String this$type = this.getType();
        String other$type = other.getType();
        return !(this$type == null ? other$type != null : !this$type.equals(other$type));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Session;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        UUID $uuid = this.getUuid();
        result = result * 59 + ($uuid == null ? 43 : ((Object)$uuid).hashCode());
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        String $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Session(username=" + this.getUsername() + ", uuid=" + String.valueOf(this.getUuid()) + ", token=" + this.getToken() + ", type=" + this.getType() + ")";
    }

    @Generated
    public Session(String username, UUID uuid, String token, String type) {
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        this.type = type;
    }
}

