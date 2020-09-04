package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.clustering.kmeans.ElkanKMeans;
import jsat.clustering.kmeans.KMeans;

import java.util.List;
/**
 * An efficient implementation of the K-Means algorithm. This implementation uses
 * the triangle inequality to accelerate computation while maintaining the exact
 * same solution. This requires that the {@link cn.edu.cug.cs.gtl.ml.distances.DistanceMetric} used support
 * isSubadditive().
 * <br>
 * Implementation based on the paper: Using the Triangle Inequality to Accelerate k-Means, by Charles Elkan
 *
 */
public class ElkanKMeansClusterer <KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType>{

    ElkanKMeans elkanKMeans;
    int [] assignments;

    public ElkanKMeansClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        super(dataSet, distanceMetrics);
        elkanKMeans=new ElkanKMeans(distanceMetrics);
    }


    /**
     * 获取聚类结果
     *
     * @return
     */
    @Override
    public List<List<Sample<KernelType>>> getClusters() {
        if(assignments==null) return null;
        return (List<List<Sample<KernelType>>>)((List)(KMeans.createClusterListFromAssignmentArray(assignments,trainSet)));
    }


    @Override
    public void fit(DataSet<KernelType> dataSet) {
        super.fit(dataSet);
        assignments = new int[dataSet.size()];
        elkanKMeans.cluster(dataSet,assignments);
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
