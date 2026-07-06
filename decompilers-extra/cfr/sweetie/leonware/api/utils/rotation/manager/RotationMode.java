/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 */
package sweetie.leonware.api.utils.rotation.manager;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

public abstract class RotationMode
implements QuickImports,
ModeSetting.NamedChoice {
    private final String name;

    public RotationMode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Rotation process(Rotation currentRotation, Rotation targetRotation) {
        return this.process(currentRotation, targetRotation, null, null);
    }

    public Rotation process(Rotation currentRotation, Rotation targetRotation, class_243 vec3d) {
        return this.process(currentRotation, targetRotation, vec3d, null);
    }

    public abstract Rotation process(Rotation var1, Rotation var2, class_243 var3, class_1297 var4);
}

