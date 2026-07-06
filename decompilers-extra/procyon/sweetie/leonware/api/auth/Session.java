// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.auth;

import lombok.Generated;
import java.util.UUID;

public class Session
{
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
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        final Session other = (Session)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        Label_0065: {
            if (this$username == null) {
                if (other$username == null) {
                    break Label_0065;
                }
            }
            else if (this$username.equals(other$username)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        Label_0102: {
            if (this$uuid == null) {
                if (other$uuid == null) {
                    break Label_0102;
                }
            }
            else if (this$uuid.equals(other$uuid)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        Label_0139: {
            if (this$token == null) {
                if (other$token == null) {
                    break Label_0139;
                }
            }
            else if (this$token.equals(other$token)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return true;
            }
        }
        else if (this$type.equals(other$type)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Session;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $username = this.getUsername();
        result = result * 59 + (($username == null) ? 43 : $username.hashCode());
        final Object $uuid = this.getUuid();
        result = result * 59 + (($uuid == null) ? 43 : $uuid.hashCode());
        final Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Session(username=" + this.getUsername() + ", uuid=" + String.valueOf(this.getUuid()) + ", token=" + this.getToken() + ", type=" + this.getType();
    }
    
    @Generated
    public Session(final String username, final UUID uuid, final String token, final String type) {
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        this.type = type;
    }
}
