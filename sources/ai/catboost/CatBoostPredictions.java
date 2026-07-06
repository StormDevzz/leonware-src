package ai.catboost;

import javax.validation.constraints.NotNull;

/* JADX INFO: loaded from: leonware-0.0.3.jar:ai/catboost/CatBoostPredictions.class */
public class CatBoostPredictions {

    @NotNull
    private final double[] data;
    private final int objectCount;
    private final int predictionDimension;

    CatBoostPredictions(int objectCount, int predictionDimension, @NotNull double[] data) {
        if (data.length != objectCount * predictionDimension) {
            String message = "data size is incorrect, must be objectCount * predictionDimension = " + String.valueOf(objectCount * predictionDimension) + "(objectCount=" + String.valueOf(objectCount) + ",  predictionDimension=" + String.valueOf(predictionDimension) + ") but got " + String.valueOf(data.length);
            throw new IllegalArgumentException(message);
        }
        this.objectCount = objectCount;
        this.predictionDimension = predictionDimension;
        this.data = data;
    }

    public CatBoostPredictions(int objectCount, int predictionDimension) {
        this.objectCount = objectCount;
        this.predictionDimension = predictionDimension;
        this.data = new double[objectCount * predictionDimension];
    }

    public int getObjectCount() {
        return this.objectCount;
    }

    public int getPredictionDimension() {
        return this.predictionDimension;
    }

    public double get(int objectIndex, int predictionIndex) {
        return this.data[(objectIndex * getPredictionDimension()) + predictionIndex];
    }

    public void copyObjectPredictions(int objectIndex, @NotNull double[] predictions) {
        if (predictions.length < getPredictionDimension()) {
            throw new IllegalArgumentException("`predictions` size is insufficient, got " + String.valueOf(predictions.length) + "but must be at least " + String.valueOf(getPredictionDimension()));
        }
        System.arraycopy(this.data, objectIndex * getPredictionDimension(), predictions, 0, getPredictionDimension());
    }

    @NotNull
    public double[] copyObjectPredictions(int objectIndex) {
        double[] predictions = new double[getPredictionDimension()];
        copyObjectPredictions(objectIndex, predictions);
        return predictions;
    }

    @NotNull
    public double[] copyRowMajorPredictions() {
        return this.data;
    }

    @NotNull
    double[] getRawData() {
        return this.data;
    }
}
