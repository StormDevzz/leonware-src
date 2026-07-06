// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.math;

import net.minecraft.class_238;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_1657;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class PredictUtility implements QuickImports
{
    public static class_243 predictPosition(final class_1657 entity, final int ticks) {
        if (entity == null || PredictUtility.mc.field_1687 == null) {
            return null;
        }
        class_243 posVec = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
        double motionX = entity.method_18798().method_10216();
        double motionZ = entity.method_18798().method_10215();
        for (int i = 0; i < ticks; ++i) {
            final float hbDeltaX = (motionX > 0.0) ? 0.3f : -0.3f;
            final float hbDeltaZ = (motionZ > 0.0) ? 0.3f : -0.3f;
            if (!PredictUtility.mc.field_1687.method_22347(class_2338.method_49638((class_2374)posVec.method_1031(motionX + hbDeltaX, 0.1, motionZ + hbDeltaZ))) || !PredictUtility.mc.field_1687.method_22347(class_2338.method_49638((class_2374)posVec.method_1031(motionX + hbDeltaX, 1.0, motionZ + hbDeltaZ)))) {
                motionX = 0.0;
                motionZ = 0.0;
            }
            posVec = posVec.method_1031(motionX, 0.0, motionZ);
        }
        return posVec;
    }
    
    public static class_238 predictBox(final class_1657 entity, final int ticks) {
        final class_243 posVec = predictPosition(entity, ticks);
        if (posVec == null) {
            return null;
        }
        return entity.method_5829().method_997(entity.method_19538().method_1035(posVec));
    }
}
