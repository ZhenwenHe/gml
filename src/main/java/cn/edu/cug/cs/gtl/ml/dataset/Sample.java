package cn.edu.cug.cs.gtl.ml.dataset;


import jsat.classifiers.CategoricalData;
import jsat.classifiers.DataPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sample<KernelType extends NumericalData> extends DataPoint  {

    /**
     * Creates a new data point
     */
    public Sample() {
        super(new Vector());
    }

    /**
     * Creates a new data point
     *
     * @param numericalValues   a vector containing the numerical values for this data point
     * @param categoricalValues an array of the category values for this data point
     * @param categoricalData   an array of the category information of this data point
     */
    public Sample(KernelType numericalValues, int[] categoricalValues, CategoricalInfo[] categoricalData) {
        super(numericalValues, categoricalValues, categoricalData);
    }

    /**
     * 仅供内部使用
     * @param numericalValues
     * @param categoricalValues
     * @param categoricalData
     */
    Sample(KernelType numericalValues, int[] categoricalValues, CategoricalData[] categoricalData) {
        super(numericalValues, categoricalValues, categoricalData);
    }
    /**
     * Creates a new data point that has no categorical variables
     *
     * @param numericalValues a vector containing the numerical values for this data point
     */
    public Sample(KernelType numericalValues) {
        super(numericalValues);
    }


    /**
     * Creates a deep clone of this data point, such that altering either data point does not effect the other one.
     *
     * @return a deep clone of this data point.
     */
    public Sample<KernelType> clone() {
        return new Sample<KernelType>((KernelType) numericalValues.clone(),
                Arrays.copyOf(categoricalValues, categoricalValues.length),
                CategoricalInfo.copyOf((CategoricalInfo[]) categoricalData));
    }

    public NumericalData getNumericalData() {
        return (NumericalData) getNumericalValues();
    }

    /**
     * 获取第 i 个分类标签值，默认为获取第0个的值
     *
     * @param i
     * @return
     */
    public String getCategoricalLabel(int i) {
        return categoricalData[i].getOptionName(categoricalValues[i]);
    }

    /**
     * 获取样本数据中的所有分类标签值
     *
     * @return
     */
    public List<String> getCategoricalLabels() {
        List<String> ls = new ArrayList<>();
        for (int i = 0; i < categoricalData.length; ++i) {
            ls.add(categoricalData[i].getOptionName(categoricalValues[i]));
        }
        return ls;
    }

    /**
     * 获取样本数据中的第 i  个分类列的信息
     *
     * @return
     */
    public CategoricalInfo getCategoricalInfo(int i) {
        return (CategoricalInfo)categoricalData[i];
    }

}
