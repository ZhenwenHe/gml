package cn.edu.cug.cs.gtl.ml.examples;

import cn.edu.cug.cs.gtl.ml.clustering.HamerlyKMeansClusterer;
import cn.edu.cug.cs.gtl.ml.dataset.DataSet;
import cn.edu.cug.cs.gtl.ml.dataset.Label;
import cn.edu.cug.cs.gtl.ml.dataset.Sample;
import cn.edu.cug.cs.gtl.ml.dataset.Vector;
import cn.edu.cug.cs.gtl.ml.io.ARFFReader;
import jsat.linear.Vec;

import java.io.File;
import java.util.List;

public class HamerlyKMeansClustererExample {
    public static void main(String[] args) {
        String nominalPath = File.separator + "Users" + File.separator + "zhenwenhe" + File.separator
                + "git" + File.separator + "data" + File.separator
                + "weka" + File.separator;
        File file = new File(nominalPath + "iris.arff");
        DataSet<Vector> dataSet = ARFFReader.read(file);

        HamerlyKMeansClusterer<Vector, Label> hamerlyKMeansClusterer= new HamerlyKMeansClusterer();
        hamerlyKMeansClusterer.fit(dataSet);
        List<List<Sample<Vector>>> lists=hamerlyKMeansClusterer.getClusters();
        for(List<Sample<Vector>> sampleList: lists){
            System.out.println(sampleList);
            for(Sample<Vector> s: sampleList){
                System.out.println(s);
            }
        }
        Label label = hamerlyKMeansClusterer.predict(dataSet.getSample(0));
        System.out.println(label);
    }
}
