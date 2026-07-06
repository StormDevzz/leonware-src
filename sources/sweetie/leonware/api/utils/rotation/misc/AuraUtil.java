package sweetie.leonware.api.utils.rotation.misc;

import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.api.utils.math.TimerUtil;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/rotation/misc/AuraUtil.class */
public final class AuraUtil implements QuickImports {
    private static class_243 dvdPoint = class_243.field_1353;
    private static class_243 dvdMotion = class_243.field_1353;
    private static float hitCount = 0.0f;
    private static TimerUtil attackTimer = new TimerUtil();

    @Generated
    private AuraUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void onAttack(String mode) {
        switch (mode) {
            case "Spooky Time":
                hitCount += 0.3f;
                if (hitCount >= 0.3f * 2.0f) {
                    hitCount = -0.3f;
                    break;
                }
                break;
            case "Lony Grief":
                attackTimer.reset();
                break;
            default:
                hitCount += 1.0f;
                if (hitCount >= 3.0f) {
                    hitCount = 0.0f;
                    break;
                }
                break;
        }
    }

    public static class_243 getAimpoint(class_1309 entity, String mode) {
        switch (mode) {
            case "Holy World":
                return getDvDPoint(entity);
            case "Spooky Time":
                return getSpookyTimePoint(entity);
            case "Lony Grief":
                return getDistanceBasedPoint(entity);
            case "Fun Time":
                return getDistanceBasedPoint(entity);
            default:
                return RotationUtil.getSpot(entity);
        }
    }

    public static class_243 getBestVector(class_1297 entity, float jitterOnBoxValue) {
        float f;
        float fMethod_15363 = class_3532.method_15363(mc.field_1724.method_18381(mc.field_1724.method_18376()) - entity.method_18381(entity.method_18376()), entity.method_17682() / 2.0f, entity.method_17682());
        if (mc.field_1724.method_6128()) {
            f = 10.0f;
        } else if (mc.field_1690.field_1903.method_1434() || !mc.field_1724.method_24828()) {
            f = 1.0f;
        } else {
            f = entity.method_5715() ? 0.8f : 0.6f;
        }
        double yExpand = fMethod_15363 / f;
        class_243 finalVector = entity.method_19538().method_1031(0.0d, yExpand, 0.0d);
        return finalVector.method_1031(jitterOnBoxValue, jitterOnBoxValue / 2.0f, jitterOnBoxValue);
    }

    public static class_243 getSpookyTimePoint(class_1297 entity) {
        float horValue = (entity.method_17681() / 2.0f) * hitCount;
        float verValue = (entity.method_17682() / 4.0f) * (hitCount + 1.0f);
        class_238 box = entity.method_5829();
        class_243 best = getBestVector(entity, 0.0f);
        class_243 point = new class_243(class_3532.method_15350(best.field_1352 - ((double) horValue), box.field_1323 + ((double) 0.06f), box.field_1320 - ((double) 0.06f)), class_3532.method_15350(best.field_1351 - ((double) verValue), box.field_1322 + ((double) 0.06f), box.field_1325 - ((double) 0.06f)), class_3532.method_15350(best.field_1350 + ((double) horValue), box.field_1321 + ((double) 0.06f), box.field_1324 - ((double) 0.06f)));
        return point;
    }

    public static class_243 getDistanceBasedPoint(class_1297 entity) {
        class_243 eye = mc.field_1724.method_33571();
        class_238 box = entity.method_5829();
        float attackDistance = AuraModule.getInstance().getAttackDistance() + AuraModule.getInstance().getPreDistance();
        float distanceFactor = (float) (mc.field_1724.method_19538().method_1022(entity.method_19538()) / ((double) attackDistance));
        float minY = (float) (box.field_1325 - box.field_1322);
        float clampedY = (float) Math.max(box.field_1322 + ((double) (minY * distanceFactor)), box.field_1322 + ((double) (minY * 0.3f)));
        float safePoint = entity.method_17681() * 0.4f;
        class_243 basePoint = new class_243(class_3532.method_15350(eye.field_1352, box.field_1323 + ((double) safePoint), box.field_1320 - ((double) safePoint)), class_3532.method_15350(eye.field_1351, box.field_1322, clampedY), class_3532.method_15350(eye.field_1350, box.field_1321 + ((double) safePoint), box.field_1324 - ((double) safePoint)));
        return basePoint;
    }

    public static class_243 getDvDPoint(class_1297 entity) {
        double lengthX = entity.method_5829().method_17939();
        double lengthY = entity.method_5829().method_17940() * 0.800000011920929d;
        double lengthZ = entity.method_5829().method_17941();
        if (dvdMotion.equals(class_243.field_1353)) {
            dvdMotion = new class_243(MathUtil.randomInRange(-0.05f, 0.05f), MathUtil.randomInRange(-0.05f, 0.05f), MathUtil.randomInRange(-0.05f, 0.05f));
        }
        dvdPoint = dvdPoint.method_1019(dvdMotion);
        if (dvdPoint.field_1352 >= (lengthX - 0.05d) / 2.0d) {
            dvdMotion = new class_243(-MathUtil.randomInRange(0.003f, 0.04f), dvdMotion.method_10214(), dvdMotion.method_10215());
        }
        if (dvdPoint.field_1351 >= lengthY) {
            dvdMotion = new class_243(dvdMotion.method_10216(), -MathUtil.randomInRange(0.001f, 0.03f), dvdMotion.method_10215());
        }
        if (dvdPoint.field_1350 >= (lengthZ - 0.05d) / 2.0d) {
            dvdMotion = new class_243(dvdMotion.method_10216(), dvdMotion.method_10214(), -MathUtil.randomInRange(0.003f, 0.04f));
        }
        if (dvdPoint.field_1352 <= (-(lengthX - 0.05d)) / 2.0d) {
            dvdMotion = new class_243(MathUtil.randomInRange(0.003f, 0.03f), dvdMotion.method_10214(), dvdMotion.method_10215());
        }
        if (dvdPoint.field_1351 <= 0.05d) {
            dvdMotion = new class_243(dvdMotion.method_10216(), MathUtil.randomInRange(0.001f, 0.03f), dvdMotion.method_10215());
        }
        if (dvdPoint.field_1350 <= (-(lengthZ - 0.05d)) / 2.0d) {
            dvdMotion = new class_243(dvdMotion.method_10216(), dvdMotion.method_10214(), MathUtil.randomInRange(0.003f, 0.04f));
        }
        dvdPoint.method_1031(MathUtil.randomInRange(-0.03f, 0.03f), 0.0d, MathUtil.randomInRange(-0.03f, 0.03f));
        class_243 dvdPointed = entity.method_19538().method_1019(dvdPoint);
        class_238 box = entity.method_5829();
        return new class_243(class_3532.method_15350(dvdPointed.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(dvdPointed.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(dvdPointed.field_1350, box.field_1321, box.field_1324));
    }
}
