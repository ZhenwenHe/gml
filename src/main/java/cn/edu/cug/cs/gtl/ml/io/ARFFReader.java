package cn.edu.cug.cs.gtl.ml.io;

import cn.edu.cug.cs.gtl.ml.dataset.*;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ARFFReader {
    /**
     * Uses the given file path to load a data set from an ARFF file.
     *
     * @param file the path to the ARFF file to load
     * @return the data set from the ARFF file, or null if the file could not be loaded.
     */
    public static DataSet<Vector> read(File file) {
        try {
            return read(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ARFFReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Uses the given reader to load a data set assuming it follows the ARFF
     * file format
     *
     * @param input the reader to load the data set from
     * @return the data set from the stream, or null of the file could not be loaded
     */
    public static DataSet<Vector> read(Reader input) {
        return read(input, new DataStore<Vector>());
    }

    /**
     * Uses the given reader to load a data set assuming it follows the ARFF
     * file format
     *
     * @param input the reader to load the data set from
     * @param list  the data store to use
     * @return the data set from the stream, or null of the file could not be loaded
     */
    public static DataSet<Vector> read(Reader input, DataStore<Vector> list) {

        List<Double> weights = new ArrayList<>();

        BufferedReader br = new BufferedReader(input);

        int numOfVars = 0;
        int numReal = 0;
        List<Boolean> isReal = new ArrayList<>();
        List<String> variableNames = new ArrayList<>();
        List<HashMap<String, Integer>> catVals = new ArrayList<>();
        String line = null;
        CategoricalInfo[] categoricalData = null;
        try {
            boolean atData = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("%") || line.trim().isEmpty())
                    continue;///Its a comment, skip

                line = line.trim();

                if (line.startsWith("@") && !atData) {
                    line = line.substring(1).toLowerCase();


                    if (line.toLowerCase().startsWith("data")) {
                        categoricalData = new CategoricalInfo[numOfVars - numReal];

                        int k = 0;
                        for (int i = 0; i < catVals.size(); i++) {
                            if (catVals.get(i) != null) {
                                categoricalData[k] = new CategoricalInfo(catVals.get(i).size());
                                categoricalData[k].setCategoryName(variableNames.get(i));
                                for (Map.Entry<String, Integer> entry : catVals.get(i).entrySet())
                                    categoricalData[k].setOptionName(entry.getKey(), entry.getValue());
                                k++;
                            }
                        }

                        //prept to start reading in data
                        list.setNumNumeric(numReal);
                        list.setCategoricalDataInfo(categoricalData);
                        atData = true;
                        continue;
                    } else if (!line.toLowerCase().startsWith("attribute"))
                        continue;
                    numOfVars++;
                    line = line.substring("attribute".length()).trim();//Remove the space, it could be multiple spaces

                    String variableName = null;
                    line = line.replace("\t", " ");
                    if (line.startsWith("'")) {
                        Pattern p = Pattern.compile("'.+?'");
                        Matcher m = p.matcher(line);
                        m.find();
                        variableName = trimName(m.group());

                        line = line.replaceFirst("'.+?'", "placeHolder");
                    } else
                        variableName = trimName(line.trim().replaceAll("\\s+.*", ""));
                    variableNames.add(variableName);
                    String[] tmp = line.split("\\s+", 2);


                    if (tmp[1].trim().equals("real") || tmp[1].trim().equals("numeric") || tmp[1].trim().startsWith("integer")) {
                        numReal++;
                        isReal.add(true);
                        catVals.add(null);
                    } else//Not correct, but we arent supporting anything other than real and categorical right now
                    {
                        isReal.add(false);
                        String cats = tmp[1].replace("{", "").replace("}", "").trim();
                        if (cats.endsWith(","))
                            cats = cats.substring(0, cats.length() - 1);
                        String[] catValsRaw = cats.split(",");
                        HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
                        for (int i = 0; i < catValsRaw.length; i++) {
                            catValsRaw[i] = trimName(catValsRaw[i]);
                            tempMap.put(catValsRaw[i], i);
                        }
                        catVals.add(tempMap);
                    }
                } else if (atData && !line.isEmpty()) {
                    double weight = 1.0;
                    String[] tmp = line.split(",");
                    if (tmp.length != isReal.size()) {
                        String s = tmp[isReal.size()];
                        if (tmp.length == isReal.size() + 1)//{#} means the # is the weight
                        {
                            if (!s.matches("\\{\\d+(\\.\\d+)?\\}"))
                                throw new RuntimeException("extra column must indicate a data point weigh in the form of \"{#}\", instead bad token " + s + " was found");
                            weight = Double.parseDouble(s.substring(1, s.length() - 1));
                        } else {
                            throw new RuntimeException("Column had " + tmp.length + " values instead of " + isReal.size());
                        }
                    }

                    Vector vec = new Vector(numReal);

                    int[] cats = new int[numOfVars - numReal];
                    int k = 0;//Keeping track of position in cats
                    for (int i = 0; i < isReal.size(); i++) {
                        String val_string = tmp[i].trim();
                        if (isReal.get(i))
                            if (val_string.equals("?"))//missing value, indicated by NaN
                                vec.set(i - k, Double.NaN);
                            else
                                vec.set(i - k, Double.parseDouble(val_string));
                        else//Categorical
                        {
                            tmp[i] = trimName(tmp[i]).trim().toLowerCase();
                            if (tmp[i].equals("?"))//missing value, indicated by -1
                                cats[k++] = -1;
                            else
                                cats[k++] = catVals.get(i).get(tmp[i]);
                        }
                    }

                    list.addDataPoint(new Sample<Vector>(vec, cats, categoricalData));
                    weights.add(weight);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        DataSet<Vector> dataSet = new DataSet<Vector>(list,Vector.class);
        for (int i = 0; i < weights.size(); i++)
            dataSet.setWeight(i, weights.get(i));
        int k = 0;
        for (int i = 0; i < isReal.size(); i++)
            if (isReal.get(i))
                dataSet.setNumericName(variableNames.get(i), k++);

        return dataSet;
    }

    /**
     * Removes the quotes at the end and front of a string if there are any, as well as spaces at the front and end
     *
     * @param in the string to trim
     * @return the white space and quote trimmed string
     */
    private static String trimName(String in) {
        in = in.trim();
        if (in.startsWith("'") || in.startsWith("\""))
            in = in.substring(1);
        if (in.endsWith("'") || in.startsWith("\""))
            in = in.substring(0, in.length() - 1);
        return in.trim();
    }
}
