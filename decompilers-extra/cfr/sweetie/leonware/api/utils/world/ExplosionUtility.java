/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  net.minecraft.class_1267
 *  net.minecraft.class_1280
 *  net.minecraft.class_1282
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1890
 *  net.minecraft.class_1922
 *  net.minecraft.class_1927
 *  net.minecraft.class_1927$class_4179
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_2374
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_259
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_3218
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_5134
 *  net.minecraft.class_6880
 *  net.minecraft.class_9304
 *  org.apache.commons.lang3.mutable.MutableInt
 *  org.jetbrains.annotations.Nullable
 */
package sweetie.leonware.api.utils.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Objects;
import net.minecraft.class_1267;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1922;
import net.minecraft.class_1927;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2374;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3218;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.math.PredictUtility;
import sweetie.leonware.client.features.modules.combat.crystalaura.CrystalAuraModule;

public final class ExplosionUtility
implements QuickImports {
    public static boolean terrainIgnore = false;
    public static boolean assumeBestArmor = false;
    public static class_1927 explosion;

    private ExplosionUtility() {
    }

    public static float getAutoCrystalDamage(class_243 crystalPos, class_1657 target, int predictTicks, boolean optimized) {
        if (predictTicks == 0) {
            return ExplosionUtility.getExplosionDamage(crystalPos, target, optimized);
        }
        return ExplosionUtility.getExplosionDamageWPredict(crystalPos, target, PredictUtility.predictBox(target, predictTicks), optimized);
    }

    public static float getSelfExplosionDamage(class_243 explosionPos, int predictTicks, boolean optimized) {
        return ExplosionUtility.getAutoCrystalDamage(explosionPos, (class_1657)ExplosionUtility.mc.field_1724, predictTicks, optimized);
    }

    public static float getExplosionDamage(class_243 explosionPos, class_1657 target, boolean optimized) {
        double distExposure;
        if (ExplosionUtility.mc.field_1687 == null || ExplosionUtility.mc.field_1724 == null || ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5801 || target == null) {
            return 0.0f;
        }
        MutableExplosion mutableExplosion = ExplosionUtility.getMutableExplosion(explosionPos);
        class_1282 damageSource = class_1927.method_55108((class_1937)ExplosionUtility.mc.field_1687, (class_1297)ExplosionUtility.mc.field_1724);
        if (!new class_238((double)class_3532.method_15357((double)(explosionPos.field_1352 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1351 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1350 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1352 + 13.0)), (double)class_3532.method_15357((double)(explosionPos.field_1351 + 13.0)), (double)class_3532.method_15357((double)(explosionPos.field_1350 + 13.0))).method_994(target.method_5829())) {
            return 0.0f;
        }
        if (!target.method_5659((class_1927)mutableExplosion) && !target.method_5655() && (distExposure = target.method_5707(explosionPos) / 144.0) <= 1.0) {
            terrainIgnore = ExplosionUtility.resolveIgnoreTerrain();
            double exposure = ExplosionUtility.getExposure(explosionPos, target.method_5829(), optimized);
            terrainIgnore = false;
            double finalExposure = (1.0 - distExposure) * exposure;
            float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
            if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5805) {
                toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
            } else if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5807) {
                toDamage = toDamage * 3.0f / 2.0f;
            }
            toDamage = class_1280.method_5496((class_1309)target, (float)toDamage, (class_1282)damageSource, (float)target.method_6096(), (float)((float)Objects.requireNonNull(target.method_5996(class_5134.field_23725)).method_6194()));
            if (target.method_6059(class_1294.field_5907)) {
                int resistance = 25 - (Objects.requireNonNull(target.method_6112(class_1294.field_5907)).method_5578() + 1) * 5;
                float resistanceScaled = toDamage * (float)resistance;
                toDamage = Math.max(resistanceScaled / 25.0f, 0.0f);
            }
            if (toDamage <= 0.0f) {
                toDamage = 0.0f;
            } else {
                float protAmount;
                float f = protAmount = ExplosionUtility.resolveAssumeBestArmor() ? 32.0f : (float)ExplosionUtility.getProtectionAmount(target.method_5661());
                if (protAmount > 0.0f) {
                    toDamage = class_1280.method_5497((float)toDamage, (float)protAmount);
                }
            }
            return toDamage;
        }
        return 0.0f;
    }

    public static float getExplosionDamageWPredict(class_243 explosionPos, class_1657 target, class_238 predict, boolean optimized) {
        double distExposure;
        if (ExplosionUtility.mc.field_1687 == null || ExplosionUtility.mc.field_1724 == null || ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5801) {
            return 0.0f;
        }
        if (target == null || predict == null) {
            return 0.0f;
        }
        MutableExplosion mutableExplosion = ExplosionUtility.getMutableExplosion(explosionPos);
        class_1282 damageSource = class_1927.method_55108((class_1937)ExplosionUtility.mc.field_1687, (class_1297)ExplosionUtility.mc.field_1724);
        if (!new class_238((double)class_3532.method_15357((double)(explosionPos.field_1352 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1351 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1350 - 11.0)), (double)class_3532.method_15357((double)(explosionPos.field_1352 + 13.0)), (double)class_3532.method_15357((double)(explosionPos.field_1351 + 13.0)), (double)class_3532.method_15357((double)(explosionPos.field_1350 + 13.0))).method_994(predict)) {
            return 0.0f;
        }
        if (!target.method_5659((class_1927)mutableExplosion) && !target.method_5655() && (distExposure = predict.method_1005().method_1031(0.0, -0.9, 0.0).method_1025(explosionPos) / 144.0) <= 1.0) {
            terrainIgnore = ExplosionUtility.resolveIgnoreTerrain();
            double exposure = ExplosionUtility.getExposure(explosionPos, predict, optimized);
            terrainIgnore = false;
            double finalExposure = (1.0 - distExposure) * exposure;
            float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
            if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5805) {
                toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
            } else if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5807) {
                toDamage = toDamage * 3.0f / 2.0f;
            }
            toDamage = class_1280.method_5496((class_1309)target, (float)toDamage, (class_1282)damageSource, (float)target.method_6096(), (float)((float)Objects.requireNonNull(target.method_5996(class_5134.field_23725)).method_6194()));
            if (target.method_6059(class_1294.field_5907)) {
                int resistance = 25 - (Objects.requireNonNull(target.method_6112(class_1294.field_5907)).method_5578() + 1) * 5;
                float resistanceScaled = toDamage * (float)resistance;
                toDamage = Math.max(resistanceScaled / 25.0f, 0.0f);
            }
            if (toDamage <= 0.0f) {
                toDamage = 0.0f;
            } else {
                float protAmount;
                float f = protAmount = ExplosionUtility.resolveAssumeBestArmor() ? 32.0f : (float)ExplosionUtility.getProtectionAmount(target.method_5661());
                if (protAmount > 0.0f) {
                    toDamage = class_1280.method_5497((float)toDamage, (float)protAmount);
                }
            }
            return toDamage;
        }
        return 0.0f;
    }

    public static class_3965 rayCastBlock(class_3959 context, class_2338 block) {
        return (class_3965)class_1922.method_17744((class_243)context.method_17750(), (class_243)context.method_17747(), (Object)context, (raycastContext, blockPos) -> {
            class_2680 blockState = !blockPos.equals((Object)block) ? class_2246.field_10124.method_9564() : class_2246.field_10540.method_9564();
            class_243 vec3d = raycastContext.method_17750();
            class_243 vec3d2 = raycastContext.method_17747();
            class_265 voxelShape = raycastContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, blockPos);
            class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, blockPos, voxelShape, blockState);
            class_265 voxelShape2 = class_259.method_1073();
            class_3965 blockHitResult2 = voxelShape2.method_1092(vec3d, vec3d2, blockPos);
            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, raycastContext -> {
            class_243 vec3d = raycastContext.method_17750().method_1020(raycastContext.method_17747());
            return class_3965.method_17778((class_243)raycastContext.method_17747(), (class_2350)class_2350.method_10142((double)vec3d.field_1352, (double)vec3d.field_1351, (double)vec3d.field_1350), (class_2338)class_2338.method_49638((class_2374)raycastContext.method_17747()));
        });
    }

    private static float getExposureGhost(class_243 source, class_1297 entity, class_2338 pos) {
        class_238 box = entity.method_5829();
        double d = 1.0 / ((box.field_1320 - box.field_1323) * 2.0 + 1.0);
        double e = 1.0 / ((box.field_1325 - box.field_1322) * 2.0 + 1.0);
        double f = 1.0 / ((box.field_1324 - box.field_1321) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double p;
                    double o;
                    double n = class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320);
                    class_243 vec3d = new class_243(n + g, o = class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), (p = class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324)) + h);
                    if (ExplosionUtility.raycastGhost(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    private static float getExposureRemoved(class_243 source, class_1297 entity, class_2338 pos) {
        class_238 box = entity.method_5829();
        double d = 1.0 / ((box.field_1320 - box.field_1323) * 2.0 + 1.0);
        double e = 1.0 / ((box.field_1325 - box.field_1322) * 2.0 + 1.0);
        double f = 1.0 / ((box.field_1324 - box.field_1321) * 2.0 + 1.0);
        double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double p;
                    double o;
                    double n = class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320);
                    class_243 vec3d = new class_243(n + g, o = class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), (p = class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324)) + h);
                    if (ExplosionUtility.raycastRemoved(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    public static float getExposure(class_243 source, class_238 box, boolean optimized) {
        if (!optimized) {
            return ExplosionUtility.getExposure(source, box);
        }
        int miss = 0;
        int hit = 0;
        for (int k = 0; k <= 1; ++k) {
            for (int l = 0; l <= 1; ++l) {
                for (int m = 0; m <= 1; ++m) {
                    double p;
                    double o;
                    double n = class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320);
                    class_243 vec3d = new class_243(n, o = class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), p = class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324));
                    if (ExplosionUtility.raycast(vec3d, source, ExplosionUtility.resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                        ++miss;
                    }
                    ++hit;
                }
            }
        }
        return (float)miss / (float)hit;
    }

    public static float getExposure(class_243 source, class_238 box) {
        double d = 0.4545454446934474;
        double e = 0.21739130885479366;
        double f = 0.4545454446934474;
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    double p;
                    double o;
                    double n = class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320);
                    class_243 vec3d = new class_243(n + 0.045454555306552624, o = class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), (p = class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324)) + 0.045454555306552624);
                    if (ExplosionUtility.raycast(vec3d, source, ExplosionUtility.resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return (float)i / (float)j;
    }

    private static class_3965 raycastGhost(class_3959 context, class_2338 bPos) {
        return (class_3965)class_1922.method_17744((class_243)context.method_17750(), (class_243)context.method_17747(), (Object)context, (innerContext, pos) -> {
            class_243 vec3d = innerContext.method_17750();
            class_243 vec3d2 = innerContext.method_17747();
            class_2680 blockState = !pos.equals((Object)bPos) ? ExplosionUtility.mc.field_1687.method_8320(bPos) : class_2246.field_10540.method_9564();
            class_265 voxelShape = innerContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, pos);
            class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, innerContext -> {
            class_243 vec3d = innerContext.method_17750().method_1020(innerContext.method_17747());
            return class_3965.method_17778((class_243)innerContext.method_17747(), (class_2350)class_2350.method_10142((double)vec3d.field_1352, (double)vec3d.field_1351, (double)vec3d.field_1350), (class_2338)class_2338.method_49638((class_2374)innerContext.method_17747()));
        });
    }

    private static class_3965 raycastRemoved(class_3959 context, class_2338 removedPos) {
        return (class_3965)class_1922.method_17744((class_243)context.method_17750(), (class_243)context.method_17747(), (Object)context, (innerContext, pos) -> {
            class_243 vec3d = innerContext.method_17750();
            class_243 vec3d2 = innerContext.method_17747();
            class_2680 blockState = pos.equals((Object)removedPos) ? class_2246.field_10124.method_9564() : ExplosionUtility.mc.field_1687.method_8320(pos);
            class_265 voxelShape = innerContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, pos);
            class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, innerContext -> {
            class_243 vec3d = innerContext.method_17750().method_1020(innerContext.method_17747());
            return class_3965.method_17778((class_243)innerContext.method_17747(), (class_2350)class_2350.method_10142((double)vec3d.field_1352, (double)vec3d.field_1351, (double)vec3d.field_1350), (class_2338)class_2338.method_49638((class_2374)innerContext.method_17747()));
        });
    }

    public static class_239.class_240 raycast(class_243 start, class_243 end, boolean ignoreTerrain) {
        return (class_239.class_240)class_1922.method_17744((class_243)start, (class_243)end, null, (innerContext, blockPos) -> {
            class_2680 blockState = ExplosionUtility.mc.field_1687.method_8320(blockPos);
            if (blockState.method_26204().method_9520() < 600.0f && ignoreTerrain) {
                return null;
            }
            class_3965 hitResult = blockState.method_26220((class_1922)ExplosionUtility.mc.field_1687, blockPos).method_1092(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
        }, innerContext -> class_239.class_240.field_1333);
    }

    public static int getProtectionAmount(Iterable<class_1799> equipment) {
        MutableInt mutableInt = new MutableInt();
        equipment.forEach(i -> mutableInt.add(ExplosionUtility.getProtectionAmount(i)));
        return mutableInt.intValue();
    }

    public static int getProtectionAmount(class_1799 stack) {
        int modifierBlast = 0;
        int modifier = 0;
        class_9304 enchantments = class_1890.method_57532((class_1799)stack);
        for (Object2IntMap.Entry entry : enchantments.method_57539()) {
            class_6880 enchantment = (class_6880)entry.getKey();
            String id = enchantment.method_55840();
            if ("minecraft:blast_protection".equals(id)) {
                modifierBlast = entry.getIntValue();
                continue;
            }
            if (!"minecraft:protection".equals(id)) continue;
            modifier = entry.getIntValue();
        }
        return modifierBlast * 2 + modifier;
    }

    private static MutableExplosion getMutableExplosion(class_243 explosionPos) {
        MutableExplosion mutable;
        class_1927 class_19272 = explosion;
        if (class_19272 instanceof MutableExplosion) {
            MutableExplosion existing;
            mutable = existing = (MutableExplosion)class_19272;
        } else {
            mutable = new MutableExplosion();
            explosion = mutable;
        }
        mutable.setPosition(explosionPos);
        mutable.setEntity((class_1297)ExplosionUtility.mc.field_1724);
        mutable.setPower(6.0f);
        mutable.setWorld((class_1937)ExplosionUtility.mc.field_1687);
        return mutable;
    }

    private static boolean resolveIgnoreTerrain() {
        try {
            return (Boolean)CrystalAuraModule.getInstance().processor.ignoreTerrain.getValue();
        }
        catch (Throwable ignored) {
            return terrainIgnore;
        }
    }

    private static boolean resolveAssumeBestArmor() {
        return assumeBestArmor;
    }

    private static final class MutableExplosion
    implements class_1927 {
        private class_3218 world;
        private class_1297 entity;
        private float power = 6.0f;
        private class_243 position = class_243.field_1353;

        private MutableExplosion() {
        }

        void setWorld(@Nullable class_1937 world) {
            class_3218 serverWorld;
            this.world = world instanceof class_3218 ? (serverWorld = (class_3218)world) : null;
        }

        void setEntity(class_1297 entity) {
            this.entity = entity;
        }

        void setPower(float power) {
            this.power = power;
        }

        void setPosition(class_243 position) {
            this.position = position;
        }

        public class_3218 method_64504() {
            return this.world;
        }

        public class_1927.class_4179 method_55111() {
            return class_1927.class_4179.field_18687;
        }

        public class_1309 method_8347() {
            class_1309 living;
            class_1297 class_12972 = this.entity;
            return class_12972 instanceof class_1309 ? (living = (class_1309)class_12972) : null;
        }

        public class_1297 method_46406() {
            return this.entity;
        }

        public float method_55107() {
            return this.power;
        }

        public class_243 method_55109() {
            return this.position;
        }

        public boolean method_60274() {
            return false;
        }

        public boolean method_61722() {
            return false;
        }
    }
}

