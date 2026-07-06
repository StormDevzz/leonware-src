/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  net.minecraft.class_1297
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_9334
 */
package sweetie.leonware.api.utils.player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_9334;
import sweetie.leonware.api.system.interfaces.QuickImports;

public final class PlayerUtil
implements QuickImports {
    private static final Pattern namePattern = Pattern.compile("^\\w{3,16}$");

    public static boolean isEating() {
        return PlayerUtil.mc.field_1724.method_6115() && PlayerUtil.mc.field_1724.method_6030().method_57353().method_57832(class_9334.field_50075);
    }

    public static boolean canSee(class_243 to) {
        class_3965 hitResult = PlayerUtil.mc.field_1687.method_17742(new class_3959(mc.method_1560().method_33571(), to, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, mc.method_1560()));
        return hitResult == null || hitResult.method_17783() == class_239.class_240.field_1333;
    }

    public static boolean isAboveWater() {
        if (PlayerUtil.mc.field_1724 == null) {
            return false;
        }
        if (PlayerUtil.mc.field_1687 == null) {
            return false;
        }
        return PlayerUtil.mc.field_1724.method_5869() || PlayerUtil.mc.field_1687.method_8320(PlayerUtil.mc.field_1724.method_24515().method_10069(0, 0, 0)).method_26204() == class_2246.field_10382;
    }

    public static boolean isInWeb() {
        if (PlayerUtil.mc.field_1724 == null) {
            return false;
        }
        class_238 playerBox = PlayerUtil.mc.field_1724.method_5829();
        class_2338 playerPosition = PlayerUtil.mc.field_1724.method_24515();
        return PlayerUtil.getNearbyBlockPositions(playerPosition).stream().anyMatch(pos -> PlayerUtil.isBlockCobweb(playerBox, pos));
    }

    private static boolean isBlockCobweb(class_238 playerBox, class_2338 blockPos) {
        return playerBox.method_994(new class_238(blockPos)) && PlayerUtil.mc.field_1687 != null && PlayerUtil.mc.field_1687.method_8320(blockPos).method_26204() == class_2246.field_10343;
    }

    public static List<class_2338> getNearbyBlockPositions(class_2338 center) {
        ArrayList<class_2338> positions = new ArrayList<class_2338>();
        for (int x = center.method_10263() - 2; x <= center.method_10263() + 2; ++x) {
            for (int y = center.method_10264() - 1; y <= center.method_10264() + 4; ++y) {
                for (int z = center.method_10260() - 2; z <= center.method_10260() + 2; ++z) {
                    positions.add(new class_2338(x, y, z));
                }
            }
        }
        return positions;
    }

    public static class_2248 getBlock(float x, float y, float z) {
        class_243 pos = PlayerUtil.mc.field_1724.method_19538();
        return PlayerUtil.mc.field_1687.method_8320(new class_2338(new class_2382((int)(pos.field_1352 + (double)x), (int)(pos.field_1351 + (double)y), (int)(pos.field_1350 + (double)z)))).method_26204();
    }

    public static boolean hasCollisionWith(class_1297 entity) {
        return PlayerUtil.hasCollisionWith(entity, 0.0f);
    }

    public static boolean hasCollisionWith(class_1297 entity, float expand) {
        class_238 box = PlayerUtil.mc.field_1724.method_5829();
        class_238 targetbox = entity.method_5829().method_1009((double)expand, 0.0, (double)expand);
        return box.field_1320 > targetbox.field_1323 && box.field_1325 > targetbox.field_1322 && box.field_1324 > targetbox.field_1321 && box.field_1323 < targetbox.field_1320 && box.field_1322 < targetbox.field_1325 && box.field_1321 < targetbox.field_1324;
    }

    public static boolean isValidName(String name) {
        return namePattern.matcher(name).matches();
    }

    @Generated
    private PlayerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

