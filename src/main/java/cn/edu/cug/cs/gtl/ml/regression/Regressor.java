package cn.edu.cug.cs.gtl.ml.regression;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;

public interface Regressor <T extends NumericalData>{
    double regress(Sample<T> data);

    void train(DataSet<T> dataSet, boolean parallel);

    default void train(DataSet<T> dataSet)
    {
        train(dataSet, false);
    }

    boolean supportsWeightedData();

    Regressor clone();
}
