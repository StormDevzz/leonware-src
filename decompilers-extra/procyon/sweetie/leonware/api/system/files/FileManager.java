// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.files;

import lombok.Generated;
import java.util.Collection;
import java.util.Iterator;
import sweetie.leonware.api.system.configs.StaffManager;
import sweetie.leonware.api.system.configs.FriendManager;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    private static final FileManager instance;
    private final List<AbstractFile> files;
    
    public FileManager() {
        this.files = new ArrayList<AbstractFile>();
    }
    
    public void load() {
        this.register(FriendManager.getInstance(), StaffManager.getInstance());
        for (final AbstractFile file : this.files) {
            file.load();
        }
    }
    
    public void save() {
        for (final AbstractFile file : this.files) {
            file.save();
        }
    }
    
    public void register(final AbstractFile... files) {
        this.files.addAll(List.of(files));
    }
    
    @Generated
    public List<AbstractFile> getFiles() {
        return this.files;
    }
    
    @Generated
    public static FileManager getInstance() {
        return FileManager.instance;
    }
    
    static {
        instance = new FileManager();
    }
}
