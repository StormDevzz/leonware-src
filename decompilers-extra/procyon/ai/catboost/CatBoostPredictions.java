// 
// Decompiled by Procyon v0.6.0
// 

package ai.catboost;

import javax.validation.constraints.NotNull;

public class CatBoostPredictions
{
    @NotNull
    private final double[] data;
    private final int objectCount;
    private final int predictionDimension;
    
    CatBoostPredictions(final int objectCount, final int predictionDimension, @NotNull final double[] data) {
        if (data.length != objectCount * predictionDimension) {
            final String message = "data size is incorrect, must be objectCount * predictionDimension = " + String.valueOf(objectCount * predictionDimension) + "(objectCount=" + String.valueOf(objectCount) + ",  predictionDimension=" + String.valueOf(predictionDimension) + ") but got " + String.valueOf(data.length);
            throw new IllegalArgumentException(message);
        }
        this.objectCount = objectCount;
        this.predictionDimension = predictionDimension;
        this.data = data;
    }
    
    public CatBoostPredictions(final int objectCount, final int predictionDimension) {
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
    
    public double get(final int objectIndex, final int predictionIndex) {
        return this.data[objectIndex * this.getPredictionDimension() + predictionIndex];
    }
    
    public void copyObjectPredictions(final int objectIndex, @NotNull final double[] predictions) {
        if (predictions.length < this.getPredictionDimension()) {
            throw new IllegalArgumentException("`predictions` size is insufficient, got " + String.valueOf(predictions.length) + "but must be at least " + String.valueOf(this.getPredictionDimension()));
        }
        System.arraycopy(this.data, objectIndex * this.getPredictionDimension(), predictions, 0, this.getPredictionDimension());
    }
    
    @NotNull
    public double[] copyObjectPredictions(final int objectIndex) {
        final double[] predictions = new double[this.getPredictionDimension()];
        this.copyObjectPredictions(objectIndex, predictions);
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
