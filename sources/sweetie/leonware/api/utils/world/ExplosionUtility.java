package sweetie.leonware.api.utils.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Objects;
import net.minecraft.class_1267;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1324;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1922;
import net.minecraft.class_1927;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/world/ExplosionUtility.class */
public final class ExplosionUtility implements QuickImports {
    public static boolean terrainIgnore = false;
    public static boolean assumeBestArmor = false;
    public static class_1927 explosion;

    private ExplosionUtility() {
    }

    public static float getAutoCrystalDamage(class_243 crystalPos, class_1657 target, int predictTicks, boolean optimized) {
        if (predictTicks == 0) {
            return getExplosionDamage(crystalPos, target, optimized);
        }
        return getExplosionDamageWPredict(crystalPos, target, PredictUtility.predictBox(target, predictTicks), optimized);
    }

    public static float getSelfExplosionDamage(class_243 explosionPos, int predictTicks, boolean optimized) {
        return getAutoCrystalDamage(explosionPos, mc.field_1724, predictTicks, optimized);
    }

    public static float getExplosionDamage(class_243 explosionPos, class_1657 target, boolean optimized) {
        if (mc.field_1687 == null || mc.field_1724 == null || mc.field_1687.method_8407() == class_1267.field_5801 || target == null) {
            return 0.0f;
        }
        MutableExplosion mutableExplosion = getMutableExplosion(explosionPos);
        class_1282 damageSource = class_1927.method_55108(mc.field_1687, mc.field_1724);
        if (new class_238(class_3532.method_15357(explosionPos.field_1352 - 11.0d), class_3532.method_15357(explosionPos.field_1351 - 11.0d), class_3532.method_15357(explosionPos.field_1350 - 11.0d), class_3532.method_15357(explosionPos.field_1352 + 13.0d), class_3532.method_15357(explosionPos.field_1351 + 13.0d), class_3532.method_15357(explosionPos.field_1350 + 13.0d)).method_994(target.method_5829()) && !target.method_5659(mutableExplosion) && !target.method_5655()) {
            double distExposure = target.method_5707(explosionPos) / 144.0d;
            if (distExposure <= 1.0d) {
                terrainIgnore = resolveIgnoreTerrain();
                double exposure = getExposure(explosionPos, target.method_5829(), optimized);
                terrainIgnore = false;
                double finalExposure = (1.0d - distExposure) * exposure;
                float toDamage = (float) Math.floor(((((finalExposure * finalExposure) + finalExposure) / 2.0d) * 7.0d * 12.0d) + 1.0d);
                if (mc.field_1687.method_8407() == class_1267.field_5805) {
                    toDamage = Math.min((toDamage / 2.0f) + 1.0f, toDamage);
                } else if (mc.field_1687.method_8407() == class_1267.field_5807) {
                    toDamage = (toDamage * 3.0f) / 2.0f;
                }
                float toDamage2 = class_1280.method_5496(target, toDamage, damageSource, target.method_6096(), (float) ((class_1324) Objects.requireNonNull(target.method_5996(class_5134.field_23725))).method_6194());
                if (target.method_6059(class_1294.field_5907)) {
                    int resistance = 25 - ((((class_1293) Objects.requireNonNull(target.method_6112(class_1294.field_5907))).method_5578() + 1) * 5);
                    float resistanceScaled = toDamage2 * resistance;
                    toDamage2 = Math.max(resistanceScaled / 25.0f, 0.0f);
                }
                if (toDamage2 <= 0.0f) {
                    toDamage2 = 0.0f;
                } else {
                    float protAmount = resolveAssumeBestArmor() ? 32.0f : getProtectionAmount((Iterable<class_1799>) target.method_5661());
                    if (protAmount > 0.0f) {
                        toDamage2 = class_1280.method_5497(toDamage2, protAmount);
                    }
                }
                return toDamage2;
            }
            return 0.0f;
        }
        return 0.0f;
    }

    public static float getExplosionDamageWPredict(class_243 explosionPos, class_1657 target, class_238 predict, boolean optimized) {
        if (mc.field_1687 == null || mc.field_1724 == null || mc.field_1687.method_8407() == class_1267.field_5801 || target == null || predict == null) {
            return 0.0f;
        }
        MutableExplosion mutableExplosion = getMutableExplosion(explosionPos);
        class_1282 damageSource = class_1927.method_55108(mc.field_1687, mc.field_1724);
        if (new class_238(class_3532.method_15357(explosionPos.field_1352 - 11.0d), class_3532.method_15357(explosionPos.field_1351 - 11.0d), class_3532.method_15357(explosionPos.field_1350 - 11.0d), class_3532.method_15357(explosionPos.field_1352 + 13.0d), class_3532.method_15357(explosionPos.field_1351 + 13.0d), class_3532.method_15357(explosionPos.field_1350 + 13.0d)).method_994(predict) && !target.method_5659(mutableExplosion) && !target.method_5655()) {
            double distExposure = predict.method_1005().method_1031(0.0d, -0.9d, 0.0d).method_1025(explosionPos) / 144.0d;
            if (distExposure <= 1.0d) {
                terrainIgnore = resolveIgnoreTerrain();
                double exposure = getExposure(explosionPos, predict, optimized);
                terrainIgnore = false;
                double finalExposure = (1.0d - distExposure) * exposure;
                float toDamage = (float) Math.floor(((((finalExposure * finalExposure) + finalExposure) / 2.0d) * 7.0d * 12.0d) + 1.0d);
                if (mc.field_1687.method_8407() == class_1267.field_5805) {
                    toDamage = Math.min((toDamage / 2.0f) + 1.0f, toDamage);
                } else if (mc.field_1687.method_8407() == class_1267.field_5807) {
                    toDamage = (toDamage * 3.0f) / 2.0f;
                }
                float toDamage2 = class_1280.method_5496(target, toDamage, damageSource, target.method_6096(), (float) ((class_1324) Objects.requireNonNull(target.method_5996(class_5134.field_23725))).method_6194());
                if (target.method_6059(class_1294.field_5907)) {
                    int resistance = 25 - ((((class_1293) Objects.requireNonNull(target.method_6112(class_1294.field_5907))).method_5578() + 1) * 5);
                    float resistanceScaled = toDamage2 * resistance;
                    toDamage2 = Math.max(resistanceScaled / 25.0f, 0.0f);
                }
                if (toDamage2 <= 0.0f) {
                    toDamage2 = 0.0f;
                } else {
                    float protAmount = resolveAssumeBestArmor() ? 32.0f : getProtectionAmount((Iterable<class_1799>) target.method_5661());
                    if (protAmount > 0.0f) {
                        toDamage2 = class_1280.method_5497(toDamage2, protAmount);
                    }
                }
                return toDamage2;
            }
            return 0.0f;
        }
        return 0.0f;
    }

    public static class_3965 rayCastBlock(class_3959 context, class_2338 block) {
        return (class_3965) class_1922.method_17744(context.method_17750(), context.method_17747(), context, (raycastContext, blockPos) -> {
            class_2680 blockState;
            if (!blockPos.equals(block)) {
                blockState = class_2246.field_10124.method_9564();
            } else {
                blockState = class_2246.field_10540.method_9564();
            }
            class_243 vec3d = raycastContext.method_17750();
            class_243 vec3d2 = raycastContext.method_17747();
            class_265 voxelShape = raycastContext.method_17748(blockState, mc.field_1687, blockPos);
            class_3965 blockHitResult = mc.field_1687.method_17745(vec3d, vec3d2, blockPos, voxelShape, blockState);
            class_265 voxelShape2 = class_259.method_1073();
            class_3965 blockHitResult2 = voxelShape2.method_1092(vec3d, vec3d2, blockPos);
            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, raycastContext2 -> {
            class_243 vec3d = raycastContext2.method_17750().method_1020(raycastContext2.method_17747());
            return class_3965.method_17778(raycastContext2.method_17747(), class_2350.method_10142(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350), class_2338.method_49638(raycastContext2.method_17747()));
        });
    }

    private static float getExposureGhost(class_243 source, class_1297 entity, class_2338 pos) {
        class_238 box = entity.method_5829();
        double d = 1.0d / (((box.field_1320 - box.field_1323) * 2.0d) + 1.0d);
        double e = 1.0d / (((box.field_1325 - box.field_1322) * 2.0d) + 1.0d);
        double f = 1.0d / (((box.field_1324 - box.field_1321) * 2.0d) + 1.0d);
        double g = (1.0d - (Math.floor(1.0d / d) * d)) / 2.0d;
        double h = (1.0d - (Math.floor(1.0d / f) * f)) / 2.0d;
        if (d < 0.0d || e < 0.0d || f < 0.0d) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        double d2 = 0.0d;
        while (true) {
            double k = d2;
            if (k <= 1.0d) {
                double d3 = 0.0d;
                while (true) {
                    double l = d3;
                    if (l <= 1.0d) {
                        double d4 = 0.0d;
                        while (true) {
                            double m = d4;
                            if (m <= 1.0d) {
                                double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                                double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                                double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                                class_243 vec3d = new class_243(n + g, o, p + h);
                                if (raycastGhost(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                                    i++;
                                }
                                j++;
                                d4 = m + f;
                            }
                        }
                        d3 = l + e;
                    }
                }
                d2 = k + d;
            } else {
                return i / j;
            }
        }
    }

    private static float getExposureRemoved(class_243 source, class_1297 entity, class_2338 pos) {
        class_238 box = entity.method_5829();
        double d = 1.0d / (((box.field_1320 - box.field_1323) * 2.0d) + 1.0d);
        double e = 1.0d / (((box.field_1325 - box.field_1322) * 2.0d) + 1.0d);
        double f = 1.0d / (((box.field_1324 - box.field_1321) * 2.0d) + 1.0d);
        double g = (1.0d - (Math.floor(1.0d / d) * d)) / 2.0d;
        double h = (1.0d - (Math.floor(1.0d / f) * f)) / 2.0d;
        if (d < 0.0d || e < 0.0d || f < 0.0d) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        double d2 = 0.0d;
        while (true) {
            double k = d2;
            if (k <= 1.0d) {
                double d3 = 0.0d;
                while (true) {
                    double l = d3;
                    if (l <= 1.0d) {
                        double d4 = 0.0d;
                        while (true) {
                            double m = d4;
                            if (m <= 1.0d) {
                                double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                                double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                                double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                                class_243 vec3d = new class_243(n + g, o, p + h);
                                if (raycastRemoved(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                                    i++;
                                }
                                j++;
                                d4 = m + f;
                            }
                        }
                        d3 = l + e;
                    }
                }
                d2 = k + d;
            } else {
                return i / j;
            }
        }
    }

    public static float getExposure(class_243 source, class_238 box, boolean optimized) {
        if (!optimized) {
            return getExposure(source, box);
        }
        int miss = 0;
        int hit = 0;
        for (int k = 0; k <= 1; k++) {
            for (int l = 0; l <= 1; l++) {
                for (int m = 0; m <= 1; m++) {
                    double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                    double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                    double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                    class_243 vec3d = new class_243(n, o, p);
                    if (raycast(vec3d, source, resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                        miss++;
                    }
                    hit++;
                }
            }
        }
        return miss / hit;
    }

    public static float getExposure(class_243 source, class_238 box) {
        int i = 0;
        int j = 0;
        double d = 0.0d;
        while (true) {
            double k = d;
            if (k <= 1.0d) {
                double d2 = 0.0d;
                while (true) {
                    double l = d2;
                    if (l <= 1.0d) {
                        double d3 = 0.0d;
                        while (true) {
                            double m = d3;
                            if (m <= 1.0d) {
                                double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                                double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                                double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                                class_243 vec3d = new class_243(n + 0.045454555306552624d, o, p + 0.045454555306552624d);
                                if (raycast(vec3d, source, resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                                    i++;
                                }
                                j++;
                                d3 = m + 0.4545454446934474d;
                            }
                        }
                        d2 = l + 0.21739130885479366d;
                    }
                }
                d = k + 0.4545454446934474d;
            } else {
                return i / j;
            }
        }
    }

    private static class_3965 raycastGhost(class_3959 context, class_2338 bPos) {
        return (class_3965) class_1922.method_17744(context.method_17750(), context.method_17747(), context, (innerContext, pos) -> {
            class_2680 blockState;
            class_243 vec3d = innerContext.method_17750();
            class_243 vec3d2 = innerContext.method_17747();
            if (!pos.equals(bPos)) {
                blockState = mc.field_1687.method_8320(bPos);
            } else {
                blockState = class_2246.field_10540.method_9564();
            }
            class_265 voxelShape = innerContext.method_17748(blockState, mc.field_1687, pos);
            class_3965 blockHitResult = mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, innerContext2 -> {
            class_243 vec3d = innerContext2.method_17750().method_1020(innerContext2.method_17747());
            return class_3965.method_17778(innerContext2.method_17747(), class_2350.method_10142(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350), class_2338.method_49638(innerContext2.method_17747()));
        });
    }

    private static class_3965 raycastRemoved(class_3959 context, class_2338 removedPos) {
        return (class_3965) class_1922.method_17744(context.method_17750(), context.method_17747(), context, (innerContext, pos) -> {
            class_2680 class_2680VarMethod_8320;
            class_243 vec3d = innerContext.method_17750();
            class_243 vec3d2 = innerContext.method_17747();
            if (pos.equals(removedPos)) {
                class_2680VarMethod_8320 = class_2246.field_10124.method_9564();
            } else {
                class_2680VarMethod_8320 = mc.field_1687.method_8320(pos);
            }
            class_2680 blockState = class_2680VarMethod_8320;
            class_265 voxelShape = innerContext.method_17748(blockState, mc.field_1687, pos);
            class_3965 blockHitResult = mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            double d = blockHitResult == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return d <= e ? blockHitResult : blockHitResult2;
        }, innerContext2 -> {
            class_243 vec3d = innerContext2.method_17750().method_1020(innerContext2.method_17747());
            return class_3965.method_17778(innerContext2.method_17747(), class_2350.method_10142(vec3d.field_1352, vec3d.field_1351, vec3d.field_1350), class_2338.method_49638(innerContext2.method_17747()));
        });
    }

    public static class_239.class_240 raycast(class_243 start, class_243 end, boolean ignoreTerrain) {
        return (class_239.class_240) class_1922.method_17744(start, end, (Object) null, (innerContext, blockPos) -> {
            class_3965 hitResult;
            class_2680 blockState = mc.field_1687.method_8320(blockPos);
            if ((blockState.method_26204().method_9520() >= 600.0f || !ignoreTerrain) && (hitResult = blockState.method_26220(mc.field_1687, blockPos).method_1092(start, end, blockPos)) != null) {
                return hitResult.method_17783();
            }
            return null;
        }, innerContext2 -> {
            return class_239.class_240.field_1333;
        });
    }

    public static int getProtectionAmount(Iterable<class_1799> equipment) {
        MutableInt mutableInt = new MutableInt();
        equipment.forEach(i -> {
            mutableInt.add(getProtectionAmount(i));
        });
        return mutableInt.intValue();
    }

    public static int getProtectionAmount(class_1799 stack) {
        int modifierBlast = 0;
        int modifier = 0;
        class_9304 enchantments = class_1890.method_57532(stack);
        for (Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
            class_6880<class_1887> enchantment = (class_6880) entry.getKey();
            String id = enchantment.method_55840();
            if ("minecraft:blast_protection".equals(id)) {
                modifierBlast = entry.getIntValue();
            } else if ("minecraft:protection".equals(id)) {
                modifier = entry.getIntValue();
            }
        }
        return (modifierBlast * 2) + modifier;
    }

    private static MutableExplosion getMutableExplosion(class_243 explosionPos) {
        MutableExplosion mutable;
        class_1927 class_1927Var = explosion;
        if (class_1927Var instanceof MutableExplosion) {
            MutableExplosion existing = (MutableExplosion) class_1927Var;
            mutable = existing;
        } else {
            mutable = new MutableExplosion();
            explosion = mutable;
        }
        mutable.setPosition(explosionPos);
        mutable.setEntity(mc.field_1724);
        mutable.setPower(6.0f);
        mutable.setWorld(mc.field_1687);
        return mutable;
    }

    private static boolean resolveIgnoreTerrain() {
        try {
            return CrystalAuraModule.getInstance().processor.ignoreTerrain.getValue().booleanValue();
        } catch (Throwable th) {
            return terrainIgnore;
        }
    }

    private static boolean resolveAssumeBestArmor() {
        return assumeBestArmor;
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/world/ExplosionUtility$MutableExplosion.class */
    private static final class MutableExplosion implements class_1927 {
        private class_3218 world;
        private class_1297 entity;
        private float power = 6.0f;
        private class_243 position = class_243.field_1353;

        private MutableExplosion() {
        }

        void setWorld(@Nullable class_1937 world) {
            class_3218 class_3218Var;
            if (world instanceof class_3218) {
                class_3218 serverWorld = (class_3218) world;
                class_3218Var = serverWorld;
            } else {
                class_3218Var = null;
            }
            this.world = class_3218Var;
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
            class_1309 class_1309Var = this.entity;
            if (!(class_1309Var instanceof class_1309)) {
                return null;
            }
            class_1309 living = class_1309Var;
            return living;
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
