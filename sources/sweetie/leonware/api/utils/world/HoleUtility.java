package sweetie.leonware.api.utils.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: leonware-0.0.3.jar:sweetie/leonware/api/utils/world/HoleUtility.class */
public final class HoleUtility {
    private static final class_310 mc = class_310.method_1551();
    public static final class_2382[] VECTOR_PATTERN = {new class_2382(0, 0, 1), new class_2382(0, 0, -1), new class_2382(1, 0, 0), new class_2382(-1, 0, 0)};

    @NotNull
    public static List<class_2338> getSurroundPoses(@NotNull class_243 from) {
        class_2338 fromPos = class_2338.method_49638(from);
        ArrayList<class_2338> tempOffsets = new ArrayList<>();
        double decimalX = Math.abs(from.method_10216()) - Math.floor(Math.abs(from.method_10216()));
        double decimalZ = Math.abs(from.method_10215()) - Math.floor(Math.abs(from.method_10215()));
        int lengthXPos = calcLength(decimalX, false);
        int lengthXNeg = calcLength(decimalX, true);
        int lengthZPos = calcLength(decimalZ, false);
        int lengthZNeg = calcLength(decimalZ, true);
        for (int x = 1; x < lengthXPos + 1; x++) {
            tempOffsets.add(addToPlayer(fromPos, x, 0.0d, 1 + lengthZPos));
            tempOffsets.add(addToPlayer(fromPos, x, 0.0d, -(1 + lengthZNeg)));
        }
        for (int x2 = 0; x2 <= lengthXNeg; x2++) {
            tempOffsets.add(addToPlayer(fromPos, -x2, 0.0d, 1 + lengthZPos));
            tempOffsets.add(addToPlayer(fromPos, -x2, 0.0d, -(1 + lengthZNeg)));
        }
        for (int z = 1; z < lengthZPos + 1; z++) {
            tempOffsets.add(addToPlayer(fromPos, 1 + lengthXPos, 0.0d, z));
            tempOffsets.add(addToPlayer(fromPos, -(1 + lengthXNeg), 0.0d, z));
        }
        for (int z2 = 0; z2 <= lengthZNeg; z2++) {
            tempOffsets.add(addToPlayer(fromPos, 1 + lengthXPos, 0.0d, -z2));
            tempOffsets.add(addToPlayer(fromPos, -(1 + lengthXNeg), 0.0d, -z2));
        }
        return tempOffsets;
    }

    @NotNull
    public static List<class_2338> getHolePoses(@NotNull class_243 from) {
        List<class_2338> positions = new ArrayList<>();
        double decimalX = from.method_10216() - Math.floor(from.method_10216());
        double decimalZ = from.method_10215() - Math.floor(from.method_10215());
        int offX = calcOffset(decimalX);
        int offZ = calcOffset(decimalZ);
        class_2338 base = getPos(from);
        positions.add(base);
        for (int x = 0; x <= Math.abs(offX); x++) {
            for (int z = 0; z <= Math.abs(offZ); z++) {
                positions.add(base.method_10069(x * offX, 0, z * offZ));
            }
        }
        return positions;
    }

    @NotNull
    private static class_2338 getPos(@NotNull class_243 from) {
        return class_2338.method_49637(from.method_10216(), from.method_10214() - Math.floor(from.method_10214()) > 0.8d ? Math.floor(from.method_10214()) + 1.0d : Math.floor(from.method_10214()), from.method_10215());
    }

    public static int calcOffset(double dec) {
        if (dec >= 0.7d) {
            return 1;
        }
        return dec <= 0.3d ? -1 : 0;
    }

    public static int calcLength(double decimal, boolean negative) {
        return negative ? decimal <= 0.3d ? 1 : 0 : decimal >= 0.7d ? 1 : 0;
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
        return playerPos.method_10081(class_2338.method_49637(x, y, z));
    }

    public static boolean isHole(class_2338 pos) {
        return isSingleHole(pos) || validTwoBlockIndestructible(pos) || validTwoBlockBedrock(pos) || validQuadIndestructible(pos) || validQuadBedrock(pos);
    }

    public static boolean isSingleHole(class_2338 pos) {
        return validIndestructible(pos) || validBedrock(pos);
    }

    public static boolean validIndestructible(@NotNull class_2338 pos) {
        return !validBedrock(pos) && isIndestructible(pos.method_10074()) && (isIndestructible(pos.method_10078()) || isBedrock(pos.method_10078())) && ((isIndestructible(pos.method_10067()) || isBedrock(pos.method_10067())) && ((isIndestructible(pos.method_10095()) || isBedrock(pos.method_10095())) && ((isIndestructible(pos.method_10072()) || isBedrock(pos.method_10072())) && isReplaceable(pos) && isReplaceable(pos.method_10084()) && isReplaceable(pos.method_10086(2)))));
    }

    public static boolean validBedrock(@NotNull class_2338 pos) {
        return isBedrock(pos.method_10074()) && isBedrock(pos.method_10078()) && isBedrock(pos.method_10067()) && isBedrock(pos.method_10095()) && isBedrock(pos.method_10072()) && isReplaceable(pos) && isReplaceable(pos.method_10084()) && isReplaceable(pos.method_10086(2));
    }

    public static boolean validTwoBlockBedrock(@NotNull class_2338 pos) {
        class_2382 addVec;
        if (!isReplaceable(pos) || (addVec = getTwoBlocksDirection(pos)) == null) {
            return false;
        }
        for (class_2338 checkPos : new class_2338[]{pos, pos.method_10081(addVec)}) {
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2)) || !isBedrock(checkPos.method_10074())) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (!isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validTwoBlockIndestructible(@NotNull class_2338 pos) {
        class_2382 addVec;
        if (!isReplaceable(pos) || (addVec = getTwoBlocksDirection(pos)) == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (class_2338 checkPos : new class_2338[]{pos, pos.method_10081(addVec)}) {
            if (isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            } else if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                } else if (!isBedrock(reducedPos) && !reducedPos.equals(pos) && !reducedPos.equals(pos.method_10081(addVec))) {
                    return false;
                }
            }
        }
        return wasIndestructible;
    }

    @Nullable
    private static class_2382 getTwoBlocksDirection(class_2338 pos) {
        for (class_2382 vec : VECTOR_PATTERN) {
            if (isReplaceable(pos.method_10081(vec))) {
                return vec;
            }
        }
        return null;
    }

    public static boolean validQuadIndestructible(@NotNull class_2338 pos) {
        List<class_2338> checkPoses = getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (class_2338 checkPos : checkPoses) {
            if (isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            } else if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                } else if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                    return false;
                }
            }
        }
        return wasIndestructible;
    }

    public static boolean validQuadBedrock(@NotNull class_2338 pos) {
        List<class_2338> checkPoses = getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        for (class_2338 checkPos : checkPoses) {
            if (!isBedrock(checkPos.method_10074()) || !isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (class_2382 vec : VECTOR_PATTERN) {
                class_2338 reducedPos = checkPos.method_10081(vec);
                if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Nullable
    private static List<class_2338> getQuadDirection(@NotNull class_2338 pos) {
        if (!isReplaceable(pos)) {
            return null;
        }
        for (Object[] objArr : new int[]{new int[]{1, 0, 0, 1, 1, 1}, new int[]{-1, 0, 0, -1, -1, -1}, new int[]{1, 0, 0, -1, 1, -1}, new int[]{-1, 0, 0, 1, -1, 1}}) {
            if (isReplaceable(pos.method_10069(objArr[0], 0, objArr[1])) && isReplaceable(pos.method_10069(objArr[2], 0, objArr[3])) && isReplaceable(pos.method_10069(objArr[4], 0, objArr[5]))) {
                return List.of(pos, pos.method_10069(objArr[0], 0, objArr[1]), pos.method_10069(objArr[2], 0, objArr[3]), pos.method_10069(objArr[4], 0, objArr[5]));
            }
        }
        return null;
    }

    private static boolean isIndestructible(class_2338 bp) {
        if (mc.field_1687 == null) {
            return false;
        }
        class_2248 block = mc.field_1687.method_8320(bp).method_26204();
        return block == class_2246.field_10540 || block == class_2246.field_22108 || block == class_2246.field_22423 || block == class_2246.field_23152;
    }

    private static boolean isBedrock(class_2338 bp) {
        return mc.field_1687 != null && mc.field_1687.method_8320(bp).method_26204() == class_2246.field_9987;
    }

    private static boolean isReplaceable(class_2338 bp) {
        return mc.field_1687 != null && mc.field_1687.method_8320(bp).method_45474();
    }
}
