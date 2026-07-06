// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

public class StaffManager extends AbstractFile
{
    private static final StaffManager instance;
    
    @Override
    public String fileName() {
        return "staffs";
    }
    
    @Generated
    public static StaffManager getInstance() {
        return StaffManager.instance;
    }
    
    static {
        instance = new StaffManager();
    }
}
