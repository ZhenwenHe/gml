package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;

/**
 * 分类器接口，具有有限个可能的离散值作为结果的有监督（或半监督）预测器。
 * 对于特定的输入样本，分类器总能给出有限离散值中的一个作为结果。
 * @param <S> 待分类的数据类型，默认为时序数据
 * @param <L> 分类标签数据类型，默认为字符串
 */
public interface Classifier<S extends NumericalData, L> {
    /**
     * 设置距离度量
     * @param distanceMetrics
     */
    void setDistanceMetrics(DistanceMetric<S> distanceMetrics);

    /**
     * 获取距离度量
     * @return
     */
    DistanceMetric<S> getDistanceMetrics();

    /**
     * 用来根据给定的数据对模型进行训练和拟合，得到训练预测模型
     * @param trainSet
     */
    void fit(DataSet<S> trainSet);

    /**
     * 用测试数据进行预测
     * @param testSamples
     * @return
     */
    Iterable<L> predict(Iterable<Sample<S>> testSamples);


    /**
     * 预测得分
     * @param testSet
     * @param i  如果数据集中有多个分类数据列，取其第i列的标签进行比较，默认为0列
     * @param predictedLabels
     * @return
     */
    double score(DataSet<S> testSet, int i, Iterable<L> predictedLabels);
    default double score(DataSet<S> testSet, Iterable<L> predictedLabels){
        return score(testSet,0,predictedLabels);
    }


    /**
     * 获取训练数据集
     * @return
     */
    DataSet<S> getTrainSet();

    /**
     * 获取测试数据集
     * @return
     */
    DataSet<S> getTestSet();

    /**
     * 设置训练数据集
     * @param dataSet
     */
    void setTrainSet(DataSet<S> dataSet);
    /**
     * 设置测试数据集
     * @param dataSet
     */
    void setTestSet(DataSet<S> dataSet);
    /**
     * 根据设置的测试数据集和训练数据集，进行得分计算
     * @param
     */
    default double score() {
        assert getTestSet() != null;
        assert getTrainSet() != null;
        assert getDistanceMetrics() != null;
        fit(getTrainSet());
        Iterable<L> labels = predict((Iterable<Sample<S>>) getTestSet().getSamples());
        return score(getTestSet(), labels);
    }
}
