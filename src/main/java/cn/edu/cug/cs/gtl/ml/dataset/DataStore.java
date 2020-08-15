package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.RowMajorStore;
import jsat.classifiers.DataPoint;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.List;

public class DataStore<T extends NumericalData> extends RowMajorStore {

    /**
     * Creates a new Data Store to add points to, where the number of features is not known in advance.
     */
    public DataStore() {
    }

    /**
     * Creates a new Data Store with the intent for a specific number of features known ahead of time.
     *
     * @param numNumeric the number of numeric features to be in the data store
     * @param cat_info   the information about the categorical data
     */
    public DataStore(int numNumeric, CategoricalData[] cat_info) {
        super(numNumeric, cat_info);
    }

    public DataStore(List<Sample<T>> collection) {
        super(toDataPointList(collection));
    }

    /**
     * Copy constructor
     *
     * @param toCopy the object to copy
     */
    public DataStore(RowMajorStore toCopy) {
        super(toCopy);
    }

    static <T extends NumericalData> List<DataPoint> toDataPointList(List<Sample<T>> collection){
        List<DataPoint> dataPoints= new ArrayList<>(collection);
        return dataPoints;
    }

    static <T extends NumericalData> List<Sample<T>> toSampleList(List<DataPoint> collection){
        int s = collection.size();
        List<Sample<T>> dataPoints= new ArrayList<>(s);
        for(int i=0;i<s;++i){
            dataPoints.add((Sample<T>)collection.get(i));
        }
        return dataPoints;
    }

    public void addSample(Sample<T> dp)
    {
        datapoints.add(dp);
        num_numeric = Math.max(dp.getNumericalValues().length(), num_numeric);
        num_cat = Math.max(dp.getCategoricalValues().length, num_cat);
    }

    public CategoricalData[] getCategoricalData()
    {
        return (CategoricalData[]) cat_info;
    }

    public Sample<T> getSample(int i)
    {
        return  (Sample<T>)datapoints.get(i);
    }


    public void setSample(int i, Sample<T> dp)
    {
        datapoints.set(i, dp);
    }

    public List<Sample<T>> getSampleList(){
        return toSampleList(datapoints);
    }

}
