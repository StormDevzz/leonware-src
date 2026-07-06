/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
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

public final class AuraUtil
implements QuickImports {
    private static class_243 dvdPoint = class_243.field_1353;
    private static class_243 dvdMotion = class_243.field_1353;
    private static float hitCount = 0.0f;
    private static TimerUtil attackTimer = new TimerUtil();

    public static void onAttack(String mode) {
        switch (mode) {
            case "Spooky Time": {
                float hits = 0.3f;
                hitCount += hits;
                if (!(hitCount >= hits * 2.0f)) break;
                hitCount = -hits;
                break;
            }
            case "Lony Grief": {
                attackTimer.reset();
                break;
            }
            default: {
                hitCount += 1.0f;
                if (!(hitCount >= 3.0f)) break;
                hitCount = 0.0f;
            }
        }
    }

    public static class_243 getAimpoint(class_1309 entity, String mode) {
        switch (mode) {
            case "Holy World": {
                return AuraUtil.getDvDPoint((class_1297)entity);
            }
            case "Spooky Time": {
                return AuraUtil.getSpookyTimePoint((class_1297)entity);
            }
            case "Lony Grief": {
                return AuraUtil.getDistanceBasedPoint((class_1297)entity);
            }
            case "Fun Time": {
                return AuraUtil.getDistanceBasedPoint((class_1297)entity);
            }
        }
        return RotationUtil.getSpot((class_1297)entity);
    }

    public static class_243 getBestVector(class_1297 entity, float jitterOnBoxValue) {
        double yExpand = class_3532.method_15363((float)(AuraUtil.mc.field_1724.method_18381(AuraUtil.mc.field_1724.method_18376()) - entity.method_18381(entity.method_18376())), (float)(entity.method_17682() / 2.0f), (float)entity.method_17682()) / (AuraUtil.mc.field_1724.method_6128() ? 10.0f : (!AuraUtil.mc.field_1690.field_1903.method_1434() && AuraUtil.mc.field_1724.method_24828() ? (entity.method_5715() ? 0.8f : 0.6f) : 1.0f));
        class_243 finalVector = entity.method_19538().method_1031(0.0, yExpand, 0.0);
        return finalVector.method_1031((double)jitterOnBoxValue, (double)(jitterOnBoxValue / 2.0f), (double)jitterOnBoxValue);
    }

    public static class_243 getSpookyTimePoint(class_1297 entity) {
        float safe = 0.06f;
        float horValue = entity.method_17681() / 2.0f * hitCount;
        float verValue = entity.method_17682() / 4.0f * (hitCount + 1.0f);
        class_238 box = entity.method_5829();
        class_243 best = AuraUtil.getBestVector(entity, 0.0f);
        class_243 point = new class_243(class_3532.method_15350((double)(best.field_1352 - (double)horValue), (double)(box.field_1323 + (double)safe), (double)(box.field_1320 - (double)safe)), class_3532.method_15350((double)(best.field_1351 - (double)verValue), (double)(box.field_1322 + (double)safe), (double)(box.field_1325 - (double)safe)), class_3532.method_15350((double)(best.field_1350 + (double)horValue), (double)(box.field_1321 + (double)safe), (double)(box.field_1324 - (double)safe)));
        return point;
    }

    public static class_243 getDistanceBasedPoint(class_1297 entity) {
        class_243 eye = AuraUtil.mc.field_1724.method_33571();
        class_238 box = entity.method_5829();
        float attackDistance = AuraModule.getInstance().getAttackDistance() + AuraModule.getInstance().getPreDistance();
        float distanceFactor = (float)(AuraUtil.mc.field_1724.method_19538().method_1022(entity.method_19538()) / (double)attackDistance);
        float minY = (float)(box.field_1325 - box.field_1322);
        float clampedY = (float)Math.max(box.field_1322 + (double)(minY * distanceFactor), box.field_1322 + (double)(minY * 0.3f));
        float safePoint = entity.method_17681() * 0.4f;
        class_243 basePoint = new class_243(class_3532.method_15350((double)eye.field_1352, (double)(box.field_1323 + (double)safePoint), (double)(box.field_1320 - (double)safePoint)), class_3532.method_15350((double)eye.field_1351, (double)box.field_1322, (double)clampedY), class_3532.method_15350((double)eye.field_1350, (double)(box.field_1321 + (double)safePoint), (double)(box.field_1324 - (double)safePoint)));
        return basePoint;
    }

    public static class_243 getDvDPoint(class_1297 entity) {
        float minMotionXZ = 0.003f;
        float maxMotionXZ = 0.04f;
        float minMotionY = 0.001f;
        float maxMotionY = 0.03f;
        double lengthX = entity.method_5829().method_17939();
        double lengthY = entity.method_5829().method_17940() * (double)0.8f;
        double lengthZ = entity.method_5829().method_17941();
        if (dvdMotion.equals((Object)class_243.field_1353)) {
            dvdMotion = new class_243((double)MathUtil.randomInRange(-0.05f, 0.05f), (double)MathUtil.randomInRange(-0.05f, 0.05f), (double)MathUtil.randomInRange(-0.05f, 0.05f));
        }
        dvdPoint = dvdPoint.method_1019(dvdMotion);
        if (AuraUtil.dvdPoint.field_1352 >= (lengthX - 0.05) / 2.0) {
            dvdMotion = new class_243((double)(-MathUtil.randomInRange(minMotionXZ, maxMotionXZ)), dvdMotion.method_10214(), dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1351 >= lengthY) {
            dvdMotion = new class_243(dvdMotion.method_10216(), (double)(-MathUtil.randomInRange(minMotionY, maxMotionY)), dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1350 >= (lengthZ - 0.05) / 2.0) {
            dvdMotion = new class_243(dvdMotion.method_10216(), dvdMotion.method_10214(), (double)(-MathUtil.randomInRange(minMotionXZ, maxMotionXZ)));
        }
        if (AuraUtil.dvdPoint.field_1352 <= -(lengthX - 0.05) / 2.0) {
            dvdMotion = new class_243((double)MathUtil.randomInRange(minMotionXZ, 0.03f), dvdMotion.method_10214(), dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1351 <= 0.05) {
            dvdMotion = new class_243(dvdMotion.method_10216(), (double)MathUtil.randomInRange(minMotionY, maxMotionY), dvdMotion.method_10215());
        }
        if (AuraUtil.dvdPoint.field_1350 <= -(lengthZ - 0.05) / 2.0) {
            dvdMotion = new class_243(dvdMotion.method_10216(), dvdMotion.method_10214(), (double)MathUtil.randomInRange(minMotionXZ, maxMotionXZ));
        }
        dvdPoint.method_1031((double)MathUtil.randomInRange(-0.03f, 0.03f), 0.0, (double)MathUtil.randomInRange(-0.03f, 0.03f));
        class_243 dvdPointed = entity.method_19538().method_1019(dvdPoint);
        class_238 box = entity.method_5829();
        return new class_243(class_3532.method_15350((double)dvdPointed.field_1352, (double)box.field_1323, (double)box.field_1320), class_3532.method_15350((double)dvdPointed.field_1351, (double)box.field_1322, (double)box.field_1325), class_3532.method_15350((double)dvdPointed.field_1350, (double)box.field_1321, (double)box.field_1324));
    }

    @Generated
    private AuraUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

