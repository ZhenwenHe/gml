package cn.edu.cug.cs.gtl.ml.dataset;


import cn.edu.cug.cs.gtl.io.Storable;
import jsat.classifiers.DataPoint;
import jsat.linear.Vec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sample <T extends NumericalData>  extends DataPoint {

    /**
     * Creates a new data point
     *
     * @param numericalValues   a vector containing the numerical values for this data point
     * @param categoricalValues an array of the category values for this data point
     * @param categoricalData   an array of the category information of this data point
     */
    public Sample(T numericalValues, int[] categoricalValues, CategoricalData[] categoricalData) {
        super(numericalValues, categoricalValues, categoricalData);
    }

    /**
     * Creates a new data point that has no categorical variables
     *
     * @param numericalValues a vector containing the numerical values for this data point
     */
    public Sample(T numericalValues) {
        super(numericalValues);
    }


    /**
     * Creates a deep clone of this data point, such that altering either data point does not effect the other one.
     * @return a deep clone of this data point.
     */
    public Sample<T> clone()
    {
        return new Sample<T>((T) numericalValues.clone(),
                Arrays.copyOf(categoricalValues, categoricalValues.length),
                CategoricalData.copyOf((CategoricalData[])categoricalData));
    }

    public NumericalData getNumericalData(){
        return (NumericalData) getNumericalValues();
    }

    /**
     * 获取第 i 个分类标签值，默认为获取第0个的值
     * @param i
     * @return
     */
    public String getCategoricalLabel(int i){
        return categoricalData[i].getOptionName(categoricalValues[i]);
    }

    /**
     * 获取样本数据中的所有分类标签值
     * @return
     */
    public List<String> getCategoricalLabels(){
        List<String> ls =new ArrayList<>();
        for(int i=0;i<categoricalData.length;++i){
            ls.add(categoricalData[i].getOptionName(categoricalValues[i]));
        }
        return ls;
    }
}
