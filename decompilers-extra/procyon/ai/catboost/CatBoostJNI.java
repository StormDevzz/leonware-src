// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

class CatBoostJNI
{
    final void catBoostHashCatFeature(@NotNull final String catFeature, @NotNull final int[] hash) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostHashCatFeature(catFeature, hash));
    }
    
    final void catBoostHashCatFeatures(@NotNull final String[] catFeatures, @NotNull final int[] hashes) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostHashCatFeatures(catFeatures, hashes));
    }
    
    final void catBoostLoadModelFromFile(@NotNull final String fname, @NotNull final long[] handle, @Nullable final String formatName) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostLoadModelFromFile(fname, handle, formatName));
    }
    
    final void catBoostLoadModelFromArray(@NotNull final byte[] data, @NotNull final long[] handle, @Nullable final String formatName) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostLoadModelFromArray(data, handle, formatName));
    }
    
    final void catBoostFreeModel(final long handle) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostFreeModel(handle));
    }
    
    final void catBoostModelGetSupportedEvaluatorTypes(final long handle, @NotNull final String[][] evaluatorTypes) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetSupportedEvaluatorTypes(handle, evaluatorTypes));
    }
    
    final void catBoostModelSetEvaluatorType(final long handle, @NotNull final String evaluatorType) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelSetEvaluatorType(handle, evaluatorType));
    }
    
    final void catBoostModelGetEvaluatorType(final long handle, @NotNull final String[] evaluatorType) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetEvaluatorType(handle, evaluatorType));
    }
    
    final void catBoostModelGetPredictionDimension(final long handle, @NotNull final int[] classesCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetPredictionDimension(handle, classesCount));
    }
    
    final void catBoostModelGetUsedNumericFeatureCount(final long handle, @NotNull final int[] numericFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedNumericFeatureCount(handle, numericFeatureCount));
    }
    
    final void catBoostModelGetUsedCategoricalFeatureCount(final long handle, @NotNull final int[] catFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedCategoricalFeatureCount(handle, catFeatureCount));
    }
    
    final void catBoostModelGetUsedTextFeatureCount(final long handle, @NotNull final int[] textFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedTextFeatureCount(handle, textFeatureCount));
    }
    
    final void catBoostModelGetUsedEmbeddingFeatureCount(final long handle, @NotNull final int[] embeddingFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedEmbeddingFeatureCount(handle, embeddingFeatureCount));
    }
    
    final void catBoostModelGetFlatFeatureVectorExpectedSize(final long handle, @NotNull final int[] featureVectorExpectedSize) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetFlatFeatureVectorExpectedSize(handle, featureVectorExpectedSize));
    }
    
    final void catBoostModelGetMetadata(final long handle, @NotNull final String[][] keys, @NotNull final String[][] values) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetMetadata(handle, keys, values));
    }
    
    final void catBoostModelGetFloatFeatures(final long handle, @NotNull final String[][] names, @NotNull final int[][] flat_feature_index, @NotNull final int[][] feature_index, @NotNull final int[][] has_nans, @NotNull final String[][] nan_value_treatment) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetFloatFeatures(handle, names, flat_feature_index, feature_index, has_nans, nan_value_treatment));
    }
    
    final void catBoostModelGetCatFeatures(final long handle, @NotNull final String[][] names, @NotNull final int[][] flat_feature_index, @NotNull final int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetCatFeatures(handle, names, flat_feature_index, feature_index));
    }
    
    final void catBoostModelGetTextFeatures(final long handle, @NotNull final String[][] names, @NotNull final int[][] flat_feature_index, @NotNull final int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetTextFeatures(handle, names, flat_feature_index, feature_index));
    }
    
    final void catBoostModelGetEmbeddingFeatures(final long handle, @NotNull final String[][] names, @NotNull final int[][] flat_feature_index, @NotNull final int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetEmbeddingFeatures(handle, names, flat_feature_index, feature_index));
    }
    
    final void catBoostModelGetUsedFeatureIndices(final long handle, @NotNull final int[][] featureIndices) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedFeatureIndices(handle, featureIndices));
    }
    
    final void catBoostModelGetTreeCount(final long handle, @NotNull final int[] treeCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetTreeCount(handle, treeCount));
    }
    
    final void catBoostModelPredict(final long handle, @Nullable final float[] numericFeatures, @Nullable final String[] catFeatures, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures, @NotNull final double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, predictions));
    }
    
    final void catBoostModelPredict(final long handle, @Nullable final float[] numericFeatures, @Nullable final int[] catFeatureHashes, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures, @NotNull final double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, predictions));
    }
    
    final void catBoostModelPredict(final long handle, @Nullable final float[][] numericFeatures, @Nullable final String[][] catFeatures, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures, @NotNull final double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, predictions));
    }
    
    final void catBoostModelPredict(final long handle, @Nullable final float[][] numericFeatures, @Nullable final int[][] catFeatureHashes, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures, @NotNull final double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, predictions));
    }
}
