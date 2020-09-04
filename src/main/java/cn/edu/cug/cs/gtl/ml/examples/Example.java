package cn.edu.cug.cs.gtl.ml.examples;

import java.io.File;

public class Example {
    protected static String dataDirectory=
            File.separator+"Users" + File.separator + "zhenwenhe" + File.separator
                +"git" + File.separator + "data" + File.separator
                +"weka" + File.separator;

    public static String getDataDirectory() {
        return dataDirectory;
    }

    public static void setDataDirectory(String dataDirectory) {
        Example.dataDirectory = dataDirectory;
    }
}
