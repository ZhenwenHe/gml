package cn.edu.cug.cs.gtl.ml.examples;

import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.io.ARFFReader;

import java.io.File;
/**
 * A simple example on loading up a data set.
 */
public class ARFFReaderExample
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String nominalPath = File.separator+"Users" + File.separator + "zhenwenhe" + File.separator
                +"git" + File.separator + "data" + File.separator
                +"weka" + File.separator ;
        File file = new File(nominalPath + "iris.arff");
        DataSet dataSet = ARFFReader.read(file);
        System.out.println("There are " + dataSet.getNumFeatures() + " features for this data set.");
        System.out.println(dataSet.getNumCategoricalVars() + " categorical features");
        System.out.println("They are:");
        for(int i = 0; i <  dataSet.getNumCategoricalVars(); i++)
            System.out.println("\t" + dataSet.getCategoryName(i));
        System.out.println(dataSet.getNumNumericalVars() + " numerical features");
        System.out.println("They are:");
        for(int i = 0; i <  dataSet.getNumNumericalVars(); i++)
            System.out.println("\t" + dataSet.getNumericName(i));

        System.out.println("\nThe whole data set");
        for(int i = 0; i < dataSet.size(); i++)
        {
            Sample dataPoint = dataSet.getSample(i);
            System.out.println(dataPoint);
        }

    }
}