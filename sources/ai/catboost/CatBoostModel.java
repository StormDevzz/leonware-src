package ai.catboost;

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

/* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel.class */
public class CatBoostModel implements AutoCloseable {
    private long handle;
    private int predictionDimension;
    private int treeCount;
    private int usedNumericFeatureCount;
    private int usedCategoricFeatureCount;
    private int usedTextFeatureCount;
    private int usedEmbeddingFeatureCount;
    private String[] featureNames;
    private Map<String, String> metadata = new HashMap();
    private List<Feature> features = new ArrayList();
    private static CatBoostJNI implLibrary;

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$FormulaEvaluatorType.class */
    public enum FormulaEvaluatorType {
        CPU,
        GPU
    }

    static {
        implLibrary = null;
        try {
            NativeLib.smartLoad("catboost4j-prediction");
            implLibrary = new CatBoostJNI();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load catboost4j-prediction native library", ex);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$Feature.class */
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

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$TextFeature.class */
    public static final class TextFeature extends Feature {
        protected TextFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$FloatFeature.class */
    public static final class FloatFeature extends Feature {
        private NanValueTreatment nanValueTreatment;
        private boolean hasNans;

        /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$FloatFeature$NanValueTreatment.class */
        public enum NanValueTreatment {
            AsIs,
            AsTrue,
            AsFalse
        }

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
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$CatFeature.class */
    public static final class CatFeature extends Feature {
        protected CatFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    /* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostModel$EmbeddingFeature.class */
    public static final class EmbeddingFeature extends Feature {
        protected EmbeddingFeature(String name, int featureIndex, int flatFeatureIndex, boolean usedInModel) {
            super(name, featureIndex, flatFeatureIndex, usedInModel);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v30, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v32, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v34, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v36, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v38, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v40, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v42, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v44, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v46, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v48, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v50, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v52, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r0v54, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v56, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r0v58, types: [int[], int[][]] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    private CatBoostModel(long handle) throws CatBoostError {
        this.handle = 0L;
        this.predictionDimension = 0;
        this.treeCount = 0;
        this.usedNumericFeatureCount = 0;
        this.usedCategoricFeatureCount = 0;
        this.usedTextFeatureCount = 0;
        this.usedEmbeddingFeatureCount = 0;
        this.handle = handle;
        int[] predictionDimension = new int[1];
        int[] treeCount = new int[1];
        int[] usedNumericFeatureCount = new int[1];
        int[] usedCatFeatureCount = new int[1];
        int[] usedTextFeatureCount = new int[1];
        int[] usedEmbeddingFeatureCount = new int[1];
        int[] featureVectorExpectedSize = new int[1];
        ?? r0 = new String[1];
        ?? r02 = new String[1];
        ?? r03 = new String[1];
        ?? r04 = new int[1];
        ?? r05 = new int[1];
        ?? r06 = new int[1];
        ?? r07 = new String[1];
        ?? r08 = new String[1];
        ?? r09 = new int[1];
        ?? r010 = new int[1];
        ?? r011 = new String[1];
        ?? r012 = new int[1];
        ?? r013 = new int[1];
        ?? r014 = new String[1];
        ?? r015 = new int[1];
        ?? r016 = new int[1];
        ?? r017 = new int[1];
        try {
            implLibrary.catBoostModelGetPredictionDimension(handle, predictionDimension);
            implLibrary.catBoostModelGetTreeCount(handle, treeCount);
            implLibrary.catBoostModelGetUsedNumericFeatureCount(handle, usedNumericFeatureCount);
            implLibrary.catBoostModelGetUsedCategoricalFeatureCount(handle, usedCatFeatureCount);
            implLibrary.catBoostModelGetUsedTextFeatureCount(handle, usedTextFeatureCount);
            implLibrary.catBoostModelGetUsedEmbeddingFeatureCount(handle, usedEmbeddingFeatureCount);
            implLibrary.catBoostModelGetFlatFeatureVectorExpectedSize(handle, featureVectorExpectedSize);
            implLibrary.catBoostModelGetMetadata(handle, r0, r02);
            implLibrary.catBoostModelGetFloatFeatures(handle, r03, r04, r05, r06, r07);
            implLibrary.catBoostModelGetCatFeatures(handle, r08, r09, r010);
            implLibrary.catBoostModelGetTextFeatures(handle, r011, r012, r013);
            implLibrary.catBoostModelGetEmbeddingFeatures(handle, r014, r015, r016);
            implLibrary.catBoostModelGetUsedFeatureIndices(handle, r017);
            HashSet<Integer> usedFeatureIndices = new HashSet<>();
            for (int i = 0; i < r017[0].length; i++) {
                usedFeatureIndices.add(Integer.valueOf(r017[0][i]));
            }
            this.predictionDimension = predictionDimension[0];
            this.treeCount = treeCount[0];
            this.usedNumericFeatureCount = usedNumericFeatureCount[0];
            this.usedCategoricFeatureCount = usedCatFeatureCount[0];
            this.usedTextFeatureCount = usedTextFeatureCount[0];
            this.usedEmbeddingFeatureCount = usedEmbeddingFeatureCount[0];
            for (int i2 = 0; i2 < r0[0].length; i2++) {
                this.metadata.put(r0[0][i2], r02[0][i2]);
            }
            for (int i3 = 0; i3 < r03[0].length; i3++) {
                this.features.add(new FloatFeature(r03[0][i3], r05[0][i3], r04[0][i3], usedFeatureIndices.contains(Integer.valueOf(r04[0][i3])), r06[0][i3], r07[0][i3]));
            }
            for (int i4 = 0; i4 < r08[0].length; i4++) {
                this.features.add(new CatFeature(r08[0][i4], r010[0][i4], r09[0][i4], usedFeatureIndices.contains(Integer.valueOf(r09[0][i4]))));
            }
            for (int i5 = 0; i5 < r011[0].length; i5++) {
                this.features.add(new TextFeature(r011[0][i5], r013[0][i5], r012[0][i5], usedFeatureIndices.contains(Integer.valueOf(r012[0][i5]))));
            }
            for (int i6 = 0; i6 < r014[0].length; i6++) {
                this.features.add(new EmbeddingFeature(r014[0][i6], r016[0][i6], r015[0][i6], usedFeatureIndices.contains(Integer.valueOf(r015[0][i6]))));
            }
            Collections.sort(this.features, new Comparator<Feature>() { // from class: ai.catboost.CatBoostModel.1
                @Override // java.util.Comparator
                public int compare(Feature v1, Feature v2) {
                    return v1.getFlatFeatureIndex() - v2.getFlatFeatureIndex();
                }
            });
            this.featureNames = new String[this.features.size()];
            for (Feature f : this.features) {
                this.featureNames[f.getFlatFeatureIndex()] = f.getName();
            }
        } catch (CatBoostError e) {
            close();
            throw e;
        }
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull String modelPath) throws CatBoostError {
        return loadModel(modelPath, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull String modelPath, @NotNull String modelFormat) throws CatBoostError {
        long[] handles = new long[1];
        implLibrary.catBoostLoadModelFromFile(modelPath, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull byte[] serializedModel) throws CatBoostError {
        return loadModel(serializedModel, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(@NotNull byte[] serializedModel, @NotNull String modelFormat) throws CatBoostError {
        long[] handles = new long[1];
        implLibrary.catBoostLoadModelFromArray(serializedModel, handles, modelFormat);
        return new CatBoostModel(handles[0]);
    }

    @NotNull
    public static CatBoostModel loadModel(InputStream in) throws CatBoostError, IOException {
        return loadModel(in, "bin");
    }

    @NotNull
    public static CatBoostModel loadModel(InputStream in, @NotNull String modelFormat) throws CatBoostError, IOException {
        byte[] copyBuffer = new byte[4096];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int bytesRead = in.read(copyBuffer);
            if (bytesRead != -1) {
                out.write(copyBuffer, 0, bytesRead);
            } else {
                return loadModel(out.toByteArray(), modelFormat);
            }
        }
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
        hashCategoricalFeatures(catFeatures, hashes);
        return hashes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public FormulaEvaluatorType[] getSupportedEvaluatorTypes() throws CatBoostError {
        ?? r0 = new String[1];
        implLibrary.catBoostModelGetSupportedEvaluatorTypes(this.handle, r0);
        int evaluatorTypesSize = r0[0].length;
        FormulaEvaluatorType[] evaluatorTypes = new FormulaEvaluatorType[evaluatorTypesSize];
        for (int i = 0; i < evaluatorTypesSize; i++) {
            evaluatorTypes[i] = FormulaEvaluatorType.valueOf(r0[0][i]);
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
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, (String[]) null, (float[][]) null, prediction.getRawData());
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, getPredictionDimension());
        predict(numericFeatures, catFeatures, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable String[] catFeatures, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, getPredictionDimension());
        predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, (String[]) null, (float[][]) null, prediction.getRawData());
    }

    public void predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, getPredictionDimension());
        predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[] numericFeatures, @Nullable int[] catFeatureHashes, @Nullable String[] textFeatures, @Nullable float[][] embeddingFeatures) throws CatBoostError {
        CatBoostPredictions prediction = new CatBoostPredictions(1, getPredictionDimension());
        predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, (String[][]) null, (float[][][]) null, prediction.getRawData());
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures) throws CatBoostError {
        return predict(numericFeatures, catFeatures, (String[][]) null, (float[][][]) null);
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable String[][] catFeatures, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize;
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
        CatBoostPredictions prediction = new CatBoostPredictions(resultSize, getPredictionDimension());
        predict(numericFeatures, catFeatures, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, (String[][]) null, (float[][][]) null, prediction.getRawData());
    }

    public void predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures, @NotNull CatBoostPredictions prediction) throws CatBoostError {
        implLibrary.catBoostModelPredict(this.handle, numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction.getRawData());
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes) throws CatBoostError {
        if (numericFeatures == null && catFeatureHashes == null) {
            throw new CatBoostError("both arguments are null");
        }
        CatBoostPredictions prediction = new CatBoostPredictions(numericFeatures == null ? catFeatureHashes.length : numericFeatures.length, getPredictionDimension());
        predict(numericFeatures, catFeatureHashes, prediction);
        return prediction;
    }

    @NotNull
    public CatBoostPredictions predict(@Nullable float[][] numericFeatures, @Nullable int[][] catFeatureHashes, @Nullable String[][] textFeatures, @Nullable float[][][] embeddingFeatures) throws CatBoostError {
        int resultSize;
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
        CatBoostPredictions prediction = new CatBoostPredictions(resultSize, getPredictionDimension());
        predict(numericFeatures, catFeatureHashes, textFeatures, embeddingFeatures, prediction);
        return prediction;
    }

    protected void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    private synchronized void dispose() throws CatBoostError {
        if (this.handle != 0) {
            implLibrary.catBoostFreeModel(this.handle);
            this.handle = 0L;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws CatBoostError {
        dispose();
    }
}
