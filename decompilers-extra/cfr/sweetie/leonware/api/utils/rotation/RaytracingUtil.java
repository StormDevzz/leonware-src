/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1675
 *  net.minecraft.class_1922
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 */
package sweetie.leonware.api.utils.rotation;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1675;
import net.minecraft.class_1922;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import sweetie.leonware.api.system.interfaces.QuickImports;
import sweetie.leonware.api.utils.rotation.manager.Rotation;

public final class RaytracingUtil
implements QuickImports {
    private static boolean shouldBlockHitCancel(class_243 start, class_243 entityPos, class_3965 blockHit) {
        if (blockHit == null || blockHit.method_17783() != class_239.class_240.field_1332) {
            return false;
        }
        if (RaytracingUtil.mc.field_1687 == null) {
            return false;
        }
        class_2680 blockState = RaytracingUtil.mc.field_1687.method_8320(blockHit.method_17777());
        if (blockState == null) {
            return false;
        }
        if (!blockState.method_26220((class_1922)RaytracingUtil.mc.field_1687, blockHit.method_17777()).method_1110()) {
            class_243 blockPos = blockHit.method_17784();
            return blockPos.method_1025(start) < entityPos.method_1025(start);
        }
        return false;
    }

    public static class_3965 raycast(Rotation rotation, double range, boolean includeFluids, float tickDelta) {
        return RaytracingUtil.raycast(range, includeFluids, RaytracingUtil.mc.field_1724.method_5836(tickDelta), rotation.getVector(), RaytracingUtil.mc.field_1719);
    }

    public static class_3965 raycast(double range, boolean includeFluids, class_243 start, class_243 direction, class_1297 entity) {
        class_243 end = start.method_1031(direction.field_1352 * range, direction.field_1351 * range, direction.field_1350 * range);
        return RaytracingUtil.mc.field_1687.method_17742(new class_3959(start, end, class_3959.class_3960.field_17559, includeFluids ? class_3959.class_242.field_1347 : class_3959.class_242.field_1348, entity));
    }

    public static class_3966 raytraceEntity(double range, Rotation rotation, boolean ignoreWalls) {
        return RaytracingUtil.raytraceEntity(range, rotation, ignoreWalls, entity -> true, 0.0f);
    }

    public static class_3966 raytraceEntity(double range, Rotation rotation, boolean ignoreWalls, float expand) {
        return RaytracingUtil.raytraceEntity(range, rotation, ignoreWalls, entity -> true, expand);
    }

    public static class_3966 raytraceEntityBacktrack(double range, Rotation rotation, boolean ignoreWalls, class_243 backtrackPos, class_1309 target) {
        class_1297 entity = mc.method_1560();
        if (entity == null || target == null) {
            return null;
        }
        class_243 cameraVec = entity.method_33571();
        class_243 rotationVec = rotation.getVector();
        class_243 rayEnd = cameraVec.method_1031(rotationVec.field_1352 * range, rotationVec.field_1351 * range, rotationVec.field_1350 * range);
        class_238 backtrackBox = new class_238(backtrackPos.field_1352 - (double)(target.method_17681() / 2.0f), backtrackPos.field_1351, backtrackPos.field_1350 - (double)(target.method_17681() / 2.0f), backtrackPos.field_1352 + (double)(target.method_17681() / 2.0f), backtrackPos.field_1351 + (double)target.method_17682(), backtrackPos.field_1350 + (double)(target.method_17681() / 2.0f));
        Optional hit = backtrackBox.method_992(cameraVec, rayEnd);
        if (hit.isEmpty()) {
            return null;
        }
        if (ignoreWalls) {
            return new class_3966((class_1297)target, (class_243)hit.get());
        }
        class_3965 blockHit = RaytracingUtil.mc.field_1687 != null ? RaytracingUtil.mc.field_1687.method_17742(new class_3959(cameraVec, (class_243)hit.get(), class_3959.class_3960.field_17558, class_3959.class_242.field_1347, (class_1297)RaytracingUtil.mc.field_1724)) : null;
        return RaytracingUtil.shouldBlockHitCancel(cameraVec, (class_243)hit.get(), blockHit) ? null : new class_3966((class_1297)target, (class_243)hit.get());
    }

    public static class_3966 raytraceEntity(double range, Rotation rotation, boolean ignoreWalls, Predicate<class_1297> filter, float expand) {
        class_238 box;
        class_1297 entity = mc.method_1560();
        if (entity == null) {
            return null;
        }
        class_243 cameraVec = entity.method_33571();
        class_243 rotationVec = rotation.getVector();
        class_243 vec3d3 = cameraVec.method_1031(rotationVec.field_1352 * range, rotationVec.field_1351 * range, rotationVec.field_1350 * range);
        class_3966 entityHit = class_1675.method_18075((class_1297)entity, (class_243)cameraVec, (class_243)vec3d3, (class_238)(box = entity.method_5829().method_18804(rotationVec.method_1021(range)).method_1009(1.0, 1.0, 1.0)), e -> e.method_5863() && e.method_5805() && !e.method_7325() && filter.test((class_1297)e), (double)(range * range));
        if (entityHit == null) {
            return null;
        }
        if (ignoreWalls) {
            return entityHit;
        }
        class_243 entityPos = entityHit.method_17782().method_5829().method_992(cameraVec, vec3d3).orElse(entityHit.method_17782().method_19538());
        class_3965 blockHit = RaytracingUtil.mc.field_1687 != null ? RaytracingUtil.mc.field_1687.method_17742(new class_3959(cameraVec, entityPos, class_3959.class_3960.field_17558, class_3959.class_242.field_1347, (class_1297)RaytracingUtil.mc.field_1724)) : null;
        return RaytracingUtil.shouldBlockHitCancel(cameraVec, entityPos, blockHit) ? null : entityHit;
    }

    @Generated
    private RaytracingUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

