/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package eu.donyka.discord.models;

import lombok.Generated;

public class User {
    private String userId;
    private String username;
    private String discriminator;
    private String avatar;

    public User() {
    }

    public User(String userId, String username, String discriminator, String avatar) {
        this.userId = userId;
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
    }

    @Generated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    @Generated
    public void setAvatar(String avatar) {
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

