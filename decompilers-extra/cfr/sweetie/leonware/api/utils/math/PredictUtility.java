/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 */
package sweetie.leonware.api.utils.math;

import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

public class PredictUtility
implements QuickImports {
    public static class_243 predictPosition(class_1657 entity, int ticks) {
        if (entity == null || PredictUtility.mc.field_1687 == null) {
            return null;
        }
        class_243 posVec = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
        double motionX = entity.method_18798().method_10216();
        double motionZ = entity.method_18798().method_10215();
        for (int i = 0; i < ticks; ++i) {
            float hbDeltaZ;
            float hbDeltaX = motionX > 0.0 ? 0.3f : -0.3f;
            float f = hbDeltaZ = motionZ > 0.0 ? 0.3f : -0.3f;
            if (!PredictUtility.mc.field_1687.method_22347(class_2338.method_49638((class_2374)posVec.method_1031(motionX + (double)hbDeltaX, 0.1, motionZ + (double)hbDeltaZ))) || !PredictUtility.mc.field_1687.method_22347(class_2338.method_49638((class_2374)posVec.method_1031(motionX + (double)hbDeltaX, 1.0, motionZ + (double)hbDeltaZ)))) {
                motionX = 0.0;
                motionZ = 0.0;
            }
            posVec = posVec.method_1031(motionX, 0.0, motionZ);
        }
        return posVec;
    }

    public static class_238 predictBox(class_1657 entity, int ticks) {
        class_243 posVec = PredictUtility.predictPosition(entity, ticks);
        if (posVec == null) {
            return null;
        }
        return entity.method_5829().method_997(entity.method_19538().method_1035(posVec));
    }
}

