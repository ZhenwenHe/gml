package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.linear.Vec;

import java.io.*;
import java.util.List;

public class TestSet<T extends NumericalData> extends DataSet<T> {

    /**
     * Creates a new dataset containing the given datapoints. The number of
     * features and categorical data information will be obtained from the
     * DataStore.
     *
     * @param datapoints the collection of data points to create a dataset from
     */
    public TestSet(DataStore<T> datapoints) {
        super(datapoints);
    }

    /**
     * Creates a new empty data set
     *
     * @param numerical  the number of numerical features for points in this
     *                   dataset
     * @param categories the information and number of categorical features in
     */
    public TestSet(int numerical, CategoricalData[] categories) {
        super(numerical, categories);
    }
}
