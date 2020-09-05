package cn.edu.cug.cs.gtl.ml.clustering;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.clustering.ClustererBase;
import jsat.clustering.MeanShift;
import jsat.clustering.kmeans.KMeans;

import java.util.List;

/**
 * “Mean shift: A robust approach toward feature space analysis.” D. Comaniciu and P. Meer, IEEE Transactions on Pattern Analysis and Machine Intelligence (2002)
 * http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.76.8968&rep=rep1&type=pdf
 * @param <KernelType>
 * @param <LabelType>
 */
public class MeanShiftClusterer <KernelType extends NumericalData, LabelType extends Label> extends DefaultClusterer<KernelType, LabelType> {

    int [] assignments;
    MeanShift meanShift;

    public MeanShiftClusterer(DataSet<KernelType> dataSet, DistanceMetric<KernelType> distanceMetrics) {
        super(dataSet, distanceMetrics);
        meanShift=new MeanShift(distanceMetrics);
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
        meanShift.cluster(dataSet,assignments);
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
