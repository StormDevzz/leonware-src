// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.rotations;

import net.minecraft.class_1297;
import net.minecraft.class_243;
import sweetie.leonware.api.utils.rotation.manager.Rotation;
import sweetie.leonware.api.utils.rotation.manager.RotationMode;

public class InstantRotation extends RotationMode
{
    public InstantRotation() {
        super("Instant");
    }
    
    @Override
    public Rotation process(final Rotation currentRotation, final Rotation targetRotation, final class_243 vec3d, final class_1297 entity) {
        return targetRotation;
    }
}
