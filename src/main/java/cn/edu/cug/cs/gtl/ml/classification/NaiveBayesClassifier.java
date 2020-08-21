package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.classifiers.CategoricalResults;
import jsat.classifiers.bayesian.NaiveBayes;

public class NaiveBayesClassifier <KernelType extends NumericalData, LabelType extends Label> extends DefaultClassifier<KernelType, LabelType>{
    NaiveBayes naiveBayes;

    public NaiveBayesClassifier() {
        naiveBayes=new NaiveBayes();
    }

    public NaiveBayesClassifier(DataSet<KernelType> trainSet, DataSet<KernelType> testSet, DistanceMetric<KernelType> distanceMetrics) {
        super(trainSet, testSet, distanceMetrics);
        naiveBayes=new NaiveBayes();
    }

    /**
     * 用来根据给定的数据对模型进行训练和拟合，得到训练预测模型
     * @param trainSet
     */
    @Override
    public void fit(DataSet<KernelType> trainSet){
        this.trainSet=trainSet;
        naiveBayes.train(trainSet.asClassificationDataSet(0));
    }

    @Override
    public LabelType predict(Sample<KernelType> testSample) {
        CategoricalResults cr = naiveBayes.classify(testSample);
        return newLabel(trainSet.getCategories()[0].getOptionName(cr.mostLikely()));
    }
}
