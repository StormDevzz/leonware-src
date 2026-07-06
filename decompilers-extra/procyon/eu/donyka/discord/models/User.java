// 
// Decompiled by Procyon v0.6.0
// 

package eu.donyka.discord.models;

import lombok.Generated;

public class User
{
    private String userId;
    private String username;
    private String discriminator;
    private String avatar;
    
    public User() {
    }
    
    public User(final String userId, final String username, final String discriminator, final String avatar) {
        this.userId = userId;
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }
    
    @Generated
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    @Generated
    public void setUsername(final String username) {
        this.username = username;
    }
    
    @Generated
    public void setDiscriminator(final String discriminator) {
        this.discriminator = discriminator;
    }
    
    @Generated
    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }
    
    @Generated
    public String getUserId() {
        return this.userId;
    }
    
    @Generated
    public String getUsername() {
        return this.username;
    }
    
    @Generated
    public String getDiscriminator() {
        return this.discriminator;
    }
    
    @Generated
    public String getAvatar() {
        return this.avatar;
    }
}
