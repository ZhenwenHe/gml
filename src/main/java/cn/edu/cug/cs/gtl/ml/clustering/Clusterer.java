package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚类器接口，属于无监督学习算法，具有有限个离散的输出结果。
 * 测试数据集和训练数据集为同一个数据集。
 * 聚类器必须提供的方法有fit()。
 * 另外，直推式聚类器应具有fit_predict()方法，归纳式聚类器应具有predict()方法。
 * @param <KernelType> 待聚类的数据类型，默认为时序数据
 * @param <LabelType> 聚类标签数据类型，默认为字符串
 */
public interface Clusterer<KernelType extends NumericalData, LabelType extends Label> {
    void setKernelClass(Class<KernelType> cls);

    void setLabelClass(Class<LabelType> cls);

    Class<KernelType> getKernelClass( );

    Class<LabelType> getLabelClass( );

    /**
     * 获取聚类结果
     * @return
     */
    List<List<Sample<KernelType>>> getClusters();
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
     * 用来根据给定的数据集合进行训练和拟合，得到训练预测模型
     * 相当于weka buildCluster
     * 相当于 jsat cluster
     * @param dataSet
     */
    void fit(DataSet<KernelType> dataSet);

    /**
     * 用数据集进行预测
     * 相当于weka clusterInstance的多次调用
     * 相当于 jsat cluster的多次调用
     * @param dataSet
     * @return
     */
    default Iterable<LabelType> predict(Iterable<Sample<KernelType>> dataSet){
        ArrayList<LabelType> al= new ArrayList<>();
        for(Sample<KernelType> s:dataSet){
            al.add((LabelType)(predict(s)));
        }
        return al;
    }

    /**
     * 用数据集进行预测，采用最邻近的对象的标签
     * 相当于weka clusterInstance
     * @param sample
     * @return
     */
    LabelType predict(Sample<KernelType> sample);

    /**
     * 预测得分
     * @param dataSet
     * @param i  如果数据集中有多个分类数据列，取其第i列的标签进行比较，默认为0列
     * @param predictedLabels
     * @return
     */
    double score(DataSet<KernelType> dataSet, int i, Iterable<LabelType> predictedLabels);
    default double score(DataSet<KernelType> dataSet, Iterable<LabelType> predictedLabels){
        return score(dataSet,0,predictedLabels);
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
        Iterable<LabelType> labels = predict(getTestSet().getSamples());
        return score(getTestSet(), labels);
    }

    /**
     *
     * @param assignments
     * @param dataSet
     * @param <KernelType>
     * @return
     */
    static <KernelType extends NumericalData> List<List<Sample<KernelType>>> createClustersFromAssignments(int[] assignments, DataSet<KernelType> dataSet) {
        List<List<Sample<KernelType>>> clusters = new ArrayList<>();

        for (int i = 0; i < dataSet.size(); i++) {
            while (clusters.size() <= assignments[i])
                clusters.add(new ArrayList<>());
            if (assignments[i] >= 0)
                clusters.get(assignments[i]).add(dataSet.getSample(i));
        }
        return clusters;
    }
}