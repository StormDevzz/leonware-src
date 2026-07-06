// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.world;

import org.jetbrains.annotations.Nullable;
import net.minecraft.class_3218;
import sweetie.leonware.client.features.modules.combat.crystalaura.CrystalAuraModule;
import net.minecraft.class_1887;
import java.util.Iterator;
import net.minecraft.class_9304;
import net.minecraft.class_6880;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.class_1890;
import org.apache.commons.lang3.mutable.MutableInt;
import net.minecraft.class_239;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2374;
import net.minecraft.class_2350;
import net.minecraft.class_259;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_3965;
import net.minecraft.class_2338;
import net.minecraft.class_3959;
import net.minecraft.class_1282;
import net.minecraft.class_1799;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1280;
import java.util.Objects;
import net.minecraft.class_5134;
import net.minecraft.class_1324;
import net.minecraft.class_238;
import net.minecraft.class_3532;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_1267;
import sweetie.leonware.api.utils.math.PredictUtility;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_1927;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class ExplosionUtility implements QuickImports
{
    public static boolean terrainIgnore;
    public static boolean assumeBestArmor;
    public static class_1927 explosion;
    
    private ExplosionUtility() {
    }
    
    public static float getAutoCrystalDamage(final class_243 crystalPos, final class_1657 target, final int predictTicks, final boolean optimized) {
        if (predictTicks == 0) {
            return getExplosionDamage(crystalPos, target, optimized);
        }
        return getExplosionDamageWPredict(crystalPos, target, PredictUtility.predictBox(target, predictTicks), optimized);
    }
    
    public static float getSelfExplosionDamage(final class_243 explosionPos, final int predictTicks, final boolean optimized) {
        return getAutoCrystalDamage(explosionPos, (class_1657)ExplosionUtility.mc.field_1724, predictTicks, optimized);
    }
    
    public static float getExplosionDamage(final class_243 explosionPos, final class_1657 target, final boolean optimized) {
        if (ExplosionUtility.mc.field_1687 == null || ExplosionUtility.mc.field_1724 == null || ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5801 || target == null) {
            return 0.0f;
        }
        final MutableExplosion mutableExplosion = getMutableExplosion(explosionPos);
        final class_1282 damageSource = class_1927.method_55108((class_1937)ExplosionUtility.mc.field_1687, (class_1297)ExplosionUtility.mc.field_1724);
        if (!new class_238((double)class_3532.method_15357(explosionPos.field_1352 - 11.0), (double)class_3532.method_15357(explosionPos.field_1351 - 11.0), (double)class_3532.method_15357(explosionPos.field_1350 - 11.0), (double)class_3532.method_15357(explosionPos.field_1352 + 13.0), (double)class_3532.method_15357(explosionPos.field_1351 + 13.0), (double)class_3532.method_15357(explosionPos.field_1350 + 13.0)).method_994(target.method_5829())) {
            return 0.0f;
        }
        if (!target.method_5659((class_1927)mutableExplosion) && !target.method_5655()) {
            final double distExposure = target.method_5707(explosionPos) / 144.0;
            if (distExposure <= 1.0) {
                ExplosionUtility.terrainIgnore = resolveIgnoreTerrain();
                final double exposure = getExposure(explosionPos, target.method_5829(), optimized);
                ExplosionUtility.terrainIgnore = false;
                final double finalExposure = (1.0 - distExposure) * exposure;
                float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
                if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5805) {
                    toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
                }
                else if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5807) {
                    toDamage = toDamage * 3.0f / 2.0f;
                }
                toDamage = class_1280.method_5496((class_1309)target, toDamage, damageSource, (float)target.method_6096(), (float)Objects.requireNonNull(target.method_5996(class_5134.field_23725)).method_6194());
                if (target.method_6059(class_1294.field_5907)) {
                    final int resistance = 25 - (Objects.requireNonNull(target.method_6112(class_1294.field_5907)).method_5578() + 1) * 5;
                    final float resistanceScaled = toDamage * resistance;
                    toDamage = Math.max(resistanceScaled / 25.0f, 0.0f);
                }
                if (toDamage <= 0.0f) {
                    toDamage = 0.0f;
                }
                else {
                    final float protAmount = resolveAssumeBestArmor() ? 32.0f : ((float)getProtectionAmount(target.method_5661()));
                    if (protAmount > 0.0f) {
                        toDamage = class_1280.method_5497(toDamage, protAmount);
                    }
                }
                return toDamage;
            }
        }
        return 0.0f;
    }
    
    public static float getExplosionDamageWPredict(final class_243 explosionPos, final class_1657 target, final class_238 predict, final boolean optimized) {
        if (ExplosionUtility.mc.field_1687 == null || ExplosionUtility.mc.field_1724 == null || ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5801) {
            return 0.0f;
        }
        if (target == null || predict == null) {
            return 0.0f;
        }
        final MutableExplosion mutableExplosion = getMutableExplosion(explosionPos);
        final class_1282 damageSource = class_1927.method_55108((class_1937)ExplosionUtility.mc.field_1687, (class_1297)ExplosionUtility.mc.field_1724);
        if (!new class_238((double)class_3532.method_15357(explosionPos.field_1352 - 11.0), (double)class_3532.method_15357(explosionPos.field_1351 - 11.0), (double)class_3532.method_15357(explosionPos.field_1350 - 11.0), (double)class_3532.method_15357(explosionPos.field_1352 + 13.0), (double)class_3532.method_15357(explosionPos.field_1351 + 13.0), (double)class_3532.method_15357(explosionPos.field_1350 + 13.0)).method_994(predict)) {
            return 0.0f;
        }
        if (!target.method_5659((class_1927)mutableExplosion) && !target.method_5655()) {
            final double distExposure = predict.method_1005().method_1031(0.0, -0.9, 0.0).method_1025(explosionPos) / 144.0;
            if (distExposure <= 1.0) {
                ExplosionUtility.terrainIgnore = resolveIgnoreTerrain();
                final double exposure = getExposure(explosionPos, predict, optimized);
                ExplosionUtility.terrainIgnore = false;
                final double finalExposure = (1.0 - distExposure) * exposure;
                float toDamage = (float)Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * 12.0 + 1.0);
                if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5805) {
                    toDamage = Math.min(toDamage / 2.0f + 1.0f, toDamage);
                }
                else if (ExplosionUtility.mc.field_1687.method_8407() == class_1267.field_5807) {
                    toDamage = toDamage * 3.0f / 2.0f;
                }
                toDamage = class_1280.method_5496((class_1309)target, toDamage, damageSource, (float)target.method_6096(), (float)Objects.requireNonNull(target.method_5996(class_5134.field_23725)).method_6194());
                if (target.method_6059(class_1294.field_5907)) {
                    final int resistance = 25 - (Objects.requireNonNull(target.method_6112(class_1294.field_5907)).method_5578() + 1) * 5;
                    final float resistanceScaled = toDamage * resistance;
                    toDamage = Math.max(resistanceScaled / 25.0f, 0.0f);
                }
                if (toDamage <= 0.0f) {
                    toDamage = 0.0f;
                }
                else {
                    final float protAmount = resolveAssumeBestArmor() ? 32.0f : ((float)getProtectionAmount(target.method_5661()));
                    if (protAmount > 0.0f) {
                        toDamage = class_1280.method_5497(toDamage, protAmount);
                    }
                }
                return toDamage;
            }
        }
        return 0.0f;
    }
    
    public static class_3965 rayCastBlock(final class_3959 context, final class_2338 block) {
        return (class_3965)class_1922.method_17744(context.method_17750(), context.method_17747(), (Object)context, (raycastContext, blockPos) -> {
            class_2680 blockState;
            if (!blockPos.equals((Object)block)) {
                blockState = class_2246.field_10124.method_9564();
            }
            else {
                blockState = class_2246.field_10540.method_9564();
            }
            final class_243 vec3d = raycastContext.method_17750();
            final class_243 vec3d2 = raycastContext.method_17747();
            final class_265 voxelShape = raycastContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, blockPos);
            final class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, blockPos, voxelShape, blockState);
            final class_265 voxelShape2 = class_259.method_1073();
            final class_3965 blockHitResult2 = voxelShape2.method_1092(vec3d, vec3d2, blockPos);
            final double d = (blockHitResult == null) ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult.method_17784());
            final double e = (blockHitResult2 == null) ? Double.MAX_VALUE : raycastContext.method_17750().method_1025(blockHitResult2.method_17784());
            return (d <= e) ? blockHitResult : blockHitResult2;
        }, raycastContext -> {
            final class_243 vec3d3 = raycastContext.method_17750().method_1020(raycastContext.method_17747());
            return class_3965.method_17778(raycastContext.method_17747(), class_2350.method_10142(vec3d3.field_1352, vec3d3.field_1351, vec3d3.field_1350), class_2338.method_49638((class_2374)raycastContext.method_17747()));
        });
    }
    
    private static float getExposureGhost(final class_243 source, final class_1297 entity, final class_2338 pos) {
        final class_238 box = entity.method_5829();
        final double d = 1.0 / ((box.field_1320 - box.field_1323) * 2.0 + 1.0);
        final double e = 1.0 / ((box.field_1325 - box.field_1322) * 2.0 + 1.0);
        final double f = 1.0 / ((box.field_1324 - box.field_1321) * 2.0 + 1.0);
        final double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        final double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    final double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                    final double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                    final double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                    final class_243 vec3d = new class_243(n + g, o, p + h);
                    if (raycastGhost(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return i / (float)j;
    }
    
    private static float getExposureRemoved(final class_243 source, final class_1297 entity, final class_2338 pos) {
        final class_238 box = entity.method_5829();
        final double d = 1.0 / ((box.field_1320 - box.field_1323) * 2.0 + 1.0);
        final double e = 1.0 / ((box.field_1325 - box.field_1322) * 2.0 + 1.0);
        final double f = 1.0 / ((box.field_1324 - box.field_1321) * 2.0 + 1.0);
        final double g = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        final double h = (1.0 - Math.floor(1.0 / f) * f) / 2.0;
        if (d < 0.0 || e < 0.0 || f < 0.0) {
            return 0.0f;
        }
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    final double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                    final double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                    final double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                    final class_243 vec3d = new class_243(n + g, o, p + h);
                    if (raycastRemoved(new class_3959(vec3d, source, class_3959.class_3960.field_17558, class_3959.class_242.field_1348, entity), pos).method_17783() == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return i / (float)j;
    }
    
    public static float getExposure(final class_243 source, final class_238 box, final boolean optimized) {
        if (!optimized) {
            return getExposure(source, box);
        }
        int miss = 0;
        int hit = 0;
        for (int k = 0; k <= 1; ++k) {
            for (int l = 0; l <= 1; ++l) {
                for (int m = 0; m <= 1; ++m) {
                    final double n = class_3532.method_16436((double)k, box.field_1323, box.field_1320);
                    final double o = class_3532.method_16436((double)l, box.field_1322, box.field_1325);
                    final double p = class_3532.method_16436((double)m, box.field_1321, box.field_1324);
                    final class_243 vec3d = new class_243(n, o, p);
                    if (raycast(vec3d, source, resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                        ++miss;
                    }
                    ++hit;
                }
            }
        }
        return miss / (float)hit;
    }
    
    public static float getExposure(final class_243 source, final class_238 box) {
        final double d = 0.4545454446934474;
        final double e = 0.21739130885479366;
        final double f = 0.4545454446934474;
        int i = 0;
        int j = 0;
        for (double k = 0.0; k <= 1.0; k += d) {
            for (double l = 0.0; l <= 1.0; l += e) {
                for (double m = 0.0; m <= 1.0; m += f) {
                    final double n = class_3532.method_16436(k, box.field_1323, box.field_1320);
                    final double o = class_3532.method_16436(l, box.field_1322, box.field_1325);
                    final double p = class_3532.method_16436(m, box.field_1321, box.field_1324);
                    final class_243 vec3d = new class_243(n + 0.045454555306552624, o, p + 0.045454555306552624);
                    if (raycast(vec3d, source, resolveIgnoreTerrain()) == class_239.class_240.field_1333) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return i / (float)j;
    }
    
    private static class_3965 raycastGhost(final class_3959 context, final class_2338 bPos) {
        return (class_3965)class_1922.method_17744(context.method_17750(), context.method_17747(), (Object)context, (innerContext, pos) -> {
            final class_243 vec3d = innerContext.method_17750();
            final class_243 vec3d2 = innerContext.method_17747();
            class_2680 blockState;
            if (!pos.equals((Object)bPos)) {
                blockState = ExplosionUtility.mc.field_1687.method_8320(bPos);
            }
            else {
                blockState = class_2246.field_10540.method_9564();
            }
            final class_265 voxelShape = innerContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, pos);
            final class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            final class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            final double d = (blockHitResult == null) ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            final double e = (blockHitResult2 == null) ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return (d <= e) ? blockHitResult : blockHitResult2;
        }, innerContext -> {
            final class_243 vec3d3 = innerContext.method_17750().method_1020(innerContext.method_17747());
            return class_3965.method_17778(innerContext.method_17747(), class_2350.method_10142(vec3d3.field_1352, vec3d3.field_1351, vec3d3.field_1350), class_2338.method_49638((class_2374)innerContext.method_17747()));
        });
    }
    
    private static class_3965 raycastRemoved(final class_3959 context, final class_2338 removedPos) {
        return (class_3965)class_1922.method_17744(context.method_17750(), context.method_17747(), (Object)context, (innerContext, pos) -> {
            final class_243 vec3d = innerContext.method_17750();
            final class_243 vec3d2 = innerContext.method_17747();
            final class_2680 blockState = pos.equals((Object)removedPos) ? class_2246.field_10124.method_9564() : ExplosionUtility.mc.field_1687.method_8320(pos);
            final class_265 voxelShape = innerContext.method_17748(blockState, (class_1922)ExplosionUtility.mc.field_1687, pos);
            final class_3965 blockHitResult = ExplosionUtility.mc.field_1687.method_17745(vec3d, vec3d2, pos, voxelShape, blockState);
            final class_3965 blockHitResult2 = class_259.method_1073().method_1092(vec3d, vec3d2, pos);
            final double d = (blockHitResult == null) ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult.method_17784());
            final double e = (blockHitResult2 == null) ? Double.MAX_VALUE : innerContext.method_17750().method_1025(blockHitResult2.method_17784());
            return (d <= e) ? blockHitResult : blockHitResult2;
        }, innerContext -> {
            final class_243 vec3d3 = innerContext.method_17750().method_1020(innerContext.method_17747());
            return class_3965.method_17778(innerContext.method_17747(), class_2350.method_10142(vec3d3.field_1352, vec3d3.field_1351, vec3d3.field_1350), class_2338.method_49638((class_2374)innerContext.method_17747()));
        });
    }
    
    public static class_239.class_240 raycast(final class_243 start, final class_243 end, final boolean ignoreTerrain) {
        return (class_239.class_240)class_1922.method_17744(start, end, (Object)null, (innerContext, blockPos) -> {
            final class_2680 blockState = ExplosionUtility.mc.field_1687.method_8320(blockPos);
            if (blockState.method_26204().method_9520() < 600.0f && ignoreTerrain) {
                return null;
            }
            else {
                final class_3965 hitResult = blockState.method_26220((class_1922)ExplosionUtility.mc.field_1687, blockPos).method_1092(start, end, blockPos);
                return (hitResult == null) ? null : hitResult.method_17783();
            }
        }, innerContext -> class_239.class_240.field_1333);
    }
    
    public static int getProtectionAmount(final Iterable<class_1799> equipment) {
        final MutableInt mutableInt = new MutableInt();
        equipment.forEach(i -> mutableInt.add(getProtectionAmount(i)));
        return mutableInt.intValue();
    }
    
    public static int getProtectionAmount(final class_1799 stack) {
        int modifierBlast = 0;
        int modifier = 0;
        final class_9304 enchantments = class_1890.method_57532(stack);
        for (final Object2IntMap.Entry<class_6880<class_1887>> entry : enchantments.method_57539()) {
            final class_6880<class_1887> enchantment = (class_6880<class_1887>)entry.getKey();
            final String id = enchantment.method_55840();
            if ("minecraft:blast_protection".equals(id)) {
                modifierBlast = entry.getIntValue();
            }
            else {
                if (!"minecraft:protection".equals(id)) {
                    continue;
                }
                modifier = entry.getIntValue();
            }
        }
        return modifierBlast * 2 + modifier;
    }
    
    private static MutableExplosion getMutableExplosion(final class_243 explosionPos) {
        final class_1927 explosion = ExplosionUtility.explosion;
        MutableExplosion mutable;
        if (explosion instanceof final MutableExplosion mutableExplosion) {
            final MutableExplosion existing = mutable = mutableExplosion;
        }
        else {
            mutable = (MutableExplosion)(ExplosionUtility.explosion = (class_1927)new MutableExplosion());
        }
        mutable.setPosition(explosionPos);
        mutable.setEntity((class_1297)ExplosionUtility.mc.field_1724);
        mutable.setPower(6.0f);
        mutable.setWorld((class_1937)ExplosionUtility.mc.field_1687);
        return mutable;
    }
    
    private static boolean resolveIgnoreTerrain() {
        try {
            return CrystalAuraModule.getInstance().processor.ignoreTerrain.getValue();
        }
        catch (final Throwable ignored) {
            return ExplosionUtility.terrainIgnore;
        }
    }
    
    private static boolean resolveAssumeBestArmor() {
        return ExplosionUtility.assumeBestArmor;
    }
    
    static {
        ExplosionUtility.terrainIgnore = false;
        ExplosionUtility.assumeBestArmor = false;
    }
    
    private static final class MutableExplosion implements class_1927
    {
        private class_3218 world;
        private class_1297 entity;
        private float power;
        private class_243 position;
        
        private MutableExplosion() {
            this.power = 6.0f;
            this.position = class_243.field_1353;
        }
        
        void setWorld(@Nullable final class_1937 world) {
            class_3218 world2;
            if (world instanceof final class_3218 class_3218) {
                final class_3218 serverWorld = world2 = class_3218;
            }
            else {
                world2 = null;
            }
            this.world = world2;
        }
        
        void setEntity(final class_1297 entity) {
            this.entity = entity;
        }
        
        void setPower(final float power) {
            this.power = power;
        }
        
        void setPosition(final class_243 position) {
            this.position = position;
        }
        
        public class_3218 method_64504() {
            return this.world;
        }
        
        public class_1927.class_4179 method_55111() {
            return class_1927.class_4179.field_18687;
        }
        
        public class_1309 method_8347() {
            final class_1297 entity = this.entity;
            class_1309 class_1309;
            if (entity instanceof final class_1309 class_1310) {
                final class_1309 living = class_1309 = class_1310;
            }
            else {
                class_1309 = null;
            }
            return class_1309;
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
