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

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/player/PlayerUtil.class */
public final class PlayerUtil implements QuickImports {
    private static final Pattern namePattern = Pattern.compile("^\\w{3,16}$");

    @Generated
    private PlayerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isEating() {
        return mc.field_1724.method_6115() && mc.field_1724.method_6030().method_57353().method_57832(class_9334.field_50075);
    }

    public static boolean canSee(class_243 to) {
        class_3965 class_3965VarMethod_17742 = mc.field_1687.method_17742(new class_3959(mc.method_1560().method_33571(), to, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, mc.method_1560()));
        return class_3965VarMethod_17742 == null || class_3965VarMethod_17742.method_17783() == class_239.class_240.field_1333;
    }

    public static boolean isAboveWater() {
        if (mc.field_1724 == null || mc.field_1687 == null) {
            return false;
        }
        return mc.field_1724.method_5869() || mc.field_1687.method_8320(mc.field_1724.method_24515().method_10069(0, 0, 0)).method_26204() == class_2246.field_10382;
    }

    public static boolean isInWeb() {
        if (mc.field_1724 == null) {
            return false;
        }
        class_238 playerBox = mc.field_1724.method_5829();
        class_2338 playerPosition = mc.field_1724.method_24515();
        return getNearbyBlockPositions(playerPosition).stream().anyMatch(pos -> {
            return isBlockCobweb(playerBox, pos);
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBlockCobweb(class_238 playerBox, class_2338 blockPos) {
        return playerBox.method_994(new class_238(blockPos)) && mc.field_1687 != null && mc.field_1687.method_8320(blockPos).method_26204() == class_2246.field_10343;
    }

    public static List<class_2338> getNearbyBlockPositions(class_2338 center) {
        List<class_2338> positions = new ArrayList<>();
        for (int x = center.method_10263() - 2; x <= center.method_10263() + 2; x++) {
            for (int y = center.method_10264() - 1; y <= center.method_10264() + 4; y++) {
                for (int z = center.method_10260() - 2; z <= center.method_10260() + 2; z++) {
                    positions.add(new class_2338(x, y, z));
                }
            }
        }
        return positions;
    }

    public static class_2248 getBlock(float x, float y, float z) {
        class_243 pos = mc.field_1724.method_19538();
        return mc.field_1687.method_8320(new class_2338(new class_2382((int) (pos.field_1352 + ((double) x)), (int) (pos.field_1351 + ((double) y)), (int) (pos.field_1350 + ((double) z))))).method_26204();
    }

    public static boolean hasCollisionWith(class_1297 entity) {
        return hasCollisionWith(entity, 0.0f);
    }

    public static boolean hasCollisionWith(class_1297 entity, float expand) {
        class_238 box = mc.field_1724.method_5829();
        class_238 targetbox = entity.method_5829().method_1009(expand, 0.0d, expand);
        return box.field_1320 > targetbox.field_1323 && box.field_1325 > targetbox.field_1322 && box.field_1324 > targetbox.field_1321 && box.field_1323 < targetbox.field_1320 && box.field_1322 < targetbox.field_1325 && box.field_1321 < targetbox.field_1324;
    }

    public static boolean isValidName(String name) {
        return namePattern.matcher(name).matches();
    }
}
