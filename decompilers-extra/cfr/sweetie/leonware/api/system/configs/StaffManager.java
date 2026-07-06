/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package sweetie.leonware.api.system.configs;

import lombok.Generated;
import sweetie.leonware.api.system.files.AbstractFile;

public class StaffManager
extends AbstractFile {
    private static final StaffManager instance = new StaffManager();

    @Override
    public String fileName() {
        return "staffs";
    }

    @Generated
    public static StaffManager getInstance() {
        return instance;
    }
}

