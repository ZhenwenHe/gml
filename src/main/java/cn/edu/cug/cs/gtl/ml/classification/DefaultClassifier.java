package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.*;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;

public abstract class DefaultClassifier<KernelType extends NumericalData, LabelType extends Label> implements Classifier<KernelType, LabelType> {
    protected DataSet<KernelType> trainSet = null;
    protected DataSet<KernelType> testSet = null;
    protected DistanceMetric<KernelType> distanceMetrics = null;

    protected Class<KernelType> kernelClass= (Class<KernelType>) Vector.class;
    protected Class<LabelType>  labelClass=(Class<LabelType>) Label.class;

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

    protected DefaultClassifier() {
    }

    public DefaultClassifier(DataSet<KernelType> trainSet, DataSet<KernelType> testSet, DistanceMetric<KernelType> distanceMetrics) {
        this.trainSet = trainSet;
        this.testSet = testSet;
        this.distanceMetrics = distanceMetrics;
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
    public abstract void fit(DataSet<KernelType> trainSet) ;

    @Override
    public abstract LabelType predict(Sample<KernelType> testSample);

    @Override
    public double score(DataSet<KernelType> testSet, int j, Iterable<LabelType> predictedLabels) {
        this.testSet = testSet;
        double probs = 0.0;
        int count = 0;
        int i = 0;
        for (LabelType p : predictedLabels) {
            if (this.testSet.getSample(i).getCategoricalLabel(j).equals(p.getName()))
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
    }

    @Override
    public void setTestSet(DataSet<KernelType> dataSet) {
        this.testSet = dataSet;
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
}
