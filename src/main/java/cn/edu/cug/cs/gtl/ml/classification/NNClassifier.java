package cn.edu.cug.cs.gtl.ml.classification;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;


import java.util.ArrayList;
import java.util.List;

public class NNClassifier<S extends NumericalData, L> extends DefaultClassifier<S, L> {

    protected NNClassifier() {
    }

    public NNClassifier(DataSet<S> trainSet, DataSet<S> testSet, DistanceMetric<S> distanceMetrics) {
        super(trainSet, testSet, distanceMetrics);
    }

    @Override
    public Iterable<L> predict(Iterable<Sample<S>> testSamples) {
        long trainDataLen = this.trainSet.size();
        double minDis = Double.MAX_VALUE;
        List<L> labelList = new ArrayList<>();
        L label = null;
        int pos = 0;
        for (Sample<S> i : testSamples) {
            minDis = Double.MAX_VALUE;
            for (int j = 0; j < trainDataLen; ++j) {
                double tempDis = this.distanceMetrics.distance((S)i.getNumericalValues(),
                        (S)this.trainSet.getSample(j).getNumericalValues());
                if (tempDis < minDis) {
                    minDis = tempDis;
                    pos = j;
                }
            }
            label = (L)this.trainSet.getSample(pos).getCategoricalLabel(0);
            labelList.add(label);
        }
        return labelList;
    }

}
