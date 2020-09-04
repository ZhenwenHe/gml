package cn.edu.cug.cs.gtl.ml.dataset;

import java.util.List;

/**
 *分类数据(categorical data)是按照现象的某种属性对其进行分类或分组而得到的反映事物类型的数据，又称定类数据。
 * 例如，按照性别将人口分为男、女两类；按照经济性质将企业分为国有、集体、私营、其他经济等。“男”、“女”，“国有”、
 * “集体”、“私营”和“其他经济”就是分类数据。为了便于计算机处理，通常用数字代码来表述各个类别，比如，用1表示“男性”，
 * 0表示“女性”，但是1和0等只是数据的代码，它们之间没有数量上的关系和差异。
 */
public class CategoricalInfo extends jsat.classifiers.CategoricalData{
    public CategoricalInfo(int n) {
        super(n);
    }

    public CategoricalInfo(String categoryName, List<String> optionNames) {
        super(optionNames.size());
        setCategoryName(categoryName);
        int i=0;
        for(String s:optionNames){
            setOptionName(s,i);
            ++i;
        }
    }

    public CategoricalInfo(jsat.classifiers.CategoricalData categoricalData) {
        super(categoricalData.getNumOfCategories());
        setCategoryName(categoricalData.getCategoryName());
        int s=categoricalData.getNumOfCategories();
        for(int i=0;i<s;++i){
            setOptionName(categoricalData.getOptionName(i),i);
            ++i;
        }
    }


    /**
     * 获取该字段下的所有选项值个数
     * @return
     */
    public int getNumberOfOptions(){
        return getNumOfCategories();
    }


    public CategoricalInfo clone()
    {
        int n = getNumberOfOptions();
        CategoricalInfo copy = new CategoricalInfo(n);

        if(n>0) {
            for(int i=0;i<n;++i)
                setOptionName(getOptionName(i),i);
        }
        return copy;
    }

    public static CategoricalInfo[] copyOf(CategoricalInfo[] orig)
    {
        CategoricalInfo[] copy = new CategoricalInfo[orig.length];
        for(int i = 0; i < copy.length; i++)
            copy[i] = orig[i].clone();
        return copy;
    }

    /**
     * 根据名称查找下标
     * @param optionName
     * @return
     */
    public int indexOfOption(String optionName){
        for(int i=0;i<getNumberOfOptions();++i){
            if(optionName.equals(getOptionName(i)))
                return i;
        }
        return -1;
    }
}
