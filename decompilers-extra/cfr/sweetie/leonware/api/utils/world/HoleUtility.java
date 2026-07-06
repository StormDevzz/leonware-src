/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2374
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package sweetie.leonware.api.utils.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2374;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HoleUtility {
    private static final class_310 mc = class_310.method_1551();
    public static final class_2382[] VECTOR_PATTERN = new class_2382[]{new class_2382(0, 0, 1), new class_2382(0, 0, -1), new class_2382(1, 0, 0), new class_2382(-1, 0, 0)};

    @NotNull
    public static List<class_2338> getSurroundPoses(@NotNull class_243 from) {
        int z;
        int x;
        class_2338 fromPos = class_2338.method_49638((class_2374)from);
        ArrayList<class_2338> tempOffsets = new ArrayList<class_2338>();
        double decimalX = Math.abs(from.method_10216()) - Math.floor(Math.abs(from.method_10216()));
        double decimalZ = Math.abs(from.method_10215()) - Math.floor(Math.abs(from.method_10215()));
        int lengthXPos = HoleUtility.calcLength(decimalX, false);
        int lengthXNeg = HoleUtility.calcLength(decimalX, true);
        int lengthZPos = HoleUtility.calcLength(decimalZ, false);
        int lengthZNeg = HoleUtility.calcLength(decimalZ, true);
        for (x = 1; x < lengthXPos + 1; ++x) {
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, x, 0.0, 1 + lengthZPos));
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, x, 0.0, -(1 + lengthZNeg)));
        }
        for (x = 0; x <= lengthXNeg; ++x) {
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, -x, 0.0, 1 + lengthZPos));
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, -x, 0.0, -(1 + lengthZNeg)));
        }
        for (z = 1; z < lengthZPos + 1; ++z) {
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, 1 + lengthXPos, 0.0, z));
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, -(1 + lengthXNeg), 0.0, z));
        }
        for (z = 0; z <= lengthZNeg; ++z) {
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, 1 + lengthXPos, 0.0, -z));
            tempOffsets.add(HoleUtility.addToPlayer(fromPos, -(1 + lengthXNeg), 0.0, -z));
        }
        return tempOffsets;
    }

    @NotNull
    public static List<class_2338> getHolePoses(@NotNull class_243 from) {
        ArrayList<class_2338> positions = new ArrayList<class_2338>();
        double decimalX = from.method_10216() - Math.floor(from.method_10216());
        double decimalZ = from.method_10215() - Math.floor(from.method_10215());
        int offX = HoleUtility.calcOffset(decimalX);
        int offZ = HoleUtility.calcOffset(decimalZ);
        class_2338 base = HoleUtility.getPos(from);
        positions.add(base);
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                positions.add(base.method_10069(x * offX, 0, z * offZ));
            }
        }
        return positions;
    }

    @NotNull
    private static class_2338 getPos(@NotNull class_243 from) {
        return class_2338.method_49637((double)from.method_10216(), (double)(from.method_10214() - Math.floor(from.method_10214()) > 0.8 ? Math.floor(from.method_10214()) + 1.0 : Math.floor(from.method_10214())), (double)from.method_10215());
    }

    public static int calcOffset(double dec) {
        return dec >= 0.7 ? 1 : (dec <= 0.3 ? -1 : 0);
    }

    public static int calcLength(double decimal, boolean negative) {
        if (negative) {
            return decimal <= 0.3 ? 1 : 0;
        }
        return decimal >= 0.7 ? 1 : 0;
    }

    public static class_2338 addToPlayer(@NotNull class_2338 playerPos, double x, double y, double z) {
        if (playerPos.method_10263() < 0) {
            x = -x;
        }
        if (playerPos.method_10264() < 0) {
            y = -y;
        }
        if (playerPos.method_10260() < 0) {
            z = -z;
        }
        return playerPos.method_10081((class_2382)class_2338.method_49637((double)x, (double)y, (double)z));
    }

    public static boolean isHole(class_2338 pos) {
        return HoleUtility.isSingleHole(pos) || HoleUtility.validTwoBlockIndestructible(pos) || HoleUtility.validTwoBlockBedrock(pos) || HoleUtility.validQuadIndestructible(pos) || HoleUtility.validQuadBedrock(pos);
    }

    public static boolean isSingleHole(class_2338 pos) {
        return HoleUtility.validIndestructible(pos) || HoleUtility.validBedrock(pos);
    }

    public static boolean validIndestructible(@NotNull class_2338 pos) {
        return !(HoleUtility.validBedrock(pos) || !HoleUtility.isIndestructible(pos.method_10074()) || !HoleUtility.isIndestructible(pos.method_10078()) && !HoleUtility.isBedrock(pos.method_10078()) || !HoleUtility.isIndestructible(pos.method_10067()) && !HoleUtility.isBedrock(pos.method_10067()) || !HoleUtility.isIndestructible(pos.method_10095()) && !HoleUtility.isBedrock(pos.method_10095()) || !HoleUtility.isIndestructible(pos.method_10072()) && !HoleUtility.isBedrock(pos.method_10072()) || !HoleUtility.isReplaceable(pos) || !HoleUtility.isReplaceable(pos.method_10084()) || !HoleUtility.isReplaceable(pos.method_10086(2)));
    }

    public static boolean validBedrock(@NotNull class_2338 pos) {
        return HoleUtility.isBedrock(pos.method_10074()) && HoleUtility.isBedrock(pos.method_10078()) && HoleUtility.isBedrock(pos.method_10067()) && HoleUtility.isBedrock(pos.method_10095()) && HoleUtility.isBedrock(pos.method_10072()) && HoleUtility.isReplaceable(pos) && HoleUtility.isReplaceable(pos.method_10084()) && HoleUtility.isReplaceable(pos.method_10086(2));
    }

    public static boolean validTwoBlockBedrock(@NotNull class_2338 pos) {
        if (!HoleUtility.isReplaceable(pos)) {
            return false;
        }
        class_2382 addVec = HoleUtility.getTwoBlocksDirection(pos);
        if (addVec == null) {
            return false;
        }
        for (class_2338 checkPos : new class_2338[]{pos, pos.method_10081(addVec)}) {
            if (!HoleUtility.isReplaceable(checkPos.method_10084()) || !HoleUtility.isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            if (!HoleUtility.isBedrock(checkPos.method_10074())) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (HoleUtility.isBedrock(reducedPos) || reducedPos.equals((Object)pos) || reducedPos.equals((Object)pos.method_10081(addVec))) continue;
                return false;
            }
        }
        return true;
    }

    public static boolean validTwoBlockIndestructible(@NotNull class_2338 pos) {
        if (!HoleUtility.isReplaceable(pos)) {
            return false;
        }
        class_2382 addVec = HoleUtility.getTwoBlocksDirection(pos);
        if (addVec == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (class_2338 checkPos : new class_2338[]{pos, pos.method_10081(addVec)}) {
            if (HoleUtility.isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            } else if (!HoleUtility.isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!HoleUtility.isReplaceable(checkPos.method_10084()) || !HoleUtility.isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (HoleUtility.isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                    continue;
                }
                if (HoleUtility.isBedrock(reducedPos) || reducedPos.equals((Object)pos) || reducedPos.equals((Object)pos.method_10081(addVec))) continue;
                return false;
            }
        }
        return wasIndestructible;
    }

    @Nullable
    private static class_2382 getTwoBlocksDirection(class_2338 pos) {
        for (class_2382 vec : VECTOR_PATTERN) {
            if (!HoleUtility.isReplaceable(pos.method_10081(vec))) continue;
            return vec;
        }
        return null;
    }

    public static boolean validQuadIndestructible(@NotNull class_2338 pos) {
        List<class_2338> checkPoses = HoleUtility.getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (class_2338 checkPos : checkPoses) {
            if (HoleUtility.isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            } else if (!HoleUtility.isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!HoleUtility.isReplaceable(checkPos.method_10084()) || !HoleUtility.isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (HoleUtility.isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                    continue;
                }
                if (HoleUtility.isBedrock(reducedPos) || checkPoses.contains(reducedPos)) continue;
                return false;
            }
        }
        return wasIndestructible;
    }

    public static boolean validQuadBedrock(@NotNull class_2338 pos) {
        List<class_2338> checkPoses = HoleUtility.getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        for (class_2338 checkPos : checkPoses) {
            if (!HoleUtility.isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!HoleUtility.isReplaceable(checkPos.method_10084()) || !HoleUtility.isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (HoleUtility.isBedrock(reducedPos) || checkPoses.contains(reducedPos)) continue;
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static List<class_2338> getQuadDirection(@NotNull class_2338 pos) {
        int[][] groups;
        if (!HoleUtility.isReplaceable(pos)) {
            return null;
        }
        for (int[] g : groups = new int[][]{{1, 0, 0, 1, 1, 1}, {-1, 0, 0, -1, -1, -1}, {1, 0, 0, -1, 1, -1}, {-1, 0, 0, 1, -1, 1}}) {
            if (!HoleUtility.isReplaceable(pos.method_10069(g[0], 0, g[1])) || !HoleUtility.isReplaceable(pos.method_10069(g[2], 0, g[3])) || !HoleUtility.isReplaceable(pos.method_10069(g[4], 0, g[5]))) continue;
            return List.of(pos, pos.method_10069(g[0], 0, g[1]), pos.method_10069(g[2], 0, g[3]), pos.method_10069(g[4], 0, g[5]));
        }
        return null;
    }

    private static boolean isIndestructible(class_2338 bp) {
        if (HoleUtility.mc.field_1687 == null) {
            return false;
        }
        class_2248 block = HoleUtility.mc.field_1687.method_8320(bp).method_26204();
        return block == class_2246.field_10540 || block == class_2246.field_22108 || block == class_2246.field_22423 || block == class_2246.field_23152;
    }

    private static boolean isBedrock(class_2338 bp) {
        return HoleUtility.mc.field_1687 != null && HoleUtility.mc.field_1687.method_8320(bp).method_26204() == class_2246.field_9987;
    }

    private static boolean isReplaceable(class_2338 bp) {
        return HoleUtility.mc.field_1687 != null && HoleUtility.mc.field_1687.method_8320(bp).method_45474();
    }
}

