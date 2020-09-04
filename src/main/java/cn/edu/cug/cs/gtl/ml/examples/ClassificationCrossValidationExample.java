package cn.edu.cug.cs.gtl.ml.examples;

import cn.edu.cug.cs.gtl.ml.dataset.Vector;
import jsat.DataSet;
import jsat.SimpleDataSet;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.ClassificationModelEvaluation;
import jsat.classifiers.Classifier;
import jsat.classifiers.bayesian.NaiveBayes;
import jsat.io.ARFFLoader;

import java.io.File;

public class ClassificationCrossValidationExample extends Example{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        String nominalPath = getDataDirectory() ;
        File file = new File(nominalPath + "iris.arff");
        DataSet dataSet = ARFFLoader.loadArffFile(file);

        cn.edu.cug.cs.gtl.ml.dataset.DataSet<Vector> dataSet1= new cn.edu.cug.cs.gtl.ml.dataset.DataSet<Vector>(Vector.class);
        dataSet1.reset((SimpleDataSet) dataSet);

        //We specify '0' as the class we would like to make the target class.
        ClassificationDataSet cDataSet = new ClassificationDataSet(dataSet, 0);

        //We do not train the classifier, we let the modelEvaluation do that for us!
        Classifier classifier = new NaiveBayes();

        ClassificationModelEvaluation modelEvaluation = new ClassificationModelEvaluation(classifier, cDataSet);

        //The number of folds is how many times the data set will be split and trained and tested. 10 is a common value
        modelEvaluation.evaluateCrossValidation(10);

        System.out.println("Cross Validation error rate is " + 100.0*modelEvaluation.getErrorRate() + "%");

        //We can also obtain how long it took to train, and how long classification took
        System.out.println("Trainig time: " + modelEvaluation.getTotalTrainingTime()/1000.0 + " seconds");
        System.out.println("Classification time: " + modelEvaluation.getTotalClassificationTime()/1000.0 + " seconds\n");

        //The model can print a 'Confusion Matrix' this tells us about the errors our classifier made.
        //Each row represents all the data points that belong to a given class.
        //Each column represents the predicted class
        //That means values in the diagonal indicate the number of correctly classifier points in each class.
        //Off diagonal values indicate mistakes
        modelEvaluation.prettyPrintConfusionMatrix();

    }
}
