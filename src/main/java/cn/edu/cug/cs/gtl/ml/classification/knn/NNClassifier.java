package cn.edu.cug.cs.gtl.ml.classification.knn;

import cn.edu.cug.cs.gtl.ml.classification.DefaultClassifier;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.distances.DistanceMetric;


import java.util.ArrayList;
import java.util.List;

public class NNClassifier<KernelType extends NumericalData, LabelType extends Label> extends DefaultClassifier<KernelType, LabelType> {

    protected NNClassifier() {
    }

    public NNClassifier(DataSet<KernelType> trainSet, DataSet<KernelType> testSet, DistanceMetric<KernelType> distanceMetrics) {
        super(trainSet, testSet, distanceMetrics);
    }

    @Override
    public void fit(DataSet<KernelType> trainSet) {
        setTrainSet(trainSet);
    }

    @Override
    public LabelType predict(Sample<KernelType> testSample) {
        long trainDataLen = this.trainSet.size();
        double minDis = Double.MAX_VALUE;
        int pos = 0;
        for (int j = 0; j < trainDataLen; ++j) {
            double tempDis = this.distanceMetrics.distance((KernelType)testSample.getNumericalValues(),
                    (KernelType)this.trainSet.getSample(j).getNumericalValues());
            if (tempDis < minDis) {
                minDis = tempDis;
                pos = j;
            }
        }
        return newLabel(this.trainSet.getSample(pos).getCategoricalLabel(0));
    }

    @Override
    public Iterable<LabelType> predict(Iterable<Sample<KernelType>> testSamples) {
        long trainDataLen = this.trainSet.size();
        double minDis = Double.MAX_VALUE;
        List<LabelType> labelList = new ArrayList<>();
        LabelType label = null;
        int pos = 0;
        for (Sample<KernelType> i : testSamples) {
            minDis = Double.MAX_VALUE;
            for (int j = 0; j < trainDataLen; ++j) {
                double tempDis = this.distanceMetrics.distance((KernelType)i.getNumericalValues(),
                        (KernelType)this.trainSet.getSample(j).getNumericalValues());
                if (tempDis < minDis) {
                    minDis = tempDis;
                    pos = j;
                }
            }
            label = newLabel(this.trainSet.getSample(pos).getCategoricalLabel(0));
            labelList.add(label);
        }
        return labelList;
    }

}
