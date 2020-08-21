package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;

import java.util.ArrayList;

/**
 * 分类器接口，具有有限个可能的离散值作为结果的有监督（或半监督）预测器。
 * 对于特定的输入样本，分类器总能给出有限离散值中的一个作为结果。
 * @param <KernelType> 待分类的数据类型，默认为时序数据
 * @param <LabelType> 分类标签数据类型，默认为字符串
 */
public interface Classifier<KernelType extends NumericalData, LabelType extends Label> {


    void setKernelClass(Class<KernelType> cls);

    void setLabelClass(Class<LabelType> cls);

    Class<KernelType> getKernelClass( );

    Class<LabelType> getLabelClass( );

    /**
     * 设置距离度量
     * @param distanceMetrics
     */
    void setDistanceMetric(DistanceMetric<KernelType> distanceMetrics);

    /**
     * 获取距离度量
     * @return
     */
    DistanceMetric<KernelType> getDistanceMetric();

    /**
     * 用来根据给定的数据对模型进行训练和拟合，得到训练预测模型
     * 相当于weka buildClassifier
     * 相当于 jsat train的
     * @param trainSet
     */
    void fit(DataSet<KernelType> trainSet);

    /**
     * 用测试数据进行预测，
     * 相当于weka classifyInstance
     * 相当于 jsat classify
     * @param testSample
     * @return
     */
    LabelType predict(Sample<KernelType> testSample);
    /**
     * 用测试数据进行预测，
     * 相当于weka classifyInstance的多次调用
     * 相当于 jsat classify的多次调用
     * @param testSamples
     * @return
     */
    default Iterable<LabelType> predict(Iterable<Sample<KernelType>> testSamples){
        ArrayList<LabelType> al= new ArrayList<>();
        for(Sample<KernelType> s:testSamples){
            al.add((LabelType)(predict(s)));
        }
        return al;
    }


    /**
     * 预测得分
     * @param testSet
     * @param i  如果数据集中有多个分类数据列，取其第i列的标签进行比较，默认为0列
     * @param predictedLabels
     * @return
     */
    double score(DataSet<KernelType> testSet, int i, Iterable<LabelType> predictedLabels);
    default double score(DataSet<KernelType> testSet, Iterable<LabelType> predictedLabels){
        return score(testSet,0,predictedLabels);
    }


    /**
     * 获取训练数据集
     * @return
     */
    DataSet<KernelType> getTrainSet();

    /**
     * 获取测试数据集
     * @return
     */
    DataSet<KernelType> getTestSet();

    /**
     * 设置训练数据集
     * @param dataSet
     */
    void setTrainSet(DataSet<KernelType> dataSet);
    /**
     * 设置测试数据集
     * @param dataSet
     */
    void setTestSet(DataSet<KernelType> dataSet);
    /**
     * 根据设置的测试数据集和训练数据集，进行得分计算
     * @param
     */
    default double score() {
        assert getTestSet() != null;
        assert getTrainSet() != null;
        assert getDistanceMetric() != null;
        fit(getTrainSet());
        Iterable<LabelType> labels = predict((Iterable<Sample<KernelType>>) getTestSet().getSamples());
        return score(getTestSet(), labels);
    }
}
