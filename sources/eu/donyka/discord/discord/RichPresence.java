package eu.donyka.discord.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/discord/RichPresence.class */
public class RichPresence {
    public String largeImageKey;
    public String largeImageText;
    public String smallImageText;
    public String partyPrivacy;
    public long startTimestamp;
    public String button_label_1;
    public int instance;
    public String partyId;
    public int partySize;
    public long endTimestamp;
    public String details;
    public String joinSecret;
    public String spectateSecret;
    public String smallImageKey;
    public String matchSecret;
    public String button_url_2;
    public String button_label_2;
    public String state;
    public String button_url_1;
    public int partyMax;

    public static RichPresenceBuilder builder() {
        return RichPresenceBuilder.builder();
    }

    public JsonObject toJson(long pid, long nonce) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("nonce", Long.valueOf(nonce));
        jsonObject.addProperty("cmd", "SET_ACTIVITY");
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("pid", Long.valueOf(pid));
        JsonObject jsonObject3 = new JsonObject();
        if (isNotNullOrEmpty(this.state)) {
            jsonObject3.addProperty("state", this.state);
        }
        if (isNotNullOrEmpty(this.details)) {
            jsonObject3.addProperty("details", this.details);
        }
        if (this.startTimestamp != 0 || this.endTimestamp != 0) {
            JsonObject timestamps = new JsonObject();
            if (this.startTimestamp != 0) {
                timestamps.addProperty("start", Long.valueOf(this.startTimestamp));
            }
            if (this.endTimestamp != 0) {
                timestamps.addProperty("end", Long.valueOf(this.endTimestamp));
            }
            jsonObject3.add("timestamps", timestamps);
        }
        if (isNotNullOrEmpty(this.largeImageKey) || isNotNullOrEmpty(this.largeImageText) || isNotNullOrEmpty(this.smallImageKey) || isNotNullOrEmpty(this.smallImageText)) {
            JsonObject assets = new JsonObject();
            if (isNotNullOrEmpty(this.largeImageKey)) {
                assets.addProperty("large_image", this.largeImageKey);
            }
            if (isNotNullOrEmpty(this.largeImageText)) {
                assets.addProperty("large_text", this.largeImageText);
            }
            if (isNotNullOrEmpty(this.smallImageKey)) {
                assets.addProperty("small_image", this.smallImageKey);
            }
            if (isNotNullOrEmpty(this.smallImageText)) {
                assets.addProperty("small_text", this.smallImageText);
            }
            jsonObject3.add("assets", assets);
        }
        if (isNotNullOrEmpty(this.partyId) || this.partySize > 0 || this.partyMax > 0) {
            JsonObject party = new JsonObject();
            if (isNotNullOrEmpty(this.partyId)) {
                party.addProperty("id", this.partyId);
            }
            if (this.partySize != 0) {
                JsonArray size = new JsonArray();
                size.add(Integer.valueOf(this.partySize));
                if (this.partyMax > 0) {
                    size.add(Integer.valueOf(this.partyMax));
                }
                party.add("size", size);
            }
            if (isNotNullOrEmpty(this.partyPrivacy)) {
                party.addProperty("privacy", this.partyPrivacy);
            }
            jsonObject3.add("party", party);
        }
        if (isNotNullOrEmpty(this.matchSecret) || isNotNullOrEmpty(this.spectateSecret) || isNotNullOrEmpty(this.joinSecret)) {
            JsonObject secrets = new JsonObject();
            if (isNotNullOrEmpty(this.matchSecret)) {
                secrets.addProperty("match", this.matchSecret);
            }
            if (isNotNullOrEmpty(this.joinSecret)) {
                secrets.addProperty("join", this.joinSecret);
            }
            if (isNotNullOrEmpty(this.spectateSecret)) {
                secrets.addProperty("spectate", this.spectateSecret);
            }
            jsonObject3.add("secrets", secrets);
        }
        JsonArray buttons = new JsonArray();
        if (isNotNullOrEmpty(this.button_label_1) && isNotNullOrEmpty(this.button_url_1)) {
            JsonObject btn1 = new JsonObject();
            btn1.addProperty("label", this.button_label_1.substring(0, Math.min(this.button_label_1.length(), 32)));
            btn1.addProperty("url", this.button_url_1);
            buttons.add(btn1);
        }
        if (isNotNullOrEmpty(this.button_label_2) && isNotNullOrEmpty(this.button_url_2)) {
            JsonObject btn2 = new JsonObject();
            btn2.addProperty("label", this.button_label_2.substring(0, Math.min(this.button_label_2.length(), 32)));
            btn2.addProperty("url", this.button_url_2);
            buttons.add(btn2);
        }
        if (buttons.size() > 0) {
            jsonObject3.add("buttons", buttons);
        }
        jsonObject3.addProperty("type", (Number) 0);
        jsonObject3.addProperty("instance", Boolean.valueOf(this.instance != 0));
        jsonObject2.add("activity", jsonObject3);
        jsonObject.add("args", jsonObject2);
        return jsonObject;
    }

    private boolean isNotNullOrEmpty(String input) {
        return (input == null || input.trim().isEmpty()) ? false : true;
    }
}
