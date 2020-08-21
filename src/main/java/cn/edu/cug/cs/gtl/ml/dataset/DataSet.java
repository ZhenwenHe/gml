package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.SimpleDataSet;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.regression.RegressionDataSet;

import java.util.ArrayList;
import java.util.List;

public class DataSet<KernelType extends NumericalData> extends jsat.DataSet<DataSet> {

    protected Class<KernelType> classKernelType;

    protected Class<KernelType> getKernelTypeClass() {
        return classKernelType;
    }

    protected KernelType newKernelInstance() {
        try {
            return this.classKernelType.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new empty dataset.
     */
    public DataSet(Class<KernelType> tClass) {
        super(0, new CategoricalInfo[0]);
        this.classKernelType = tClass;
    }

    /**
     * Creates a new dataset containing the given datapoints.
     *
     * @param datapoints the collection of data points to create a dataset from
     */
    public DataSet(List<Sample<KernelType>> datapoints, Class<KernelType> tClass) {
        super(datapoints.get(0).numNumericalValues(), datapoints.get(0).getCategoricalData());
        this.classKernelType = tClass;
        for (Sample<KernelType> dp : datapoints)
            this.addSample(dp);
    }

    /**
     * Creates a new dataset containing the given datapoints. The number of
     * features and categorical data information will be obtained from the
     * DataStore.
     *
     * @param datapoints the collection of data points to create a dataset from
     */
    public DataSet(DataStore datapoints, Class<KernelType> tClass) {
        super(datapoints);
        this.classKernelType = tClass;
    }

    /**
     * Creates a new empty data set
     *
     * @param numerical  the number of numerical features for points in this
     *                   dataset
     * @param categories the information and number of categorical features in
     *                   this dataset
     */
    public DataSet(int numerical, CategoricalInfo[] categories, Class<KernelType> tClass) {
        super(numerical, categories);
    }

    /**
     * Adds a new datapoint to this set.
     *
     * @param dp the datapoint to add
     */
    public void addSample(Sample<KernelType> dp) {
        base_add(dp, 1.0);
    }

    public Sample<KernelType> getSample(int i) {
        return (Sample<KernelType>) getDataPoint(i);
    }

    @Override
    protected DataSet getSubset(List<Integer> indicies) {
        DataSet<KernelType> newData = new DataSet<KernelType>(numNumerVals, (CategoricalInfo[]) categories, getKernelTypeClass());
        for (int i : indicies)
            newData.addSample(getSample(i));
        return newData;
    }

    /**
     * Converts this dataset into one meant for classification problems. The
     * given categorical feature index is removed from the data and made the
     * target variable for the classification problem.
     *
     * @param index the classification variable index, should be in the range
     *              [0, {@link #getNumCategoricalVars() })
     * @return a new dataset where one categorical variable is removed and made
     * the target of a classification dataset
     */
    public ClassificationDataSet asClassificationDataSet(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Index must be a non-negative value");
        else if (getNumCategoricalVars() == 0)
            throw new IllegalArgumentException("Dataset has no categorical variables, can not create classification dataset");
        else if (index >= getNumCategoricalVars())
            throw new IllegalArgumentException("Index " + index + " is larger than number of categorical features " + getNumCategoricalVars());
        return new ClassificationDataSet(this, index);
    }

    /**
     * Converts this dataset into one meant for regression problems. The
     * given numeric feature index is removed from the data and made the
     * target variable for the regression problem.
     *
     * @param index the regression variable index, should be in the range
     *              [0, {@link #getNumNumericalVars() })
     * @return a new dataset where one numeric variable is removed and made
     * the target of a regression dataset
     */
    public RegressionDataSet asRegressionDataSet(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Index must be a non-negative value");
        else if (getNumNumericalVars() == 0)
            throw new IllegalArgumentException("Dataset has no numeric variables, can not create regression dataset");
        else if (index >= getNumNumericalVars())
            throw new IllegalArgumentException("Index " + index + " i larger than number of numeric features " + getNumNumericalVars());

        RegressionDataSet rds = new RegressionDataSet(this.datapoints.toList(), index);
        for (int i = 0; i < size(); i++)
            rds.setWeight(i, this.getWeight(i));
        return rds;
    }

    /**
     * @return access to a list of the list that backs this data set. May or may
     * not be backed by the original data.
     */
    public List<Sample<KernelType>> getSampleList() {
        return ((DataStore) datapoints).getSampleList();
    }

    public List<Sample<KernelType>> getSamples() {
        return ((DataStore) datapoints).getSampleList();
    }

    @Override
    public DataSet shallowClone() {
        return new DataSet(new ArrayList<>(this.datapoints.toList()), getKernelTypeClass());
    }

    @Override
    public DataSet emptyClone() {
        DataSet sds = new DataSet(numNumerVals, (CategoricalInfo[]) categories, getKernelTypeClass());
        return sds;
    }

    @Override
    public DataSet getTwiceShallowClone() {
        return (DataSet) super.getTwiceShallowClone();
    }


    /**
     * Creates a new dataset containing the given datapoints.
     *
     * @param simpleDataSet the collection of data points to create a dataset from
     */
    public void reset(SimpleDataSet simpleDataSet) {
        /**
         * The number of numerical values each data point must have
         */
        this.numNumerVals = simpleDataSet.getNumNumericalVars();
        /**
         * Contains the categories for each of the categorical variables
         */
        this.categories = simpleDataSet.getCategories();
        for (int i = 0; i < this.categories.length; ++i) {
            this.categories[i] = new CategoricalInfo(categories[i]);
        }

        /**
         * The backing store that holds all data points
         */
        //protected jsat.DataStore datapoints;
        this.datapoints = new DataStore<KernelType>();
        this.datapoints.setNumNumeric(numNumerVals);
        this.datapoints.setCategoricalDataInfo(this.categories);
        List<DataPoint> dataPointList = simpleDataSet.getDataPoints();

        for (DataPoint dp : dataPointList) {
            KernelType kernelType = newKernelInstance();
            kernelType.reset(dp.getNumericalValues().arrayCopy());
            Sample<KernelType> s = new Sample<KernelType>(kernelType,dp.getCategoricalValues(),this.categories);
            addSample(s);
        }

        /**
         * Store all the weights for each data point. If null, indicates an implicit
         * value of 1.0 for each datumn.
         */
        this.weights = simpleDataSet.getDataWeights().arrayCopy();

        /**
         * The map of the names of the numeric variables.
         */
        //protected Map<Integer, String> numericalVariableNames;
        int c = simpleDataSet.getNumNumericalVars();
        for (int i = 0; i < c; ++i) {
            this.numericalVariableNames.put(i, simpleDataSet.getNumericName(i));
        }
    }

    /**
     * 获取数值列的信息列表
     * @return
     */
    public List<NumericalInfo> getNumericalInfos(){
        List<NumericalInfo> numericalInfoList = new ArrayList<>();
        int c = this.getNumNumericalVars();
        for (int i = 0; i < c; ++i) {
            numericalInfoList.add(new NumericalInfo(i,this.getNumericName(i)));
        }
        return numericalInfoList;
    }

    /**
     * 获取分类数据标签列的信息
     * @return
     */
    public List<CategoricalInfo> getCategoricalInfos(){
        List<CategoricalInfo> categoricalInfoList = new ArrayList<>();
        int c = this.categories.length;
        for (int i = 0; i < c; ++i) {
            categoricalInfoList.add((CategoricalInfo)this.categories[i]);
        }
        return categoricalInfoList;
    }

}
