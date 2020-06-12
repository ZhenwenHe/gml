package cn.edu.cug.cs.gtl.ml.dataset;

import java.io.*;
import java.util.List;

public class TestSet<S, L> extends DefaultDataSet<S, L> {

    protected TestSet() {
    }

    public TestSet(List<S> samples, List<L> labels) {
        super(samples, labels);
    }

    @Override
    public Object clone() {
        TestSet<S, L> ts = new TestSet<S, L>();
        try {
            byte[] bytes = this.storeToByteArray();
            ts.loadFromByteArray(bytes);
            return ts;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
