package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import jsat.linear.Vec;

public abstract class DefaultClassifier<S extends NumericalData, L> implements Classifier<S, L> {
    protected DataSet<S> trainSet = null;
    protected DataSet<S> testSet = null;
    protected DistanceMetric<S> distanceMetrics = null;

    protected DefaultClassifier() {
    }

    public DefaultClassifier(DataSet<S> trainSet, DataSet<S> testSet, DistanceMetric<S> distanceMetrics) {
        this.trainSet = trainSet;
        this.testSet = testSet;
        this.distanceMetrics = distanceMetrics;
    }

    @Override
    public void setDistanceMetrics(DistanceMetric<S> distanceMetrics) {
        this.distanceMetrics = distanceMetrics;
    }

    @Override
    public DistanceMetric<S> getDistanceMetrics() {
        return this.distanceMetrics;
    }

    @Override
    public void fit(DataSet<S> trainSet) {
        this.trainSet = trainSet;
    }

    @Override
    public abstract Iterable<L> predict(Iterable<Sample<S>> testSamples);

    @Override
    public double score(DataSet<S> testSet, int j, Iterable<L> predictedLabels) {
        this.testSet = testSet;
        double probs = 0.0;
        int count = 0;
        int i = 0;
        for (L p : predictedLabels) {
            if (this.testSet.getSample(i).getCategoricalLabel(j).equals(p))
                count++;
            ++i;
        }
        probs = count * 1.0 / i;
        return probs;
    }

    @Override
    public DataSet<S> getTrainSet() {
        return this.trainSet;
    }

    @Override
    public DataSet<S> getTestSet() {
        return this.testSet;
    }

    @Override
    public void setTrainSet(DataSet<S> dataSet) {
        this.trainSet = dataSet;
    }

    @Override
    public void setTestSet(DataSet<S> dataSet) {
        this.testSet = dataSet;
    }
}
