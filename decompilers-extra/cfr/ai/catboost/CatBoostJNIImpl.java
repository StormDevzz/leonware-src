/*
 * Decompiled with CFR 0.152.
 */
package ai.catboost;

import ai.catboost.CatBoostError;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

class CatBoostJNIImpl {
    CatBoostJNIImpl() {
    }

    static final void checkCall(@Nullable String message) throws CatBoostError {
        if (message != null) {
            throw new CatBoostError(message);
        }
    }

    @Nullable
    static final native String catBoostHashCatFeature(@NotNull String var0, @NotNull int[] var1);

    @Nullable
    static final native String catBoostHashCatFeatures(@NotNull String[] var0, @NotNull int[] var1);

    @Nullable
    static final native String catBoostLoadModelFromFile(@NotNull String var0, @NotNull long[] var1, @Nullable String var2);

    @Nullable
    static final native String catBoostLoadModelFromArray(@NotNull byte[] var0, @NotNull long[] var1, @Nullable String var2);

    @Nullable
    static final native String catBoostFreeModel(long var0);

    @Nullable
    static final native String catBoostModelGetSupportedEvaluatorTypes(long var0, @NotNull String[][] var2);

    @Nullable
    static final native String catBoostModelSetEvaluatorType(long var0, @NotNull String var2);

    @Nullable
    static final native String catBoostModelGetEvaluatorType(long var0, @NotNull String[] var2);

    @Nullable
    static final native String catBoostModelGetPredictionDimension(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetUsedNumericFeatureCount(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetUsedCategoricalFeatureCount(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetUsedTextFeatureCount(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetUsedEmbeddingFeatureCount(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetFlatFeatureVectorExpectedSize(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelGetMetadata(long var0, @NotNull String[][] var2, @NotNull String[][] var3);

    @Nullable
    static final native String catBoostModelGetFloatFeatures(long var0, @NotNull String[][] var2, @NotNull int[][] var3, @NotNull int[][] var4, @NotNull int[][] var5, @NotNull String[][] var6);

    @Nullable
    static final native String catBoostModelGetCatFeatures(long var0, @NotNull String[][] var2, @NotNull int[][] var3, @NotNull int[][] var4);

    @Nullable
    static final native String catBoostModelGetTextFeatures(long var0, @NotNull String[][] var2, @NotNull int[][] var3, @NotNull int[][] var4);

    @Nullable
    static final native String catBoostModelGetEmbeddingFeatures(long var0, @NotNull String[][] var2, @NotNull int[][] var3, @NotNull int[][] var4);

    @Nullable
    static final native String catBoostModelGetUsedFeatureIndices(long var0, @NotNull int[][] var2);

    @Nullable
    static final native String catBoostModelGetTreeCount(long var0, @NotNull int[] var2);

    @Nullable
    static final native String catBoostModelPredict(long var0, @Nullable float[] var2, @Nullable String[] var3, @Nullable String[] var4, @Nullable float[][] var5, @NotNull double[] var6);

    @Nullable
    static final native String catBoostModelPredict(long var0, @Nullable float[] var2, @Nullable int[] var3, @Nullable String[] var4, @Nullable float[][] var5, @NotNull double[] var6);

    @Nullable
    static final native String catBoostModelPredict(long var0, @Nullable float[][] var2, @Nullable String[][] var3, @Nullable String[][] var4, @Nullable float[][][] var5, @NotNull double[] var6);

    @Nullable
    static final native String catBoostModelPredict(long var0, @Nullable float[][] var2, @Nullable int[][] var3, @Nullable String[][] var4, @Nullable float[][][] var5, @NotNull double[] var6);
}

