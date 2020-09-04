package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;
import jsat.classifiers.CategoricalResults;
import jsat.classifiers.knn.NearestNeighbour;

public class NearestNeighbourClassifier<KernelType extends NumericalData, LabelType extends Label> extends DefaultClassifier<KernelType,LabelType>{

    NearestNeighbour nearestNeighbour;

    /**
     * Constructs a new Nearest Neighbor Classifier
     * @param k the number of neighbors to use
     * @param weighted whether or not to weight the influence of neighbors by their distance
     * @param distanceMetric the method of computing distance between two vectors.
     */

    public NearestNeighbourClassifier(int k, boolean weighted,DistanceMetric<KernelType> distanceMetric ) {
        super(null,null,distanceMetric);
        nearestNeighbour=new NearestNeighbour(k,weighted,distanceMetric);
    }

    @Override
    public void fit(DataSet<KernelType> trainSet) {
        setTrainSet(trainSet);
        nearestNeighbour.train(trainSet.asClassificationDataSet(0),false);
    }

    @Override
    public LabelType predict(Sample<KernelType> testSample) {
        CategoricalResults cr = nearestNeighbour.classify(testSample);
        return newLabel(trainSet.getCategories()[0].getOptionName(cr.mostLikely()));
    }
}