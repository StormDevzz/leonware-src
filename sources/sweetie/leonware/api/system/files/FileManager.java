package sweetie.leonware.api.system.files;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import sweetie.leonware.api.system.configs.FriendManager;
import sweetie.leonware.api.system.configs.StaffManager;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/files/FileManager.class */
public class FileManager {
    private static final FileManager instance = new FileManager();
    private final List<AbstractFile> files = new ArrayList();

    @Generated
    public static FileManager getInstance() {
        return instance;
    }

    @Generated
    public List<AbstractFile> getFiles() {
        return this.files;
    }

    public void load() {
        register(FriendManager.getInstance(), StaffManager.getInstance());
        for (AbstractFile file : this.files) {
            file.load();
        }
    }

    public void save() {
        for (AbstractFile file : this.files) {
            file.save();
        }
    }

    public void register(AbstractFile... files) {
        this.files.addAll(List.of((Object[]) files));
    }
}
