package cn.edu.cug.cs.gtl.ml.dataset;

import cn.edu.cug.cs.gtl.io.Storable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NumericalInfo implements Storable {
    int index;
    String name;

    public NumericalInfo(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public Object clone() {
        return new NumericalInfo(this.index,this.name);
    }

    @Override
    public boolean load(DataInput dataInput) throws IOException {
        this.index=dataInput.readInt();
        this.name=dataInput.readUTF();
        return true;
    }

    @Override
    public boolean store(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.index);
        dataOutput.writeUTF(this.name);
        return true;
    }
}
