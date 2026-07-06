// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.manager;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.module.setting.ModeSetting;
import sweetie.leonware.api.system.interfaces.QuickImports;

public abstract class RotationMode implements QuickImports, ModeSetting.NamedChoice
{
    private final String name;
    
    public RotationMode(final String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation) {
        return this.process(currentRotation, targetRotation, null, null);
    }
    
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d) {
        return this.process(currentRotation, targetRotation, vec3d, null);
    }
    
    public abstract Rotation process(final Rotation p0, final Rotation p1, final class_243 p2, final class_1297 p3);
}
