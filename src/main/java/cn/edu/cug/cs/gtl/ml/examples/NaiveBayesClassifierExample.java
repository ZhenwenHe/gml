package cn.edu.cug.cs.gtl.ml.examples;

import cn.edu.cug.cs.gtl.ml.classification.Classifier;
import cn.edu.cug.cs.gtl.ml.classification.NaiveBayesClassifier;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.dataset.Vector;
import cn.edu.cug.cs.gtl.ml.io.ARFFReader;

import java.io.File;

public class NaiveBayesClassifierExample extends Example{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String nominalPath = getDataDirectory();
        File file = new File(nominalPath + "iris.arff");
        DataSet<Vector> dataSet = ARFFReader.read(file);

        //We specify '0' as the class we would like to make the target class.
        //ClassificationDataSet cDataSet = new ClassificationDataSet(dataSet, 0);

        int errors = 0;
        Classifier<Vector, Label> classifier = new NaiveBayesClassifier<Vector,Label>();
        classifier.fit(dataSet);

        for(int i = 0; i < dataSet.size(); i++)
        {
            Sample<Vector> dataPoint = dataSet.getSample(i);//It is important not to mix these up, the class has been removed from data points in 'cDataSet'
            String truth = dataSet.getSample(i).getCategoricalLabel(0);//We can grab the true category from the data set

            //Categorical Results contains the probability estimates for each possible target class value.
            //Classifiers that do not support probability estimates will mark its prediction with total confidence.
            Label predictionResults = classifier.predict(dataPoint);

            if(!predictionResults.equals(truth))
                errors++;
            System.out.println( i + "| True Class: " + truth + ", Predicted: " + predictionResults  );
        }

        System.out.println(errors + " errors were made, " + 100.0*errors/dataSet.size() + "% error rate" );
    }
}
