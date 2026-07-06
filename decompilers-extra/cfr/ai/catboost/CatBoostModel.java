/*
 * Decompiled with CFR 0.152.
 */
package ai.catboost;

import ai.catboost.CatBoostError;
import ai.catboost.CatBoostJNI;
import ai.catboost.CatBoostPredictions;
import ai.catboost.common.NativeLib;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public class CatBoostModel
implements AutoCloseable {
    private long handle = 0L;
    private int predictionDimension = 0;
    private int treeCount = 0;
    private int usedNumericFeatureCount = 0;
    private int usedCategoricFeatureCount = 0;
    private int usedTextFeatureCount = 0;
    private int usedEmbeddingFeatureCount = 0;
    private String[] featureNames;
    private Map<String, String> metadata = new HashMap<String, String>();
    private List<Feature> features = new ArrayList<Feature>();
    private static CatBoostJNI implLibrary = null;

    private CatBoostModel(long handle) throws CatBoostError {
        int i;
        this.handle = handle;
        int[] predictionDimension = new int[1];
        int[] treeCount = new int[1];
        int[] usedNumericFeatureCount = new int[1];
        int[] usedCatFeatureCount = new int[1];
        int[] usedTextFeatureCount = new int[1];
        int[] usedEmbeddingFeatureCount = new int[1];
        int[] featureVectorExpectedSize = new int[1];
        String[][] modelMetadataKeys = new String[1][];
        String[][] modelMetadataValues = new String[1][];
        String[][] floatFeatureNames = new String[1][];
        int[][] floatFlatFeatureIndex = new int[1][];
        int[][] floatFeatureIndex = new int[1][];
        int[][] floatHasNans = new int[1][];
        String[][] floatNanValueTreatment = new String[1][];
        String[][] catFeatureNames = new String[1][];
        int[][] catFlatFeatureIndex = new int[1][];
        int[][] catFeatureIndex = new int[1][];
        String[][] textFeatureNames = new String[1][];
        int[][] textFlatFeatureIndex = new int[1][];
        int[][] textFeatureIndex = new int[1][];
        String[][] embeddingFeatureNames = new String[1][];
        int[][] embeddingFlatFeatureIndex = new int[1][];
        int[][] embeddingFeatureIndex = new int[1][];
        int[][] usedFeatureIndicesArr = new int[1][];
        try {
            implLibrary.catBoostModelGetPredictionDimension(handle, predictionDimension);
            implLibrary.catBoostModelGetTreeCount(handle, treeCount);
            implLibrary.catBoostModelGetUsedNumericFeatureCount(handle, usedNumericFeatureCount);
            implLibrary.catBoostModelGetUsedCategoricalFeatureCount(handle, usedCatFeatureCount);
            implLibrary.catBoostModelGetUsedTextFeatureCount(handle, usedTextFeatureCount);
            implLibrary.catBoostModelGetUsedEmbeddingFeatureCount(handle, usedEmbeddingFeatureCount);
            implLibrary.catBoostModelGetFlatFeatureVectorExpectedSize(handle, featureVectorExpectedSize);
            implLibrary.catBoostModelGetMetadata(handle, modelMetadataKeys, modelMetadataValues);
            implLibrary.catBoostModelGetFloatFeatures(handle, floatFeatureNames, floatFlatFeatureIndex, floatFeatureIndex, floatHasNans, floatNanValueTreatment);
            implLibrary.catBoostModelGetCatFeatures(handle, catFeatureNames, catFlatFeatureIndex, catFeatureIndex);
            implLibrary.catBoostModelGetTextFeatures(handle, textFeatureNames, textFlatFeatureIndex, textFeatureIndex);
            implLibrary.catBoostModelGetEmbeddingFeatures(handle, embeddingFeatureNames, embeddingFlatFeatureIndex, embeddingFeatureIndex);
            implLibrary.catBoostModelGetUsedFeatureIndices(handle, usedFeatureIndicesArr);
        }
        catch (CatBoostError e) {
            this.close();
            throw e;
        }
        HashSet<Integer> usedFeatureIndices = new HashSet<Integer>();
        for (i = 0; i < usedFeatureIndicesArr[0].length; ++i) {
            usedFeatureIndices.add(usedFeatureIndicesArr[0][i]);
        }
        this.predictionDimension = predictionDimension[0];
        this.treeCount = treeCount[0];
        this.usedNumericFeatureCount = usedNumericFeatureCount[0];
        this.usedCategoricFeatureCount = usedCatFeatureCount[0];
        this.usedTextFeatureCount = usedTextFeatureCount[0];
        this.usedEmbeddingFeatureCount = usedEmbeddingFeatureCount[0];
        for (i = 0; i < modelMetadataKeys[0].length; ++i) {
            this.metadata.put(modelMetadataKeys[0][i], modelMetadataValues[0][i]);
        }
        for (i = 0; i < floatFeatureNames[0].length; ++i) {
            this.features.add(new FloatFeature(floatFeatureNames[0][i], floatFeatureIndex[0][i], floatFlatFeatureIndex[0][i], usedFeatureIndices.contains(floatFlatFeatureIndex[0][i]), floatHasNans[0][i], floatNanValueTreatment[0][i]));
        }
        for (i = 0; i < catFeatureNames[0].length; ++i) {
            this.features.add(new CatFeature(catFeatureNames[0][i], catFeatureIndex[0][i], catFlatFeatureIndex[0][i], usedFeatureIndices.contains(catFlatFeatureIndex[0][i])));
        }
        for (i = 0; i < textFeatureNames[0].length; ++i) {
            this.features.add(new TextFeature(textFeatureNames[0][i], textFeatureIndex[0][i], textFlatFeatureIndex[0][i], usedFeatureIndices.contains(textFlatFeatureIndex[0][i])));
        }
        for (i = 0; i < embeddingFeatureNames[0].length; ++i) {
            this.features.add(new EmbeddingFeature(embeddingFeatureNames[0][i], embeddingFeatureIndex[0][i], embeddingFlatFeatureIndex[0][i], usedFeatureIndices.contains(embeddingFlatFeatureIndex[0][i])));
        }
        Collections.sort(this.features, new Comparator<Feature>(){

            @Override
            public int compare(Feature v1, Feature v2) {
                return v1.getFlatFeatureIndex() - v2.getFlatFeatureIndex();
            }
        });
        this.featureNames = new String[this.features.size()];
        for (Feature f : this.features) {
            this.featureNames[f.getFlatFeatureIndex()] = f.getName();
        }
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull String modelPath) throws CatBoostError {
        return CatBoostModel.loadModel(modelPath, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull String modelPath, @NotNull String modelFormat) throws CatBoostError {
        long[] handles = new long[1];
        implLibrary.catBoostLoadModelFromFile(modelPath, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull byte[] serializedModel) throws CatBoostError {
        return CatBoostModel.loadModel(serializedModel, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull byte[] serializedModel, @NotNull String modelFormat) throws CatBoostError {
        long[] handles = new long[1];
        implLibrary.catBoostLoadModelFromArray(serializedModel, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }

    @NotNull
    public static CatBoostModel loadModel(InputStream in) throws CatBoostError, IOException {
        return CatBoostModel.loadModel(in, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(InputStream in, @NotNull String modelFormat) throws CatBoostError, IOException {
        int bytesRead;
        byte[] copyBuffer = new byte[4096];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((bytesRead = in.read(copyBuffer)) != -1) {
            out.write(copyBuffer, 0, bytesRead);
        }
        return CatBoostModel.loadModel(out.toByteArray(), modelFormat);
    }

    public static int hashCategoricalFeature(@NotNull String catFeature) throws CatBoostError {
        int[] hash = new int[1];
        implLibrary.catBoostHashCatFeature(catFeature, hash);
        return hash[0];
    }

    public static void hashCategoricalFeatures(@NotNull String[] catFeatures, @NotNull int[] hashes) throws CatBoostError {
        implLibrary.catBoostHashCatFeatures(catFeatures, hashes);
    }

    @NotNull
    public static int[] hashCategoricalFeatures(@NotNull String[] catFeatures) throws CatBoostError {
        int[] hashes = new int[catFeatures.length];
        CatBoostModel.hashCategoricalFeatures(catFeatures, hashes);
        return hashes;
    }

    public FormulaEvaluatorType[] getSupportedEvaluatorTypes() throws CatBoostError {
        String[][] evaluatorTypesAsStrings = new String[1][];
        implLibrary.catBoostModelGetSupportedEvaluatorTypes(this.handle, evaluatorTypesAsStrings);
        int evaluatorTypesSize = evaluatorTypesAsStrings[0].length;
        FormulaEvaluatorType[] evaluatorTypes = new FormulaEvaluatorType[evaluatorTypesSize];
        for (int i = 0; i < evaluatorTypesSize; ++i) {
            evaluatorTypes[i] = FormulaEvaluatorType.valueOf(evaluatorTypesAsStrings[0][i]);
        }
        return evaluatorTypes;
    }

    public void setEvaluatorType(FormulaEvaluatorType evaluatorType) throws CatBoostError {
        implLibrary.catBoostModelSetEvaluatorType(this.handle, evaluatorType.toString());
    }

    public FormulaEvaluatorType getEvaluatorType() throws CatBoostError, IllegalArgumentException {
        String[] evaluatorTypeAsString = new String[1];
        implLibrary.catBoostModelGetEvaluatorType(this.handle, evaluatorTypeAsString);
        return FormulaEvaluatorType.valueOf(evaluatorTypeAsString[0]);
    }

    public int getPredictionDimension() {
        return this.predictionDimension;
    }

    public int getTreeCount() {
        return this.treeCount;
    }

    public int getUsedNumericFeatureCount() {
        return this.usedNumericFeatureCount;
    }

    public int getUsedCategoricFeatureCount() {
        return this.usedCategoricFeatureCount;
    }

    public int getUsedTextFeatureCount() {
        return this.usedTextFeatureCount;
    }

    public int getUsedEmbeddingFeatureCount() {
        return this.usedEmbeddingFeatureCount;
    }

    public String[] getFeatureNames() {
        return this.featureNames;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public List<Feature> getFeatures() {
        return this.features;
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, null, (float[][])null, prediction.getRawData());
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, null, (float[][])null, prediction.getRawData());
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, (String[][])null, (float[][][])null, prediction.getRawData());
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures) throws CatBoostError {
        return this.predict(numericFeatures, catFeatures, (String[][])null, (float[][][])null);
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize = 0;
        if (numericFeatures != null) {
            resultSize = numericFeatures.length;
        } else if (catFeatures != null) {
            resultSize = catFeatures.length;
        } else if (textFeatures != null) {
            resultSize = textFeatures.length;
        } else if (embeddingFeatures != null) {
            resultSize = embeddingFeatures.length;
        } else {
            throw new CatBoostError("all arguments are null");
        }
        CatBoostPredictions prediction = new CatBoostPredictions(resultSize, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, (String[][])null, (float[][][])null, prediction.getRawData());
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes) throws CatBoostError {
        if (numericFeatures == null && catFeatureHashes == null) {
            throw new CatBoostError("both arguments are null");
        }
        CatBoostPredictions prediction = new CatBoostPredictions(numericFeatures == null ? catFeatureHashes.length : numericFeatures.length, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize = 0;
        if (numericFeatures != null) {
            resultSize = numericFeatures.length;
        } else if (catFeatureHashes != null) {
            resultSize = catFeatureHashes.length;
        } else if (textFeatures != null) {
            resultSize = textFeatures.length;
        } else if (embeddingFeatures != null) {
            resultSize = embeddingFeatures.length;
        } else {
            throw new CatBoostError("all arguments are null");
        }
        CatBoostPredictions prediction = new CatBoostPredictions(resultSize, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    protected void finalize() throws Throwable {
        try {
            this.dispose();
        }
        finally {
            super.finalize();
        }
    }

    private synchronized void dispose() throws CatBoostError {
        if (this.handle != 0L) {
            implLibrary.catBoostFreeModel(this.handle);
            this.handle = 0L;
        }
    }

    @Override
    public void close() throws CatBoostError {
        this.dispose();
    }

    static {
        try {
            NativeLib.smartLoad("catboost4j-prediction");
        }
        catch (Exception ex) {
            throw new RuntimeException("Failed to load catboost4j-prediction native library", ex);
        }
        implLibrary = new CatBoostJNI();
    }

    public static final class EmbeddingFeature
    extends Feature {
        protected EmbeddingFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    public static final class CatFeature
    extends Feature {
        protected CatFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    public static final class FloatFeature
    extends Feature {
        private NanValueTreatment nanValueTreatment;
        private boolean hasNans;

        protected FloatFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel, int hasNans, String nanValueTreatment) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
            this.hasNans = hasNans > 0;
            this.nanValueTreatment = NanValueTreatment.valueOf(nanValueTreatment);
        }

        public boolean hasNans() {
            return this.hasNans;
        }

        public NanValueTreatment getNanValueTreatment() {
            return this.nanValueTreatment;
        }

        public static enum NanValueTreatment {
            AsIs,
            AsTrue,
            AsFalse;

        }
    }

    public static final class TextFeature
    extends Feature {
        protected TextFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    public static abstract class Feature {
        private String name;
        private int featureIndex;
        private int flatFeatureIndex;
        private boolean usedInModel;

        protected Feature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            this.name = name;
            this.featureIndex = featureIndex;
            this.flatFeatureIndex = flatFeatureIndex;
            this.usedInModel = usedInModel;
        }

        public String getName() {
            return this.name;
        }

        public int getFeatureIndex() {
            return this.featureIndex;
        }

        public int getFlatFeatureIndex() {
            return this.flatFeatureIndex;
        }

        public boolean isUsedInModel() {
            return this.usedInModel;
        }
    }

    public static enum FormulaEvaluatorType {
        CPU,
        GPU;

    }
}

