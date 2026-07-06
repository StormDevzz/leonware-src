/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

public class FriendManager
extends AbstractFile {
    private static final FriendManager instance = new FriendManager();

    @Override
    public String fileName() {
        return "friends";
    }

    @Generated
    public static FriendManager getInstance() {
        return instance;
    }
}

