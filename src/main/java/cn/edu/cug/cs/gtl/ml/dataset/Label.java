package cn.edu.cug.cs.gtl.ml.dataset;

import cn.edu.cug.cs.gtl.io.Storable;
import cn.edu.cug.cs.gtl.io.StorableComparable;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class Label implements StorableComparable<Label> {
    int index;
    String name;


    public Label(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public Label(int index) {
        this.index = index;
        this.name = String.valueOf(index);
    }

    public Label(String name) {
        this.index = -1;
        this.name = name;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object clone() {
        return new Label(this.index,this.name);
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

    @Override
    public int compareTo(@NotNull Label label) {
        if(this.index==label.index)
            return 0;
        else if(this.index>label.index)
            return 1;
        else return -1;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Label)) return false;
        Label label = (Label) o;
        return getIndex() == label.getIndex() &&
                Objects.equals(getName(), label.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getName());
    }
}
