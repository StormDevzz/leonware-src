/*
 * Decompiled with CFR 0.152.
 */
package ai.catboost;

import ai.catboost.CatBoostError;
import ai.catboost.CatBoostJNIImpl;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

class CatBoostJNI {
    CatBoostJNI() {
    }

    final void catBoostHashCatFeature(@NotNull String catFeature, @NotNull int[] hash) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostHashCatFeature(catFeature, hash));
    }

    final void catBoostHashCatFeatures(@NotNull String[] catFeatures, @NotNull int[] hashes) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostHashCatFeatures(catFeatures, hashes));
    }

    final void catBoostLoadModelFromFile(@NotNull String fname, @NotNull long[] handle, @Nullable String formatName) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostLoadModelFromFile(fname, handle, formatName));
    }

    final void catBoostLoadModelFromArray(@NotNull byte[] data, @NotNull long[] handle, @Nullable String formatName) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostLoadModelFromArray(data, handle, formatName));
    }

    final void catBoostFreeModel(long handle) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostFreeModel(handle));
    }

    final void catBoostModelGetSupportedEvaluatorTypes(long handle, @NotNull String[][] evaluatorTypes) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetSupportedEvaluatorTypes(handle, evaluatorTypes));
    }

    final void catBoostModelSetEvaluatorType(long handle, @NotNull String evaluatorType) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelSetEvaluatorType(handle, evaluatorType));
    }

    final void catBoostModelGetEvaluatorType(long handle, @NotNull String[] evaluatorType) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetEvaluatorType(handle, evaluatorType));
    }

    final void catBoostModelGetPredictionDimension(long handle, @NotNull int[] classesCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetPredictionDimension(handle, classesCount));
    }

    final void catBoostModelGetUsedNumericFeatureCount(long handle, @NotNull int[] numericFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedNumericFeatureCount(handle, numericFeatureCount));
    }

    final void catBoostModelGetUsedCategoricalFeatureCount(long handle, @NotNull int[] catFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedCategoricalFeatureCount(handle, catFeatureCount));
    }

    final void catBoostModelGetUsedTextFeatureCount(long handle, @NotNull int[] textFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedTextFeatureCount(handle, textFeatureCount));
    }

    final void catBoostModelGetUsedEmbeddingFeatureCount(long handle, @NotNull int[] embeddingFeatureCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedEmbeddingFeatureCount(handle, embeddingFeatureCount));
    }

    final void catBoostModelGetFlatFeatureVectorExpectedSize(long handle, @NotNull int[] featureVectorExpectedSize) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetFlatFeatureVectorExpectedSize(handle, featureVectorExpectedSize));
    }

    final void catBoostModelGetMetadata(long handle, @NotNull String[][] keys, @NotNull String[][] values) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetMetadata(handle, keys, values));
    }

    final void catBoostModelGetFloatFeatures(long handle, @NotNull String[][] names, @NotNull int[][] flat_feature_index, @NotNull int[][] feature_index, @NotNull int[][] has_nans, @NotNull String[][] nan_value_treatment) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetFloatFeatures(handle, names, flat_feature_index, feature_index, has_nans, nan_value_treatment));
    }

    final void catBoostModelGetCatFeatures(long handle, @NotNull String[][] names, @NotNull int[][] flat_feature_index, @NotNull int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetCatFeatures(handle, names, flat_feature_index, feature_index));
    }

    final void catBoostModelGetTextFeatures(long handle, @NotNull String[][] names, @NotNull int[][] flat_feature_index, @NotNull int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetTextFeatures(handle, names, flat_feature_index, feature_index));
    }

    final void catBoostModelGetEmbeddingFeatures(long handle, @NotNull String[][] names, @NotNull int[][] flat_feature_index, @NotNull int[][] feature_index) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetEmbeddingFeatures(handle, names, flat_feature_index, feature_index));
    }

    final void catBoostModelGetUsedFeatureIndices(long handle, @NotNull int[][] featureIndices) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetUsedFeatureIndices(handle, featureIndices));
    }

    final void catBoostModelGetTreeCount(long handle, @NotNull int[] treeCount) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelGetTreeCount(handle, treeCount));
    }

    final void catBoostModelPredict(long handle, @Nullable float[] numericFeatures, @Nullable String[] catFeatures, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, predictions));
    }

    final void catBoostModelPredict(long handle, @Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, predictions));
    }

    final void catBoostModelPredict(long handle, @Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, predictions));
    }

    final void catBoostModelPredict(long handle, @Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull double[] predictions) throws CatBoostError {
        CatBoostJNIImpl.checkCall(CatBoostJNIImpl.catBoostModelPredict(handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, predictions));
    }
}

