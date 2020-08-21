package cn.edu.cug.cs.gtl.ml.dataset;

public class TrainSet<KernelType extends NumericalData> extends DataSet<KernelType> {

    /**
     * Creates a new dataset containing the given datapoints. The number of
     * features and categorical data information will be obtained from the
     * DataStore.
     *
     * @param datapoints the collection of data points to create a dataset from
     */
    public TrainSet(DataStore<KernelType> datapoints, Class<KernelType> tClass) {
        super(datapoints,tClass);
    }

    /**
     * Creates a new empty data set
     *
     * @param numerical  the number of numerical features for points in this
     *                   dataset
     * @param categories the information and number of categorical features in
     */
    public TrainSet(int numerical, CategoricalInfo[] categories, Class<KernelType> tClass) {

        super(numerical, categories,tClass);
    }
}
