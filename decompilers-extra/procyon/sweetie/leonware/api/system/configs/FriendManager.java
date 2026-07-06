// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

public class FriendManager extends AbstractFile
{
    private static final FriendManager instance;
    
    @Override
    public String fileName() {
        return "friends";
    }
    
    @Generated
    public static FriendManager getInstance() {
        return FriendManager.instance;
    }
    
    static {
        instance = new FriendManager();
    }
}
