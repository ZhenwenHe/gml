package cn.edu.cug.cs.gtl.ml.clustering.kmedoids;

import cn.edu.cug.cs.gtl.ml.clustering.DefaultClusterer;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.clustering.CLARA;
import jsat.clustering.ClustererBase;

import java.util.List;

/**
 * CLARA(Clustering LARge Applications，大型应用中的聚类方法)(Kaufmann and Rousseeuw in 1990):
 * k-medoids变种算法之一，
 * 不考虑整个数据集, 而是选择数据的一小部分作为样本。它从数据集中抽取多个样本集, 对每个样本集使用PAM, 并以最好的聚类作为输出
 * 　　CLARA 算法的步骤:
 * 　　(1) for 　i = 1 to v (选样的次数) ,重复执行下列步骤( (2) ～ (4) ) :
 * 　　(2) 随机地从整个数据库中抽取一个N(例如：(40 + 2 k))个对象的样本,调用PAM方法从样本中找出样本的k个最优的中心点。
 * 　　(3)将这k个中心点应用到整个数据库上， 对于每一个非代表对象Oj ,判断它与从样本中选出的哪个代表对象距离最近.
 * 　　(4) 计算上一步中得到的聚类的总代价. 若该值小于当前的最小值,用该值替换当前的最小值,保留在这次选样中得到的k个代表对象作为到目前为止得到的最好的代表对象的集合.
 * 　　(5) 返回到步骤(1) ,开始下一个循环.
 * 　　算法结束后，输出最好的聚类结果。
 * @param <KernelType>
 * @param <LabelType>
 */
public class CLARAClusterer <KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType> {

    CLARA clara;
    int [] assignments;

    public CLARAClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        super(dataSet, distanceMetrics);
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
        clara.cluster(dataSet,assignments);
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
