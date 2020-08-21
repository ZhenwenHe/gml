package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.*;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.classifiers.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚类的时候，测试数据集和训练数据集为同一个数据集
 * @param <KernelType>
 * @param <LabelType>
 */
public abstract class DefaultClusterer<KernelType extends NumericalData, LabelType extends Label>  implements Clusterer<KernelType, LabelType>{
    protected DataSet<KernelType> trainSet = null;//没有训练集，直接指向测试数据集
    protected DataSet<KernelType> testSet = null;
    protected DistanceMetric<KernelType> distanceMetrics = null;
    protected Class<KernelType> kernelClass= (Class<KernelType>) Vector.class;
    protected Class<LabelType>  labelClass=(Class<LabelType>) Label.class;

    public DefaultClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        this.trainSet = dataSet;
        this.testSet = dataSet;
        this.distanceMetrics = distanceMetrics;
    }

    protected DefaultClusterer() {

    }

    @Override
    public void setDistanceMetric(DistanceMetric<KernelType> distanceMetrics) {
        this.distanceMetrics = distanceMetrics;
    }

    @Override
    public DistanceMetric<KernelType> getDistanceMetric() {
        return this.distanceMetrics;
    }

    @Override
    public void fit(DataSet<KernelType> dataSet) {
        this.testSet = dataSet;
        this.trainSet = dataSet;
    }

    @Override
    public abstract LabelType predict(Sample<KernelType> testSample);

    @Override
    public double score(DataSet<KernelType> testSet, int j, Iterable<LabelType> predictedLabels) {
        this.testSet = testSet;
        double probs = 0.0;
        int count = 0;
        int i = 0;
        for (LabelType p : predictedLabels) {
            if (this.testSet.getSample(i).getCategoricalLabel(j).equals(p))
                count++;
            ++i;
        }
        probs = count * 1.0 / i;
        return probs;
    }

    @Override
    public DataSet<KernelType> getTrainSet() {
        return this.trainSet;
    }

    @Override
    public DataSet<KernelType> getTestSet() {
        return this.testSet;
    }

    @Override
    public void setTrainSet(DataSet<KernelType> dataSet) {
        this.trainSet = dataSet;
        this.testSet = dataSet;
    }

    @Override
    public void setTestSet(DataSet<KernelType> dataSet) {
        this.trainSet = dataSet;
        this.testSet = dataSet;
    }



    @Override
    public void setKernelClass(Class<KernelType> cls){
        kernelClass=cls;
    }

    @Override
    public void setLabelClass(Class<LabelType> cls){
        labelClass=cls;
    }

    @Override
    public Class<KernelType> getKernelClass( ){
        return kernelClass;
    }

    @Override
    public Class<LabelType> getLabelClass( ){
        return labelClass;
    }

    protected LabelType newLabel(String val){
        try{
            return  getLabelClass().getConstructor(String.class).newInstance(val);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    protected LabelType newLabel(int i){
        try{
            return  getLabelClass().getConstructor(int.class).newInstance(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
