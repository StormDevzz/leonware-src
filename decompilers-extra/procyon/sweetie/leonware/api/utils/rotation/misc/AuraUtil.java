// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.rotation.misc;

import lombok.Generated;
import sweetie.leonware.api.utils.math.MathUtil;
import sweetie.leonware.client.features.modules.combat.AuraModule;
import net.minecraft.class_238;
import net.minecraft.class_3532;
import sweetie.leonware.api.utils.rotation.RotationUtil;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import sweetie.leonware.api.utils.math.TimerUtil;
import net.minecraft.class_243;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class AuraUtil implements QuickImports
{
    private static class_243 dvdPoint;
    private static class_243 dvdMotion;
    private static float hitCount;
    private static TimerUtil attackTimer;
    
    public static void onAttack(final String mode) {
        switch (mode) {
            case "Spooky Time": {
                final float hits = 0.3f;
                AuraUtil.hitCount += hits;
                if (AuraUtil.hitCount >= hits * 2.0f) {
                    AuraUtil.hitCount = -hits;
                }
                break;
            }
            case "Lony Grief": {
                AuraUtil.attackTimer.reset();
                break;
            }
            default: {
                ++AuraUtil.hitCount;
                if (AuraUtil.hitCount >= 3.0f) {
                    AuraUtil.hitCount = 0.0f;
                    break;
                }
                break;
            }
        }
    }
    
    public static class_243 getAimpoint(final class_1309 entity, final String mode) {
        switch (mode) {
            case "Holy World": {
                return getDvDPoint((class_1297)entity);
            }
            case "Spooky Time": {
                return getSpookyTimePoint((class_1297)entity);
            }
            case "Lony Grief": {
                return getDistanceBasedPoint((class_1297)entity);
            }
            case "Fun Time": {
                return getDistanceBasedPoint((class_1297)entity);
            }
            default: {
                return RotationUtil.getSpot((class_1297)entity);
            }
        }
    }
    
    public static class_243 getBestVector(final class_1297 entity, final float jitterOnBoxValue) {
        final double yExpand = class_3532.method_15363(AuraUtil.mc.field_1724.method_18381(AuraUtil.mc.field_1724.method_18376()) - entity.method_18381(entity.method_18376()), entity.method_17682() / 2.0f, entity.method_17682()) / (AuraUtil.mc.field_1724.method_6128() ? 10.0f : ((!AuraUtil.mc.field_1690.field_1903.method_1434() && AuraUtil.mc.field_1724.method_24828()) ? (entity.method_5715() ? 0.8f : 0.6f) : 1.0f));
        final class_243 finalVector = entity.method_19538().method_1031(0.0, yExpand, 0.0);
        return finalVector.method_1031((double)jitterOnBoxValue, (double)(jitterOnBoxValue / 2.0f), (double)jitterOnBoxValue);
    }
    
    public static class_243 getSpookyTimePoint(final class_1297 entity) {
        final float safe = 0.06f;
        final float horValue = entity.method_17681() / 2.0f * AuraUtil.hitCount;
        final float verValue = entity.method_17682() / 4.0f * (AuraUtil.hitCount + 1.0f);
        final class_238 box = entity.method_5829();
        final class_243 best = getBestVector(entity, 0.0f);
        final class_243 point = new class_243(class_3532.method_15350(best.field_1352 - horValue, box.field_1323 + safe, box.field_1320 - safe), class_3532.method_15350(best.field_1351 - verValue, box.field_1322 + safe, box.field_1325 - safe), class_3532.method_15350(best.field_1350 + horValue, box.field_1321 + safe, box.field_1324 - safe));
        return point;
    }
    
    public static class_243 getDistanceBasedPoint(final class_1297 entity) {
        final class_243 eye = AuraUtil.mc.field_1724.method_33571();
        final class_238 box = entity.method_5829();
        final float attackDistance = AuraModule.getInstance().getAttackDistance() + AuraModule.getInstance().getPreDistance();
        final float distanceFactor = (float)(AuraUtil.mc.field_1724.method_19538().method_1022(entity.method_19538()) / attackDistance);
        final float minY = (float)(box.field_1325 - box.field_1322);
        final float clampedY = (float)Math.max(box.field_1322 + minY * distanceFactor, box.field_1322 + minY * 0.3f);
        final float safePoint = entity.method_17681() * 0.4f;
        final class_243 basePoint = new class_243(class_3532.method_15350(eye.field_1352, box.field_1323 + safePoint, box.field_1320 - safePoint), class_3532.method_15350(eye.field_1351, box.field_1322, (double)clampedY), class_3532.method_15350(eye.field_1350, box.field_1321 + safePoint, box.field_1324 - safePoint));
        return basePoint;
    }
    
    public static class_243 getDvDPoint(final class_1297 entity) {
        final float minMotionXZ = 0.003f;
        final float maxMotionXZ = 0.04f;
        final float minMotionY = 0.001f;
        final float maxMotionY = 0.03f;
        final double lengthX = entity.method_5829().method_17939();
        final double lengthY = entity.method_5829().method_17940() * 0.800000011920929;
        final double lengthZ = entity.method_5829().method_17941();
        if (AuraUtil.dvdMotion.equals((Object)class_243.field_1353)) {
            AuraUtil.dvdMotion = new class_243((double)MathUtil.randomInRange(-0.05f, 0.05f), (double)MathUtil.randomInRange(-0.05f, 0.05f), (double)MathUtil.randomInRange(-0.05f, 0.05f));
        }
        AuraUtil.dvdPoint = AuraUtil.dvdPoint.method_1019(AuraUtil.dvdMotion);
        if (AuraUtil.dvdPoint.field_1352 >= (lengthX - 0.05) / 2.0) {
            AuraUtil.dvdMotion = new class_243((double)(-MathUtil.randomInRange(minMotionXZ, maxMotionXZ)), AuraUtil.dvdMotion.method_10214(), AuraUtil.dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1351 >= lengthY) {
            AuraUtil.dvdMotion = new class_243(AuraUtil.dvdMotion.method_10216(), (double)(-MathUtil.randomInRange(minMotionY, maxMotionY)), AuraUtil.dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1350 >= (lengthZ - 0.05) / 2.0) {
            AuraUtil.dvdMotion = new class_243(AuraUtil.dvdMotion.method_10216(), AuraUtil.dvdMotion.method_10214(), (double)(-MathUtil.randomInRange(minMotionXZ, maxMotionXZ)));
        }
        if (AuraUtil.dvdPoint.field_1352 <= -(lengthX - 0.05) / 2.0) {
            AuraUtil.dvdMotion = new class_243((double)MathUtil.randomInRange(minMotionXZ, 0.03f), AuraUtil.dvdMotion.method_10214(), AuraUtil.dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1351 <= 0.05) {
            AuraUtil.dvdMotion = new class_243(AuraUtil.dvdMotion.method_10216(), (double)MathUtil.randomInRange(minMotionY, maxMotionY), AuraUtil.dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1350 <= -(lengthZ - 0.05) / 2.0) {
            AuraUtil.dvdMotion = new class_243(AuraUtil.dvdMotion.method_10216(), AuraUtil.dvdMotion.method_10214(), (double)MathUtil.randomInRange(minMotionXZ, maxMotionXZ));
        }
        AuraUtil.dvdPoint.method_1031((double)MathUtil.randomInRange(-0.03f, 0.03f), 0.0, (double)MathUtil.randomInRange(-0.03f, 0.03f));
        final class_243 dvdPointed = entity.method_19538().method_1019(AuraUtil.dvdPoint);
        final class_238 box = entity.method_5829();
        return new class_243(class_3532.method_15350(dvdPointed.field_1352, box.field_1323, box.field_1320), class_3532.method_15350(dvdPointed.field_1351, box.field_1322, box.field_1325), class_3532.method_15350(dvdPointed.field_1350, box.field_1321, box.field_1324));
    }
    
    @Generated
    private AuraUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        AuraUtil.dvdPoint = class_243.field_1353;
        AuraUtil.dvdMotion = class_243.field_1353;
        AuraUtil.hitCount = 0.0f;
        AuraUtil.attackTimer = new TimerUtil();
    }
}
