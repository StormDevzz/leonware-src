// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost;

import javax.validation.constraints.NotNull;
import javax.annotation.Nullable;

class CatBoostJNIImpl
{
    static final void checkCall(@Nullable final String message) throws CatBoostError {
        if (message != null) {
            throw new CatBoostError(message);
        }
    }
    
    @Nullable
    static final native String catBoostHashCatFeature(@NotNull final String p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostHashCatFeatures(@NotNull final String[] p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostLoadModelFromFile(@NotNull final String p0, @NotNull final long[] p1, @Nullable final String p2);
    
    @Nullable
    static final native String catBoostLoadModelFromArray(@NotNull final byte[] p0, @NotNull final long[] p1, @Nullable final String p2);
    
    @Nullable
    static final native String catBoostFreeModel(final long p0);
    
    @Nullable
    static final native String catBoostModelGetSupportedEvaluatorTypes(final long p0, @NotNull final String[][] p1);
    
    @Nullable
    static final native String catBoostModelSetEvaluatorType(final long p0, @NotNull final String p1);
    
    @Nullable
    static final native String catBoostModelGetEvaluatorType(final long p0, @NotNull final String[] p1);
    
    @Nullable
    static final native String catBoostModelGetPredictionDimension(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetUsedNumericFeatureCount(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetUsedCategoricalFeatureCount(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetUsedTextFeatureCount(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetUsedEmbeddingFeatureCount(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetFlatFeatureVectorExpectedSize(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelGetMetadata(final long p0, @NotNull final String[][] p1, @NotNull final String[][] p2);
    
    @Nullable
    static final native String catBoostModelGetFloatFeatures(final long p0, @NotNull final String[][] p1, @NotNull final int[][] p2, @NotNull final int[][] p3, @NotNull final int[][] p4, @NotNull final String[][] p5);
    
    @Nullable
    static final native String catBoostModelGetCatFeatures(final long p0, @NotNull final String[][] p1, @NotNull final int[][] p2, @NotNull final int[][] p3);
    
    @Nullable
    static final native String catBoostModelGetTextFeatures(final long p0, @NotNull final String[][] p1, @NotNull final int[][] p2, @NotNull final int[][] p3);
    
    @Nullable
    static final native String catBoostModelGetEmbeddingFeatures(final long p0, @NotNull final String[][] p1, @NotNull final int[][] p2, @NotNull final int[][] p3);
    
    @Nullable
    static final native String catBoostModelGetUsedFeatureIndices(final long p0, @NotNull final int[][] p1);
    
    @Nullable
    static final native String catBoostModelGetTreeCount(final long p0, @NotNull final int[] p1);
    
    @Nullable
    static final native String catBoostModelPredict(final long p0, @Nullable final float[] p1, @Nullable final String[] p2, @Nullable final String[] p3, @Nullable final float[][] p4, @NotNull final double[] p5);
    
    @Nullable
    static final native String catBoostModelPredict(final long p0, @Nullable final float[] p1, @Nullable final int[] p2, @Nullable final String[] p3, @Nullable final float[][] p4, @NotNull final double[] p5);
    
    @Nullable
    static final native String catBoostModelPredict(final long p0, @Nullable final float[][] p1, @Nullable final String[][] p2, @Nullable final String[][] p3, @Nullable final float[][][] p4, @NotNull final double[] p5);
    
    @Nullable
    static final native String catBoostModelPredict(final long p0, @Nullable final float[][] p1, @Nullable final int[][] p2, @Nullable final String[][] p3, @Nullable final float[][][] p4, @NotNull final double[] p5);
}
