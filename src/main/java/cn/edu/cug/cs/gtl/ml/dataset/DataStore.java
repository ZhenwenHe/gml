package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.RowMajorStore;
import jsat.classifiers.DataPoint;

import java.util.ArrayList;
import java.util.List;

public class DataStore<KernelType extends NumericalData> extends RowMajorStore {

    /**
     * Creates a new Data Store to add points to, where the number of features is not known in advance.
     */
    public DataStore() {
        this.datapoints=new ArrayList<>();
    }

    /**
     * Creates a new Data Store with the intent for a specific number of features known ahead of time.
     *
     * @param numNumeric the number of numeric features to be in the data store
     * @param cat_info   the information about the categorical data
     */
    public DataStore(int numNumeric, CategoricalInfo[] cat_info) {
        super(numNumeric, cat_info);
    }

    public DataStore(List<Sample<KernelType>> collection) {
        super((List<DataPoint>) ((Object)(collection)));
    }

    /**
     * Copy constructor
     *
     * @param toCopy the object to copy
     */
    public DataStore(RowMajorStore toCopy) {
        super(toCopy);
    }

    public void addSample(Sample<KernelType> dp)
    {
        datapoints.add(dp);
        num_numeric = Math.max(dp.getNumericalValues().length(), num_numeric);
        num_cat = Math.max(dp.getCategoricalValues().length, num_cat);
    }

    public CategoricalInfo[] getCategoricalInfos()
    {
        return (CategoricalInfo[]) cat_info;
    }

    public void setCategoricalInfos(CategoricalInfo[] categoricalInfos){
        cat_info=categoricalInfos;
    }

    public Sample<KernelType> getSample(int i)
    {
        return  (Sample<KernelType>)datapoints.get(i);
    }


    public void setSample(int i, Sample<KernelType> dp)
    {
        datapoints.set(i, dp);
    }

    public List<Sample<KernelType>> getSampleList(){
        return (List<Sample<KernelType>>)((Object)(datapoints));
    }
}
