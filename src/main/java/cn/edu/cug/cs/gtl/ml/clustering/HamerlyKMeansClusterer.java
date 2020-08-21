package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import cn.edu.cug.cs.gtl.ml.distances.EuclideanDistanceMetric;
import jsat.clustering.SeedSelectionMethods;
import jsat.clustering.kmeans.HamerlyKMeans;
import jsat.clustering.kmeans.KMeans;
import jsat.linear.distancemetrics.EuclideanDistance;

import java.util.List;

public class HamerlyKMeansClusterer<KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType>{

    HamerlyKMeans hamerlyKMeans;
    int [] assignments;

    public HamerlyKMeansClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        super(dataSet, distanceMetrics);
        hamerlyKMeans = new HamerlyKMeans(distanceMetrics, SeedSelectionMethods.SeedSelection.FARTHEST_FIRST);
    }

    public HamerlyKMeansClusterer() {
        distanceMetrics=new EuclideanDistanceMetric<KernelType>();
        hamerlyKMeans = new HamerlyKMeans(distanceMetrics,SeedSelectionMethods.SeedSelection.KPP);
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
        hamerlyKMeans.cluster(dataSet,assignments);
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
