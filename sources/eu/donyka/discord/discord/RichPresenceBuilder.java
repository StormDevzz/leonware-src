package eu.donyka.discord.discord;

import lombok.Generated;

/* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/discord/RichPresenceBuilder.class */
public class RichPresenceBuilder {
    private final RichPresence presence = new RichPresence();

    /* JADX INFO: loaded from: leonware-0.0.3.jar:eu/donyka/discord/discord/RichPresenceBuilder$RPCButton.class */
    public static class RPCButton {
        private final String label;
        private final String url;

        @Generated
        public String getLabel() {
            return this.label;
        }

        @Generated
        public String getUrl() {
            return this.url;
        }

        private RPCButton(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public static RPCButton of(String label, String url) {
            return new RPCButton(label, url);
        }
    }

    public RichPresenceBuilder details(String details) {
        this.presence.details = details;
        return this;
    }

    public RichPresenceBuilder state(String state) {
        this.presence.state = state;
        return this;
    }

    public RichPresenceBuilder largeImageKey(String largeImageKey) {
        this.presence.largeImageKey = largeImageKey;
        return this;
    }

    public RichPresenceBuilder largeImageText(String largeImageText) {
        this.presence.largeImageText = largeImageText;
        return this;
    }

    public RichPresenceBuilder smallImageKey(String smallImageKey) {
        this.presence.smallImageKey = smallImageKey;
        return this;
    }

    public RichPresenceBuilder smallImageText(String smallImageText) {
        this.presence.smallImageText = smallImageText;
        return this;
    }

    public RichPresenceBuilder startTimestamp(long startTimestamp) {
        this.presence.startTimestamp = startTimestamp;
        return this;
    }

    public RichPresenceBuilder endTimestamp(long endTimestamp) {
        this.presence.endTimestamp = endTimestamp;
        return this;
    }

    public RichPresenceBuilder partyId(String partyId) {
        this.presence.partyId = partyId;
        return this;
    }

    public RichPresenceBuilder partySize(int partySize) {
        this.presence.partySize = partySize;
        return this;
    }

    public RichPresenceBuilder partyMax(int partyMax) {
        this.presence.partyMax = partyMax;
        return this;
    }

    public RichPresenceBuilder partyPrivacy(String partyPrivacy) {
        this.presence.partyPrivacy = partyPrivacy;
        return this;
    }

    public RichPresenceBuilder matchSecret(String matchSecret) {
        this.presence.matchSecret = matchSecret;
        return this;
    }

    public RichPresenceBuilder joinSecret(String joinSecret) {
        this.presence.joinSecret = joinSecret;
        return this;
    }

    public RichPresenceBuilder spectateSecret(String spectateSecret) {
        this.presence.spectateSecret = spectateSecret;
        return this;
    }

    public RichPresenceBuilder button1(String label, String url) {
        this.presence.button_label_1 = label;
        this.presence.button_url_1 = url;
        return this;
    }

    public RichPresenceBuilder button2(String label, String url) {
        this.presence.button_label_2 = label;
        this.presence.button_url_2 = url;
        return this;
    }

    public RichPresenceBuilder button(RPCButton button) {
        if (this.presence.button_label_1 == null) {
            return button1(button.getLabel(), button.getUrl());
        }
        if (this.presence.button_label_2 == null) {
            return button2(button.getLabel(), button.getUrl());
        }
        return this;
    }

    public RichPresenceBuilder button(String label, String url) {
        return button(RPCButton.of(label, url));
    }

    public RichPresenceBuilder instance(boolean instance) {
        this.presence.instance = instance ? 1 : 0;
        return this;
    }

    public RichPresence build() {
        return this.presence;
    }

    public static RichPresenceBuilder builder() {
        return new RichPresenceBuilder();
    }
}
