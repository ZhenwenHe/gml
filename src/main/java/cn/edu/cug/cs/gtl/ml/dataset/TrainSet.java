package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.linear.Vec;

import java.io.IOException;
import java.util.List;

public class TrainSet<T extends NumericalData> extends DataSet<T> {

    /**
     * Creates a new dataset containing the given datapoints. The number of
     * features and categorical data information will be obtained from the
     * DataStore.
     *
     * @param datapoints the collection of data points to create a dataset from
     */
    public TrainSet(DataStore<T> datapoints) {
        super(datapoints);
    }

    /**
     * Creates a new empty data set
     *
     * @param numerical  the number of numerical features for points in this
     *                   dataset
     * @param categories the information and number of categorical features in
     */
    public TrainSet(int numerical, CategoricalData[] categories) {
        super(numerical, categories);
    }
}
