// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost;

import ai.catboost.common.NativeLib;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatBoostModel implements AutoCloseable
{
    private long handle;
    private int predictionDimension;
    private int treeCount;
    private int usedNumericFeatureCount;
    private int usedCategoricFeatureCount;
    private int usedTextFeatureCount;
    private int usedEmbeddingFeatureCount;
    private String[] featureNames;
    private Map<String, String> metadata;
    private List<Feature> features;
    private static CatBoostJNI implLibrary;
    
    private CatBoostModel(final long handle) throws CatBoostError {
        this.handle = 0L;
        this.predictionDimension = 0;
        this.treeCount = 0;
        this.usedNumericFeatureCount = 0;
        this.usedCategoricFeatureCount = 0;
        this.usedTextFeatureCount = 0;
        this.usedEmbeddingFeatureCount = 0;
        this.metadata = new HashMap<String, String>();
        this.features = new ArrayList<Feature>();
        this.handle = handle;
        final int[] predictionDimension = { 0 };
        final int[] treeCount = { 0 };
        final int[] usedNumericFeatureCount = { 0 };
        final int[] usedCatFeatureCount = { 0 };
        final int[] usedTextFeatureCount = { 0 };
        final int[] usedEmbeddingFeatureCount = { 0 };
        final int[] featureVectorExpectedSize = { 0 };
        final String[][] modelMetadataKeys = { null };
        final String[][] modelMetadataValues = { null };
        final String[][] floatFeatureNames = { null };
        final int[][] floatFlatFeatureIndex = { null };
        final int[][] floatFeatureIndex = { null };
        final int[][] floatHasNans = { null };
        final String[][] floatNanValueTreatment = { null };
        final String[][] catFeatureNames = { null };
        final int[][] catFlatFeatureIndex = { null };
        final int[][] catFeatureIndex = { null };
        final String[][] textFeatureNames = { null };
        final int[][] textFlatFeatureIndex = { null };
        final int[][] textFeatureIndex = { null };
        final String[][] embeddingFeatureNames = { null };
        final int[][] embeddingFlatFeatureIndex = { null };
        final int[][] embeddingFeatureIndex = { null };
        final int[][] usedFeatureIndicesArr = { null };
        try {
            CatBoostModel.implLibrary.catBoostModelGetPredictionDimension(handle, predictionDimension);
            CatBoostModel.implLibrary.catBoostModelGetTreeCount(handle, treeCount);
            CatBoostModel.implLibrary.catBoostModelGetUsedNumericFeatureCount(handle, usedNumericFeatureCount);
            CatBoostModel.implLibrary.catBoostModelGetUsedCategoricalFeatureCount(handle, usedCatFeatureCount);
            CatBoostModel.implLibrary.catBoostModelGetUsedTextFeatureCount(handle, usedTextFeatureCount);
            CatBoostModel.implLibrary.catBoostModelGetUsedEmbeddingFeatureCount(handle, usedEmbeddingFeatureCount);
            CatBoostModel.implLibrary.catBoostModelGetFlatFeatureVectorExpectedSize(handle, featureVectorExpectedSize);
            CatBoostModel.implLibrary.catBoostModelGetMetadata(handle, modelMetadataKeys, modelMetadataValues);
            CatBoostModel.implLibrary.catBoostModelGetFloatFeatures(handle, floatFeatureNames, floatFlatFeatureIndex, floatFeatureIndex, floatHasNans, floatNanValueTreatment);
            CatBoostModel.implLibrary.catBoostModelGetCatFeatures(handle, catFeatureNames, catFlatFeatureIndex, catFeatureIndex);
            CatBoostModel.implLibrary.catBoostModelGetTextFeatures(handle, textFeatureNames, textFlatFeatureIndex, textFeatureIndex);
            CatBoostModel.implLibrary.catBoostModelGetEmbeddingFeatures(handle, embeddingFeatureNames, embeddingFlatFeatureIndex, embeddingFeatureIndex);
            CatBoostModel.implLibrary.catBoostModelGetUsedFeatureIndices(handle, usedFeatureIndicesArr);
        }
        catch (final CatBoostError e) {
            this.close();
            throw e;
        }
        final HashSet<Integer> usedFeatureIndices = new HashSet<Integer>();
        for (int i = 0; i < usedFeatureIndicesArr[0].length; ++i) {
            usedFeatureIndices.add(usedFeatureIndicesArr[0][i]);
        }
        this.predictionDimension = predictionDimension[0];
        this.treeCount = treeCount[0];
        this.usedNumericFeatureCount = usedNumericFeatureCount[0];
        this.usedCategoricFeatureCount = usedCatFeatureCount[0];
        this.usedTextFeatureCount = usedTextFeatureCount[0];
        this.usedEmbeddingFeatureCount = usedEmbeddingFeatureCount[0];
        for (int i = 0; i < modelMetadataKeys[0].length; ++i) {
            this.metadata.put(modelMetadataKeys[0][i], modelMetadataValues[0][i]);
        }
        for (int i = 0; i < floatFeatureNames[0].length; ++i) {
            this.features.add(new FloatFeature(floatFeatureNames[0][i], floatFeatureIndex[0][i], floatFlatFeatureIndex[0][i], usedFeatureIndices.contains(floatFlatFeatureIndex[0][i]), floatHasNans[0][i], floatNanValueTreatment[0][i]));
        }
        for (int i = 0; i < catFeatureNames[0].length; ++i) {
            this.features.add(new CatFeature(catFeatureNames[0][i], catFeatureIndex[0][i], catFlatFeatureIndex[0][i], usedFeatureIndices.contains(catFlatFeatureIndex[0][i])));
        }
        for (int i = 0; i < textFeatureNames[0].length; ++i) {
            this.features.add(new TextFeature(textFeatureNames[0][i], textFeatureIndex[0][i], textFlatFeatureIndex[0][i], usedFeatureIndices.contains(textFlatFeatureIndex[0][i])));
        }
        for (int i = 0; i < embeddingFeatureNames[0].length; ++i) {
            this.features.add(new EmbeddingFeature(embeddingFeatureNames[0][i], embeddingFeatureIndex[0][i], embeddingFlatFeatureIndex[0][i], usedFeatureIndices.contains(embeddingFlatFeatureIndex[0][i])));
        }
        Collections.sort(this.features, new Comparator<Feature>() {
            @Override
            public int compare(final Feature v1, final Feature v2) {
                return v1.getFlatFeatureIndex() - v2.getFlatFeatureIndex();
            }
        });
        this.featureNames = new String[this.features.size()];
        for (final Feature f : this.features) {
            this.featureNames[f.getFlatFeatureIndex()] = f.getName();
        }
    }
    
    @NotNull
    public static CatBoostModel loadModel(@NotNull final String modelPath) throws CatBoostError {
        return loadModel(modelPath, "bin");
    }
    
    @NotNull
    public static CatBoostModel loadModel(@NotNull final String modelPath, @NotNull final String modelFormat) throws CatBoostError {
        final long[] handles = { 0L };
        CatBoostModel.implLibrary.catBoostLoadModelFromFile(modelPath, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }
    
    @NotNull
    public static CatBoostModel loadModel(@NotNull final byte[] serializedModel) throws CatBoostError {
        return loadModel(serializedModel, "bin");
    }
    
    @NotNull
    public static CatBoostModel loadModel(@NotNull final byte[] serializedModel, @NotNull final String modelFormat) throws CatBoostError {
        final long[] handles = { 0L };
        CatBoostModel.implLibrary.catBoostLoadModelFromArray(serializedModel, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }
    
    @NotNull
    public static CatBoostModel loadModel(final InputStream in) throws CatBoostError, IOException {
        return loadModel(in, "bin");
    }
    
    @NotNull
    public static CatBoostModel loadModel(final InputStream in, @NotNull final String modelFormat) throws CatBoostError, IOException {
        final byte[] copyBuffer = new byte[4096];
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = in.read(copyBuffer)) != -1) {
            out.write(copyBuffer, 0, bytesRead);
        }
        return loadModel(out.toByteArray(), modelFormat);
    }
    
    public static int hashCategoricalFeature(@NotNull final String catFeature) throws CatBoostError {
        final int[] hash = { 0 };
        CatBoostModel.implLibrary.catBoostHashCatFeature(catFeature, hash);
        return hash[0];
    }
    
    public static void hashCategoricalFeatures(@NotNull final String[] catFeatures, @NotNull final int[] hashes) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostHashCatFeatures(catFeatures, hashes);
    }
    
    @NotNull
    public static int[] hashCategoricalFeatures(@NotNull final String[] catFeatures) throws CatBoostError {
        final int[] hashes = new int[catFeatures.length];
        hashCategoricalFeatures(catFeatures, hashes);
        return hashes;
    }
    
    public FormulaEvaluatorType[] getSupportedEvaluatorTypes() throws CatBoostError {
        final String[][] evaluatorTypesAsStrings = { null };
        CatBoostModel.implLibrary.catBoostModelGetSupportedEvaluatorTypes(this.handle, evaluatorTypesAsStrings);
        final int evaluatorTypesSize = evaluatorTypesAsStrings[0].length;
        final FormulaEvaluatorType[] evaluatorTypes = new FormulaEvaluatorType[evaluatorTypesSize];
        for (int i = 0; i < evaluatorTypesSize; ++i) {
            evaluatorTypes[i] = FormulaEvaluatorType.valueOf(evaluatorTypesAsStrings[0][i]);
        }
        return evaluatorTypes;
    }
    
    public void setEvaluatorType(final FormulaEvaluatorType evaluatorType) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelSetEvaluatorType(this.handle, evaluatorType.toString());
    }
    
    public FormulaEvaluatorType getEvaluatorType() throws CatBoostError, IllegalArgumentException {
        final String[] evaluatorTypeAsString = { null };
        CatBoostModel.implLibrary.catBoostModelGetEvaluatorType(this.handle, evaluatorTypeAsString);
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
    
    public void predict(@Nullable final float[] numericFeatures, @Nullable final String[] catFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, null, null, prediction.getRawData());
    }
    
    public void predict(@Nullable final float[] numericFeatures, @Nullable final String[] catFeatures, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[] numericFeatures, @Nullable final String[] catFeatures) throws CatBoostError {
        final CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, prediction);
        return prediction;
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[] numericFeatures, @Nullable final String[] catFeatures, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures) throws CatBoostError {
        final CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }
    
    public void predict(@Nullable final float[] numericFeatures, @Nullable final int[] catFeatureHashes, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, null, null, prediction.getRawData());
    }
    
    public void predict(@Nullable final float[] numericFeatures, @Nullable final int[] catFeatureHashes, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[] numericFeatures, @Nullable final int[] catFeatureHashes) throws CatBoostError {
        final CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[] numericFeatures, @Nullable final int[] catFeatureHashes, @Nullable final String[] textFeatures, @Nullable final float[][] embeddingFeatures) throws CatBoostError {
        final CatBoostPredictions prediction = new CatBoostPredictions(1, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }
    
    public void predict(@Nullable final float[][] numericFeatures, @Nullable final String[][] catFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, null, null, prediction.getRawData());
    }
    
    public void predict(@Nullable final float[][] numericFeatures, @Nullable final String[][] catFeatures, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[][] numericFeatures, @Nullable final String[][] catFeatures) throws CatBoostError {
        return this.predict(numericFeatures, catFeatures, null, null);
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[][] numericFeatures, @Nullable final String[][] catFeatures, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize = 0;
        if (numericFeatures != null) {
            resultSize = numericFeatures.length;
        }
        else if (catFeatures != null) {
            resultSize = catFeatures.length;
        }
        else if (textFeatures != null) {
            resultSize = textFeatures.length;
        }
        else {
            if (embeddingFeatures == null) {
                throw new CatBoostError("all arguments are null");
            }
            resultSize = embeddingFeatures.length;
        }
        final CatBoostPredictions prediction = new CatBoostPredictions(resultSize, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }
    
    public void predict(@Nullable final float[][] numericFeatures, @Nullable final int[][] catFeatureHashes, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, null, null, prediction.getRawData());
    }
    
    public void predict(@Nullable final float[][] numericFeatures, @Nullable final int[][] catFeatureHashes, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures, @NotNull final CatBoostPredictions prediction) throws CatBoostError {
        CatBoostModel.implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[][] numericFeatures, @Nullable final int[][] catFeatureHashes) throws CatBoostError {
        if (numericFeatures == null && catFeatureHashes == null) {
            throw new CatBoostError("both arguments are null");
        }
        final CatBoostPredictions prediction = new CatBoostPredictions((numericFeatures == null) ? catFeatureHashes.length : numericFeatures.length, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }
    
    @NotNull
    public CatBoostPredictions predict(@Nullable final float[][] numericFeatures, @Nullable final int[][] catFeatureHashes, @Nullable final String[][] textFeatures, @Nullable final float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize = 0;
        if (numericFeatures != null) {
            resultSize = numericFeatures.length;
        }
        else if (catFeatureHashes != null) {
            resultSize = catFeatureHashes.length;
        }
        else if (textFeatures != null) {
            resultSize = textFeatures.length;
        }
        else {
            if (embeddingFeatures == null) {
                throw new CatBoostError("all arguments are null");
            }
            resultSize = embeddingFeatures.length;
        }
        final CatBoostPredictions prediction = new CatBoostPredictions(resultSize, this.getPredictionDimension());
        this.predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }
    
    @Override
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
            CatBoostModel.implLibrary.catBoostFreeModel(this.handle);
            this.handle = 0L;
        }
    }
    
    @Override
    public void close() throws CatBoostError {
        this.dispose();
    }
    
    static {
        CatBoostModel.implLibrary = null;
        try {
            NativeLib.smartLoad("catboost4j-prediction");
        }
        catch (final Exception ex) {
            throw new RuntimeException("Failed to load catboost4j-prediction native library", ex);
        }
        CatBoostModel.implLibrary = new CatBoostJNI();
    }
    
    public enum FormulaEvaluatorType
    {
        CPU, 
        GPU;
    }
    
    public abstract static class Feature
    {
        private String name;
        private int featureIndex;
        private int flatFeatureIndex;
        private boolean usedInModel;
        
        protected Feature(final String name, final int featureIndex, final int flatFeatureIndex, final boolean usedInModel) {
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
    
    public static final class TextFeature extends Feature
    {
        protected TextFeature(final String name, final int featureIndex, final int flatFeatureIndex, final boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }
    
    public static final class FloatFeature extends Feature
    {
        private NanValueTreatment nanValueTreatment;
        private boolean hasNans;
        
        protected FloatFeature(final String name, final int featureIndex, final int flatFeatureIndex, final boolean usedInModel, final int hasNans, final String nanValueTreatment) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
            this.hasNans = (hasNans > 0);
            this.nanValueTreatment = NanValueTreatment.valueOf(nanValueTreatment);
        }
        
        public boolean hasNans() {
            return this.hasNans;
        }
        
        public NanValueTreatment getNanValueTreatment() {
            return this.nanValueTreatment;
        }
        
        public enum NanValueTreatment
        {
            AsIs, 
            AsTrue, 
            AsFalse;
        }
    }
    
    public static final class CatFeature extends Feature
    {
        protected CatFeature(final String name, final int featureIndex, final int flatFeatureIndex, final boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }
    
    public static final class EmbeddingFeature extends Feature
    {
        protected EmbeddingFeature(final String name, final int featureIndex, final int flatFeatureIndex, final boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }
}
