// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.discord;

import lombok.Generated;

public class RichPresenceBuilder
{
    private final RichPresence presence;
    
    public RichPresenceBuilder() {
        this.presence = new RichPresence();
    }
    
    public RichPresenceBuilder details(final String details) {
        this.presence.details = details;
        return this;
    }
    
    public RichPresenceBuilder state(final String state) {
        this.presence.state = state;
        return this;
    }
    
    public RichPresenceBuilder largeImageKey(final String largeImageKey) {
        this.presence.largeImageKey = largeImageKey;
        return this;
    }
    
    public RichPresenceBuilder largeImageText(final String largeImageText) {
        this.presence.largeImageText = largeImageText;
        return this;
    }
    
    public RichPresenceBuilder smallImageKey(final String smallImageKey) {
        this.presence.smallImageKey = smallImageKey;
        return this;
    }
    
    public RichPresenceBuilder smallImageText(final String smallImageText) {
        this.presence.smallImageText = smallImageText;
        return this;
    }
    
    public RichPresenceBuilder startTimestamp(final long startTimestamp) {
        this.presence.startTimestamp = startTimestamp;
        return this;
    }
    
    public RichPresenceBuilder endTimestamp(final long endTimestamp) {
        this.presence.endTimestamp = endTimestamp;
        return this;
    }
    
    public RichPresenceBuilder partyId(final String partyId) {
        this.presence.partyId = partyId;
        return this;
    }
    
    public RichPresenceBuilder partySize(final int partySize) {
        this.presence.partySize = partySize;
        return this;
    }
    
    public RichPresenceBuilder partyMax(final int partyMax) {
        this.presence.partyMax = partyMax;
        return this;
    }
    
    public RichPresenceBuilder partyPrivacy(final String partyPrivacy) {
        this.presence.partyPrivacy = partyPrivacy;
        return this;
    }
    
    public RichPresenceBuilder matchSecret(final String matchSecret) {
        this.presence.matchSecret = matchSecret;
        return this;
    }
    
    public RichPresenceBuilder joinSecret(final String joinSecret) {
        this.presence.joinSecret = joinSecret;
        return this;
    }
    
    public RichPresenceBuilder spectateSecret(final String spectateSecret) {
        this.presence.spectateSecret = spectateSecret;
        return this;
    }
    
    public RichPresenceBuilder button1(final String label, final String url) {
        this.presence.button_label_1 = label;
        this.presence.button_url_1 = url;
        return this;
    }
    
    public RichPresenceBuilder button2(final String label, final String url) {
        this.presence.button_label_2 = label;
        this.presence.button_url_2 = url;
        return this;
    }
    
    public RichPresenceBuilder button(final RPCButton button) {
        if (this.presence.button_label_1 == null) {
            return this.button1(button.getLabel(), button.getUrl());
        }
        if (this.presence.button_label_2 == null) {
            return this.button2(button.getLabel(), button.getUrl());
        }
        return this;
    }
    
    public RichPresenceBuilder button(final String label, final String url) {
        return this.button(RPCButton.of(label, url));
    }
    
    public RichPresenceBuilder instance(final boolean instance) {
        this.presence.instance = (instance ? 1 : 0);
        return this;
    }
    
    public RichPresence build() {
        return this.presence;
    }
    
    public static RichPresenceBuilder builder() {
        return new RichPresenceBuilder();
    }
    
    public static class RPCButton
    {
        private final String label;
        private final String url;
        
        private RPCButton(final String label, final String url) {
            this.label = label;
            this.url = url;
        }
        
        public static RPCButton of(final String label, final String url) {
            return new RPCButton(label, url);
        }
        
        @Generated
        public String getLabel() {
            return this.label;
        }
        
        @Generated
        public String getUrl() {
            return this.url;
        }
    }
}
