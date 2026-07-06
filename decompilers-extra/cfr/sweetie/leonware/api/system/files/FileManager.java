/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.files;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.configs.StaffManager;
import sweetie.leonware.api.system.files.AbstractFile;

public class FileManager {
    private static final FileManager instance = new FileManager();
    private final List<AbstractFile> files = new ArrayList<AbstractFile>();

    public void load() {
        this.register(FriendManager.getInstance(), StaffManager.getInstance());
        for (AbstractFile file : this.files) {
            file.load();
        }
    }

    public void save() {
        for (AbstractFile file : this.files) {
            file.save();
        }
    }

    public void register(AbstractFile ... files) {
        this.files.addAll(List.of(files));
    }

    @Generated
    public List<AbstractFile> getFiles() {
        return this.files;
    }

    @Generated
    public static FileManager getInstance() {
        return instance;
    }
}

