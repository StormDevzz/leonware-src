package ai.catboost;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostJNIImpl.class */
class CatBoostJNIImpl {
    @Nullable
    static final native String catBoostHashCatFeature(@NotNull String str, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostHashCatFeatures(@NotNull String[] strArr, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostLoadModelFromFile(@NotNull String str, @NotNull long[] jArr, @Nullable String str2);

    @Nullable
    static final native String catBoostLoadModelFromArray(@NotNull byte[] bArr, @NotNull long[] jArr, @Nullable String str);

    @Nullable
    static final native String catBoostFreeModel(long j);

    @Nullable
    static final native String catBoostModelGetSupportedEvaluatorTypes(long j, @NotNull String[][] strArr);

    @Nullable
    static final native String catBoostModelSetEvaluatorType(long j, @NotNull String str);

    @Nullable
    static final native String catBoostModelGetEvaluatorType(long j, @NotNull String[] strArr);

    @Nullable
    static final native String catBoostModelGetPredictionDimension(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetUsedNumericFeatureCount(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetUsedCategoricalFeatureCount(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetUsedTextFeatureCount(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetUsedEmbeddingFeatureCount(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetFlatFeatureVectorExpectedSize(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelGetMetadata(long j, @NotNull String[][] strArr, @NotNull String[][] strArr2);

    @Nullable
    static final native String catBoostModelGetFloatFeatures(long j, @NotNull String[][] strArr, @NotNull int[][] iArr, @NotNull int[][] iArr2, @NotNull int[][] iArr3, @NotNull String[][] strArr2);

    @Nullable
    static final native String catBoostModelGetCatFeatures(long j, @NotNull String[][] strArr, @NotNull int[][] iArr, @NotNull int[][] iArr2);

    @Nullable
    static final native String catBoostModelGetTextFeatures(long j, @NotNull String[][] strArr, @NotNull int[][] iArr, @NotNull int[][] iArr2);

    @Nullable
    static final native String catBoostModelGetEmbeddingFeatures(long j, @NotNull String[][] strArr, @NotNull int[][] iArr, @NotNull int[][] iArr2);

    @Nullable
    static final native String catBoostModelGetUsedFeatureIndices(long j, @NotNull int[][] iArr);

    @Nullable
    static final native String catBoostModelGetTreeCount(long j, @NotNull int[] iArr);

    @Nullable
    static final native String catBoostModelPredict(long j, @Nullable float[] fArr, @Nullable String[] strArr, @Nullable String[] strArr2, @Nullable float[][] fArr2, @NotNull double[] dArr);

    @Nullable
    static final native String catBoostModelPredict(long j, @Nullable float[] fArr, @Nullable int[] iArr, @Nullable String[] strArr, @Nullable float[][] fArr2, @NotNull double[] dArr);

    @Nullable
    static final native String catBoostModelPredict(long j, @Nullable float[][] fArr, @Nullable String[][] strArr, @Nullable String[][] strArr2, @Nullable float[][][] fArr2, @NotNull double[] dArr);

    @Nullable
    static final native String catBoostModelPredict(long j, @Nullable float[][] fArr, @Nullable int[][] iArr, @Nullable String[][] strArr, @Nullable float[][][] fArr2, @NotNull double[] dArr);

    CatBoostJNIImpl() {
    }

    static final void checkCall(@Nullable String message) throws CatBoostError {
        if (message != null) {
            throw new CatBoostError(message);
        }
    }
}
