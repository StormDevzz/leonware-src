package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/FriendManager.class */
public class FriendManager extends AbstractFile {
    private static final FriendManager instance = new FriendManager();

    @Generated
    public static FriendManager getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.system.files.AbstractFile
    public String fileName() {
        return "friends";
    }
}
