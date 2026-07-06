package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/system/configs/StaffManager.class */
public class StaffManager extends AbstractFile {
    private static final StaffManager instance = new StaffManager();

    @Generated
    public static StaffManager getInstance() {
        return instance;
    }

    @Override // sweetie.leonware.api.system.files.AbstractFile
    public String fileName() {
        return "staffs";
    }
}
