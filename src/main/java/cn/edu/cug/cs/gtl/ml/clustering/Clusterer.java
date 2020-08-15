package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.linear.Vec;

/**
 * 聚类器接口，属于无监督学习算法，具有有限个离散的输出结果。
 * 测试数据集和训练数据集为同一个数据集。
 * 聚类器必须提供的方法有fit()。
 * 另外，直推式聚类器应具有fit_predict()方法，归纳式聚类器应具有predict()方法。
 * @param <S> 待聚类的数据类型，默认为时序数据
 * @param <L> 聚类标签数据类型，默认为字符串
 */
public interface Clusterer<S extends NumericalData, L> extends jsat.clustering.Clusterer{
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
     * 用来根据给定的数据集合进行训练和拟合，得到训练预测模型
     * @param dataSet
     */
    void fit(DataSet<S> dataSet);

    /**
     * 用数据集进行预测
     * @param dataSet
     * @return
     */
    Iterable<L> predict(Iterable<Sample<S>> dataSet);

    /**
     * 预测得分
     * @param dataSet
     * @param i  如果数据集中有多个分类数据列，取其第i列的标签进行比较，默认为0列
     * @param predictedLabels
     * @return
     */
    double score(DataSet<S> dataSet, int i, Iterable<L> predictedLabels);
    default double score(DataSet<S> dataSet, Iterable<L> predictedLabels){
        return score(dataSet,0,predictedLabels);
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
        Iterable<L> labels = predict(getTestSet().getSamples());
        return score(getTestSet(), labels);
    }
}