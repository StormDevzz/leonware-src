package sweetie.leonware.api.auth;

import java.util.UUID;
import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/auth/Session.class */
public class Session {
    private final String username;
    private final UUID uuid;
    private final String token;
    private final String type;

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Session)) {
            return false;
        }
        Session other = (Session) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$username = getUsername();
        Object other$username = other.getUsername();
        if (this$username == null) {
            if (other$username != null) {
                return false;
            }
        } else if (!this$username.equals(other$username)) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid == null) {
            if (other$uuid != null) {
                return false;
            }
        } else if (!this$uuid.equals(other$uuid)) {
            return false;
        }
        Object this$token = getToken();
        Object other$token = other.getToken();
        if (this$token == null) {
            if (other$token != null) {
                return false;
            }
        } else if (!this$token.equals(other$token)) {
            return false;
        }
        Object this$type = getType();
        Object other$type = other.getType();
        return this$type == null ? other$type == null : this$type.equals(other$type);
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Session;
    }

    @Generated
    public int hashCode() {
        Object $username = getUsername();
        int result = (1 * 59) + ($username == null ? 43 : $username.hashCode());
        Object $uuid = getUuid();
        int result2 = (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $token = getToken();
        int result3 = (result2 * 59) + ($token == null ? 43 : $token.hashCode());
        Object $type = getType();
        return (result3 * 59) + ($type == null ? 43 : $type.hashCode());
    }

    @Generated
    public String toString() {
        return "Session(username=" + getUsername() + ", uuid=" + String.valueOf(getUuid()) + ", token=" + getToken() + ", type=" + getType() + ")";
    }

    @Generated
    public Session(String username, UUID uuid, String token, String type) {
        this.username = username;
        this.uuid = uuid;
        this.token = token;
        this.type = type;
    }

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
}
