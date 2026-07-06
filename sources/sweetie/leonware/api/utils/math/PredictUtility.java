package sweetie.leonware.api.utils.math;

import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/math/PredictUtility.class */
public class PredictUtility implements QuickImports {
    public static class_243 predictPosition(class_1657 entity, int ticks) {
        if (entity == null || mc.field_1687 == null) {
            return null;
        }
        class_243 posVec = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321());
        double motionX = entity.method_18798().method_10216();
        double motionZ = entity.method_18798().method_10215();
        for (int i = 0; i < ticks; i++) {
            float hbDeltaX = motionX > 0.0d ? 0.3f : -0.3f;
            float hbDeltaZ = motionZ > 0.0d ? 0.3f : -0.3f;
            if (!mc.field_1687.method_22347(class_2338.method_49638(posVec.method_1031(motionX + ((double) hbDeltaX), 0.1d, motionZ + ((double) hbDeltaZ)))) || !mc.field_1687.method_22347(class_2338.method_49638(posVec.method_1031(motionX + ((double) hbDeltaX), 1.0d, motionZ + ((double) hbDeltaZ))))) {
                motionX = 0.0d;
                motionZ = 0.0d;
            }
            posVec = posVec.method_1031(motionX, 0.0d, motionZ);
        }
        return posVec;
    }

    public static class_238 predictBox(class_1657 entity, int ticks) {
        class_243 posVec = predictPosition(entity, ticks);
        if (posVec == null) {
            return null;
        }
        return entity.method_5829().method_997(entity.method_19538().method_1035(posVec));
    }
}
