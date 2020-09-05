package cn.edu.cug.cs.gtl.ml.clustering.kmeans;

import cn.edu.cug.cs.gtl.ml.clustering.DefaultClusterer;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.clustering.kmeans.KMeans;
import jsat.clustering.kmeans.MiniBatchKMeans;

import java.util.List;

/**
 * The MiniBatchKMeans is a variant of the KMeans algorithm which uses mini-batches to reduce the computation time,
 * while still attempting to optimise the same objective function. Mini-batches are subsets of the input data,
 * randomly sampled in each training iteration.
 * These mini-batches drastically reduce the amount of computation required to converge to a local solution.
 * In contrast to other algorithms that reduce the convergence time of k-means,
 * mini-batch k-means produces results that are generally only slightly worse than the standard algorithm.
 *
 * The algorithm iterates between two major steps, similar to vanilla k-means. In the first step,b
 *  samples are drawn randomly from the dataset, to form a mini-batch.
 *  These are then assigned to the nearest centroid.
 *  In the second step, the centroids are updated.
 *  In contrast to k-means, this is done on a per-sample basis.
 *  For each sample in the mini-batch, the assigned centroid is updated by taking the streaming average of the sample and all previous samples assigned to that centroid.
 *  This has the effect of decreasing the rate of change for a centroid over time.
 *  These steps are performed until convergence or a predetermined number of iterations is reached.
 *
 * MiniBatchKMeans converges faster than KMeans, but the quality of the results is reduced.
 * In practice this difference in quality can be quite small, as shown in the example and cited reference.
 * “Web Scale K-Means clustering” D. Sculley, Proceedings of the 19th international conference on World wide web (2010)
 * @param <KernelType>
 * @param <LabelType>
 */
public class MiniBatchKMeansClusterer <KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType> {

    int [] assignments;
    MiniBatchKMeans miniBatchKMeans;


    /**
     *
     * @param dataSet
     * @param distanceMetrics the distance metric to use
     * @param batchSize the mini-batch size
     * @param iterations the number of mini batches to perform
     */
    public MiniBatchKMeansClusterer(DataSet<KernelType> dataSet,
                                    DistanceMetric<KernelType> distanceMetrics, int batchSize, int iterations) {
        super(dataSet, distanceMetrics);
        miniBatchKMeans=new MiniBatchKMeans(distanceMetrics,batchSize,iterations);
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
        miniBatchKMeans.cluster(dataSet,assignments);
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
