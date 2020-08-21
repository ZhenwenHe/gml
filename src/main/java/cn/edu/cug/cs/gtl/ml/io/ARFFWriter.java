package cn.edu.cug.cs.gtl.ml.io;

import jsat.DataSet;
import jsat.classifiers.CategoricalData;
import jsat.classifiers.ClassificationDataSet;
import jsat.classifiers.DataPoint;
import jsat.linear.Vec;
import jsat.regression.RegressionDataSet;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ARFFWriter {

    public static void write(DataSet data, OutputStream os) {
        write(data, os, "Default_Relation");
    }

    /**
     * Writes out the dataset as an ARFF file to the given stream. This method
     * will automatically handle the target variable of
     * {@link ClassificationDataSet} and {@link RegressionDataSet}.
     *
     * @param data     the dataset to write out
     * @param os       the output stream to write too
     * @param relation the relation label to write out
     */
    public static void write(DataSet data, OutputStream os, String relation) {
        PrintWriter writer = new PrintWriter(os);
        //write out the relation tag
        writer.write(String.format("@relation %s\n", addQuotes(relation)));

        //write out attributes
        //first all categorical features
        CategoricalData[] catInfo = data.getCategories();
        for (CategoricalData cate : catInfo) {
            writeCatVar(writer, cate);
        }
        //write out all numeric features
        for (int i = 0; i < data.getNumNumericalVars(); i++) {
            String name = data.getNumericName(i);
            writer.write("@attribute " + (name == null ? "num" + i : name.replaceAll("\\s+", "-")) + " NUMERIC\n");
        }
        if (data instanceof ClassificationDataSet)//also write out class variable
            writeCatVar(writer, ((ClassificationDataSet) data).getPredicting());
        if (data instanceof RegressionDataSet)
            writer.write("@ATTRIBUTE target NUMERIC\n");
        writer.write("@DATA\n");
        for (int row = 0; row < data.size(); row++) {
            DataPoint dp = data.getDataPoint(row);
            boolean firstFeature = true;
            //cat vars first
            for (int i = 0; i < catInfo.length; i++) {
                if (!firstFeature)
                    writer.write(",");
                firstFeature = false;
                int cat_val = dp.getCategoricalValue(i);
                if (cat_val < 0)
                    writer.write("?");
                else
                    writer.write(addQuotes(catInfo[i].getOptionName(cat_val)));
            }
            //numeric vars
            Vec v = dp.getNumericalValues();
            for (int i = 0; i < v.length(); i++) {
                if (!firstFeature)
                    writer.write(",");
                firstFeature = false;
                double val = v.get(i);
                if (Double.isNaN(val))//missing value case
                    writer.write("?");
                else if (Math.rint(val) == val)//cast to long before writting to save space
                    writer.write(Long.toString((long) val));
                else
                    writer.write(Double.toString(val));
            }
            if (data instanceof ClassificationDataSet)//also write out class variable
            {
                if (!firstFeature)
                    writer.write(",");
                firstFeature = false;
                ClassificationDataSet cdata = (ClassificationDataSet) data;
                writer.write(addQuotes(cdata.getPredicting().getOptionName(cdata.getDataPointCategory(row))));
            }
            if (data instanceof RegressionDataSet) {
                if (!firstFeature)
                    writer.write(",");
                firstFeature = false;
                writer.write(Double.toString(((RegressionDataSet) data).getTargetValue(row)));
            }
            writer.write("\n");
        }
        writer.flush();
    }

    private static String addQuotes(String string) {
        if (string.contains(" "))
            return "\"" + string + "\"";
        else
            return string;
    }

    private static void writeCatVar(PrintWriter writer, CategoricalData cate) {
        writer.write("@ATTRIBUTE " + cate.getCategoryName().replaceAll("\\s+", "-") + " {");
        for (int i = 0; i < cate.getNumOfCategories(); i++) {
            if (i != 0)
                writer.write(",");
            writer.write(addQuotes(cate.getOptionName(i)));
        }
        writer.write("}\n");
    }


}
