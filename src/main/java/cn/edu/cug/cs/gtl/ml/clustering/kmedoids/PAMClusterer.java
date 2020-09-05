package cn.edu.cug.cs.gtl.ml.clustering.kmedoids;

import cn.edu.cug.cs.gtl.ml.clustering.DefaultClusterer;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.clustering.ClustererBase;
import jsat.clustering.PAM;

import java.util.List;

/**
 * PAM算法（Partitioning Around Medoid,围绕中心点的划分）,
 * k-medoids变种算法,是聚类分析算法中划分法的一个聚类方法，
 * 是最早提出的k-中心点算法之一。基本原理为：选用簇中位置最中心的对象，
 * 试图对n个对象给出k个划分；代表对象也被称为是中心点，
 * 其他对象则被称为非代表对象；最初随机选择k个对象作为中心点，
 * 该算法反复地用非代表对象来代替代表对象，试图找出更好的中心点，
 * 以改进聚类的质量；在每次迭代中，所有可能的对象对被分析，
 * 每个对中的一个对象是中心点，而另一个是非代表对象。对可能的各种组合，
 * 估算聚类结果的质量；一个对象Oi可以被使最大平方-误差值减少的对象代替；
 * 在一次迭代中产生的最佳对象集合成为下次迭代的中心点。
 * @param <KernelType>
 * @param <LabelType>
 */
public class PAMClusterer <KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType> {

    PAM pam;
    int [] assignments;

    public PAMClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        super(dataSet, distanceMetrics);
        pam=new PAM(distanceMetrics);
    }

    /**
     * 获取聚类结果
     *
     * @return
     */
    @Override
    public List<List<Sample<KernelType>>> getClusters() {
        if(assignments==null) return null;
        return (List<List<Sample<KernelType>>>)((List)(ClustererBase.createClusterListFromAssignmentArray(assignments,trainSet)));
    }

    @Override
    public void fit(DataSet<KernelType> dataSet) {
        super.fit(dataSet);
        assignments = new int[dataSet.size()];
        pam.cluster(dataSet,assignments);
    }

    @Override
    public LabelType predict(Sample<KernelType> testSample) {
        if(assignments==null)
            fit(getTrainSet());

        int sampleCount = getTrainSet().size();
        int r=-1;
        double minDistance = Double.MAX_VALUE;
        if(distanceMetrics!=null){
            for(int i=0;i<sampleCount;++i){
                double tmp = distanceMetrics.distance(testSample.getNumericalData(),trainSet.getSample(i).getNumericalData());
                if(minDistance> tmp) {
                    minDistance = tmp;
                    r=i;
                }
            }
            LabelType label = newLabel(assignments[r]);
            return label;
        }
        return null;
    }
}
