package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.distances.DistanceMetrics;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.TestSet;
import cn.edu.cug.cs.gtl.ml.dataset.TrainSet;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetrics;

import java.util.Collection;

/**
 * 分类器接口
 * @param <S>
 * @param <L>
 */
public interface Classifier<S, L> {
    /**
     * 设置距离度量
     * @param distanceMetrics
     */
    void setDistanceMetrics(DistanceMetrics<S> distanceMetrics);

    /**
     * 获取距离度量
     * @return
     */
    DistanceMetrics<S> getDistanceMetrics();

    /**
     * 用来根据给定的数据对模型进行训练和拟合，得到训练预测模型
     * @param trainSet
     */
    void fit(DataSet<S, L> trainSet);

    /**
     * 用测试数据进行预测
     * @param testSamples
     * @return
     */
    Iterable<L> predict(Iterable<S> testSamples);

    /**
     * 预测得分
     * @param testSet
     * @param predictedLabels
     * @return
     */
    double score(DataSet<S, L> testSet, Iterable<L> predictedLabels);

    /**
     * 获取训练数据集
     * @return
     */
    DataSet<S, L> getTrainSet();

    /**
     * 获取测试数据集
     * @return
     */
    DataSet<S, L> getTestSet();

    /**
     * 设置训练数据集
     * @param dataSet
     */
    void setTrainSet(DataSet<S, L> dataSet);
    /**
     * 设置测试数据集
     * @param dataSet
     */
    void setTestSet(DataSet<S, L> dataSet);
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
