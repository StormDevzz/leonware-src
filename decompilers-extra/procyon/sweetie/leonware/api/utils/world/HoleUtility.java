// 
// Decompiled by Procyon v0.6.0
// 

package sweetie.leonware.api.utils.world;

import net.minecraft.class_2248;
import net.minecraft.class_2246;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import net.minecraft.class_2374;
import net.minecraft.class_2338;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import net.minecraft.class_243;
import net.minecraft.class_2382;
import net.minecraft.class_310;

public final class HoleUtility
{
    private static final class_310 mc;
    public static final class_2382[] VECTOR_PATTERN;
    
    @NotNull
    public static List<class_2338> getSurroundPoses(@NotNull final class_243 from) {
        final class_2338 fromPos = class_2338.method_49638((class_2374)from);
        final ArrayList<class_2338> tempOffsets = new ArrayList<class_2338>();
        final double decimalX = Math.abs(from.method_10216()) - Math.floor(Math.abs(from.method_10216()));
        final double decimalZ = Math.abs(from.method_10215()) - Math.floor(Math.abs(from.method_10215()));
        final int lengthXPos = calcLength(decimalX, false);
        final int lengthXNeg = calcLength(decimalX, true);
        final int lengthZPos = calcLength(decimalZ, false);
        final int lengthZNeg = calcLength(decimalZ, true);
        for (int x = 1; x < lengthXPos + 1; ++x) {
            tempOffsets.add(addToPlayer(fromPos, x, 0.0, 1 + lengthZPos));
            tempOffsets.add(addToPlayer(fromPos, x, 0.0, -(1 + lengthZNeg)));
        }
        for (int x = 0; x <= lengthXNeg; ++x) {
            tempOffsets.add(addToPlayer(fromPos, -x, 0.0, 1 + lengthZPos));
            tempOffsets.add(addToPlayer(fromPos, -x, 0.0, -(1 + lengthZNeg)));
        }
        for (int z = 1; z < lengthZPos + 1; ++z) {
            tempOffsets.add(addToPlayer(fromPos, 1 + lengthXPos, 0.0, z));
            tempOffsets.add(addToPlayer(fromPos, -(1 + lengthXNeg), 0.0, z));
        }
        for (int z = 0; z <= lengthZNeg; ++z) {
            tempOffsets.add(addToPlayer(fromPos, 1 + lengthXPos, 0.0, -z));
            tempOffsets.add(addToPlayer(fromPos, -(1 + lengthXNeg), 0.0, -z));
        }
        return tempOffsets;
    }
    
    @NotNull
    public static List<class_2338> getHolePoses(@NotNull final class_243 from) {
        final List<class_2338> positions = new ArrayList<class_2338>();
        final double decimalX = from.method_10216() - Math.floor(from.method_10216());
        final double decimalZ = from.method_10215() - Math.floor(from.method_10215());
        final int offX = calcOffset(decimalX);
        final int offZ = calcOffset(decimalZ);
        final class_2338 base = getPos(from);
        positions.add(base);
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                positions.add(base.method_10069(x * offX, 0, z * offZ));
            }
        }
        return positions;
    }
    
    @NotNull
    private static class_2338 getPos(@NotNull final class_243 from) {
        return class_2338.method_49637(from.method_10216(), (from.method_10214() - Math.floor(from.method_10214()) > 0.8) ? (Math.floor(from.method_10214()) + 1.0) : Math.floor(from.method_10214()), from.method_10215());
    }
    
    public static int calcOffset(final double dec) {
        return (dec >= 0.7) ? 1 : ((dec <= 0.3) ? -1 : 0);
    }
    
    public static int calcLength(final double decimal, final boolean negative) {
        if (negative) {
            return (decimal <= 0.3) ? 1 : 0;
        }
        return (decimal >= 0.7) ? 1 : 0;
    }
    
    public static class_2338 addToPlayer(@NotNull final class_2338 playerPos, double x, double y, double z) {
        if (playerPos.method_10263() < 0) {
            x = -x;
        }
        if (playerPos.method_10264() < 0) {
            y = -y;
        }
        if (playerPos.method_10260() < 0) {
            z = -z;
        }
        return playerPos.method_10081((class_2382)class_2338.method_49637(x, y, z));
    }
    
    public static boolean isHole(final class_2338 pos) {
        return isSingleHole(pos) || validTwoBlockIndestructible(pos) || validTwoBlockBedrock(pos) || validQuadIndestructible(pos) || validQuadBedrock(pos);
    }
    
    public static boolean isSingleHole(final class_2338 pos) {
        return validIndestructible(pos) || validBedrock(pos);
    }
    
    public static boolean validIndestructible(@NotNull final class_2338 pos) {
        return !validBedrock(pos) && isIndestructible(pos.method_10074()) && (isIndestructible(pos.method_10078()) || isBedrock(pos.method_10078())) && (isIndestructible(pos.method_10067()) || isBedrock(pos.method_10067())) && (isIndestructible(pos.method_10095()) || isBedrock(pos.method_10095())) && (isIndestructible(pos.method_10072()) || isBedrock(pos.method_10072())) && isReplaceable(pos) && isReplaceable(pos.method_10084()) && isReplaceable(pos.method_10086(2));
    }
    
    public static boolean validBedrock(@NotNull final class_2338 pos) {
        return isBedrock(pos.method_10074()) && isBedrock(pos.method_10078()) && isBedrock(pos.method_10067()) && isBedrock(pos.method_10095()) && isBedrock(pos.method_10072()) && isReplaceable(pos) && isReplaceable(pos.method_10084()) && isReplaceable(pos.method_10086(2));
    }
    
    public static boolean validTwoBlockBedrock(@NotNull final class_2338 pos) {
        if (!isReplaceable(pos)) {
            return false;
        }
        final class_2382 addVec = getTwoBlocksDirection(pos);
        if (addVec == null) {
            return false;
        }
        for (final class_2338 checkPos : new class_2338[] { pos, pos.method_10081(addVec) }) {
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            for (final class_2382 vec : HoleUtility.VECTOR_PATTERN) {
                final class_2338 reducedPos = checkPos.method_10081(vec);
                if (!isBedrock(reducedPos) && !reducedPos.equals((Object)pos) && !reducedPos.equals((Object)pos.method_10081(addVec))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean validTwoBlockIndestructible(@NotNull final class_2338 pos) {
        if (!isReplaceable(pos)) {
            return false;
        }
        final class_2382 addVec = getTwoBlocksDirection(pos);
        if (addVec == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (final class_2338 checkPos : new class_2338[] { pos, pos.method_10081(addVec) }) {
            if (isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            }
            else if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (final class_2382 vec : HoleUtility.VECTOR_PATTERN) {
                final class_2338 reducedPos = checkPos.method_10081(vec);
                if (isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                }
                else if (!isBedrock(reducedPos) && !reducedPos.equals((Object)pos) && !reducedPos.equals((Object)pos.method_10081(addVec))) {
                    return false;
                }
            }
        }
        return wasIndestructible;
    }
    
    @Nullable
    private static class_2382 getTwoBlocksDirection(final class_2338 pos) {
        for (final class_2382 vec : HoleUtility.VECTOR_PATTERN) {
            if (isReplaceable(pos.method_10081(vec))) {
                return vec;
            }
        }
        return null;
    }
    
    public static boolean validQuadIndestructible(@NotNull final class_2338 pos) {
        final List<class_2338> checkPoses = getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        boolean wasIndestructible = false;
        for (final class_2338 checkPos : checkPoses) {
            if (isIndestructible(checkPos.method_10074())) {
                wasIndestructible = true;
            }
            else if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (final class_2382 vec : HoleUtility.VECTOR_PATTERN) {
                final class_2338 reducedPos = checkPos.method_10081(vec);
                if (isIndestructible(reducedPos)) {
                    wasIndestructible = true;
                }
                else if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                    return false;
                }
            }
        }
        return wasIndestructible;
    }
    
    public static boolean validQuadBedrock(@NotNull final class_2338 pos) {
        final List<class_2338> checkPoses = getQuadDirection(pos);
        if (checkPoses == null) {
            return false;
        }
        for (final class_2338 checkPos : checkPoses) {
            if (!isBedrock(checkPos.method_10074())) {
                return false;
            }
            if (!isReplaceable(checkPos.method_10084()) || !isReplaceable(checkPos.method_10086(2))) {
                return false;
            }
            for (final class_2382 vec : HoleUtility.VECTOR_PATTERN) {
                final class_2338 reducedPos = checkPos.method_10081(vec);
                if (!isBedrock(reducedPos) && !checkPoses.contains(reducedPos)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Nullable
    private static List<class_2338> getQuadDirection(@NotNull final class_2338 pos) {
        if (!isReplaceable(pos)) {
            return null;
        }
        final int[][] array;
        final int[][] groups = array = new int[][] { { 1, 0, 0, 1, 1, 1 }, { -1, 0, 0, -1, -1, -1 }, { 1, 0, 0, -1, 1, -1 }, { -1, 0, 0, 1, -1, 1 } };
        for (final int[] g : array) {
            if (isReplaceable(pos.method_10069(g[0], 0, g[1])) && isReplaceable(pos.method_10069(g[2], 0, g[3])) && isReplaceable(pos.method_10069(g[4], 0, g[5]))) {
                return List.of(pos, pos.method_10069(g[0], 0, g[1]), pos.method_10069(g[2], 0, g[3]), pos.method_10069(g[4], 0, g[5]));
            }
        }
        return null;
    }
    
    private static boolean isIndestructible(final class_2338 bp) {
        if (HoleUtility.mc.field_1687 == null) {
            return false;
        }
        final class_2248 block = HoleUtility.mc.field_1687.method_8320(bp).method_26204();
        return block == class_2246.field_10540 || block == class_2246.field_22108 || block == class_2246.field_22423 || block == class_2246.field_23152;
    }
    
    private static boolean isBedrock(final class_2338 bp) {
        return HoleUtility.mc.field_1687 != null && HoleUtility.mc.field_1687.method_8320(bp).method_26204() == class_2246.field_9987;
    }
    
    private static boolean isReplaceable(final class_2338 bp) {
        return HoleUtility.mc.field_1687 != null && HoleUtility.mc.field_1687.method_8320(bp).method_45474();
    }
    
    static {
        mc = class_310.method_1551();
        VECTOR_PATTERN = new class_2382[] { new class_2382(0, 0, 1), new class_2382(0, 0, -1), new class_2382(1, 0, 0), new class_2382(-1, 0, 0) };
    }
}
