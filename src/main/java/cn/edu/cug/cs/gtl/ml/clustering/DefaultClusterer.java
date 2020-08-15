package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.linear.Vec;

/**
 * 聚类的时候，测试数据集和训练数据集为同一个数据集
 * @param <S>
 * @param <L>
 */
public abstract class DefaultClusterer<S extends NumericalData,L> extends jsat.clustering.ClustererBase implements Clusterer<S,L>{
    protected DataSet<S> trainSet = null;//没有训练集，直接指向测试数据集
    protected DataSet<S> testSet = null;
    protected DistanceMetric<S> distanceMetrics = null;


    public DefaultClusterer(DataSet<S> dataSet, DistanceMetric<S> distanceMetrics) {
        this.trainSet = dataSet;
        this.testSet = dataSet;
        this.distanceMetrics = distanceMetrics;
    }

    protected DefaultClusterer() {

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
    public void fit(DataSet<S> dataSet) {
        this.testSet = dataSet;
        this.trainSet = dataSet;
    }

    @Override
    public abstract Iterable<L> predict(Iterable<Sample<S>> testSamples);

    @Override
    public double score(DataSet<S> testSet,int j, Iterable<L> predictedLabels) {
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
        this.testSet = dataSet;
    }

    @Override
    public void setTestSet(DataSet<S> dataSet) {
        this.trainSet = dataSet;
        this.testSet = dataSet;
    }
}
