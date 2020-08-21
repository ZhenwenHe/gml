package cn.edu.cug.cs.gtl.ml.dataset;


import jsat.linear.IndexValue;
import jsat.linear.Matrix;
import jsat.linear.Vec;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.*;

public class Vector extends NumericalData {

    private static final long serialVersionUID = 5549589219061373839L;
    protected double[] array;
    protected int startIndex;
    protected int endIndex;

    /**
     * Creates a new Dense Vector of zeros
     *
     * @param length the length of the vector
     */
    public Vector(int length) {
        if (length < 0)
            throw new ArithmeticException("You can not have a negative dimension vector");
        array = new double[length];
        startIndex = 0;
        endIndex = array.length;
    }

    public Vector() {
        array = null;
        startIndex = 0;
        endIndex = 0;
    }

    /**
     * Creates a new vector of the length of the given list, and values copied
     * over in order.
     *
     * @param list the list of values to copy into a new vector
     */
    public Vector(List<Double> list) {
        this.array = new double[list.size()];
        for (int i = 0; i < list.size(); i++)
            this.array[i] = list.get(i);
        startIndex = 0;
        endIndex = this.array.length;
    }

    /**
     * Creates a new Dense Vector that uses the given array as its values. Its
     * values will not be copied, and raw access and mutations tot he given
     * array may occur.
     *
     * @param array the backing array to use for a new vector of the same length
     */
    public Vector(double[] array) {
        this(array, 0, array.length);
    }

    /**
     * Creates a new Dense Vector that uses the given array as its values. Its
     * values will not be copied, and raw access and mutations tot he given
     * array may occur.
     *
     * @param array the backing array to use for a new vector
     * @param start the first index in the array, inclusive, to mark the start
     *              of the vector.
     * @param end   the last index in the array, exclusive, to mark the end of the
     *              vector.
     */
    public Vector(double[] array, int start, int end) {
        this.array = array;
        this.startIndex = start;
        this.endIndex = end;
    }

    /**
     * Creates a new Dense Vector that contains a copy of the values in the
     * given vector
     *
     * @param toCopy the vector to copy
     */
    public Vector(Vec toCopy) {
        this(toCopy.length());
        for (IndexValue iv : toCopy)
            set(iv.getIndex(), iv.getValue());
    }

    @Override
    public int length() {
        return (endIndex - startIndex);
    }

    @Override
    public double get(int index) {
        return array[index + startIndex];
    }

    @Override
    public void set(int index, double val) {
        array[index + startIndex] = val;
    }

    @Override
    public double min() {
        double result = array[startIndex];
        for (int i = startIndex + 1; i < endIndex; i++)
            result = Math.min(result, array[i]);

        return result;
    }

    @Override
    public double max() {
        double result = array[startIndex];
        for (int i = startIndex + 1; i < endIndex; i++)
            result = Math.max(result, array[i]);

        return result;
    }

    @Override
    public double sum() {
        /*
         * Uses Kahan summation algorithm, which is more accurate then
         * naively summing the values in floating point. Though it
         * does not guarenty the best possible accuracy
         *
         * See: http://en.wikipedia.org/wiki/Kahan_summation_algorithm
         */

        double sum = 0;
        double c = 0;
        for (int i = startIndex; i < endIndex; i++) {
            double y = array[i] - c;
            double t = sum + y;
            c = (t - sum) - y;
            sum = t;
        }

        return sum;
    }

    @Override
    public double median() {
        double[] copy = Arrays.copyOfRange(array, startIndex, endIndex);

        Arrays.sort(copy);

        if (copy.length % 2 == 1)
            return copy[copy.length / 2];
        else
            return copy[copy.length / 2] / 2 + copy[copy.length / 2 + 1] / 2;//Divisions by 2 then add is more numericaly stable
    }

    @Override
    public double skewness() {
        double mean = mean();

        double tmp = 0;

        for (int i = startIndex; i < endIndex; i++)
            tmp += pow(array[i] - mean, 3);

        double s1 = tmp / (pow(standardDeviation(), 3) * (array.length - 1));

        if (array.length >= 3)//We can use the bias corrected formula
            return sqrt(array.length * (array.length - 1)) / (array.length - 2) * s1;

        return s1;
    }

    @Override
    public double kurtosis() {
        double mean = mean();

        double tmp = 0;

        for (int i = startIndex; i < endIndex; i++)
            tmp += pow(array[i] - mean, 4);
        tmp /= length();

        return tmp / pow(variance(), 2) - 3;
    }


    @Override
    public jsat.linear.DenseVector sortedCopy() {
        double[] copy = Arrays.copyOfRange(array, startIndex, endIndex);

        Arrays.sort(copy);

        return new jsat.linear.DenseVector(copy);
    }

    @Override
    public double variance() {
        double mu = mean();
        double tmp = 0;

        double N = length();


        for (int i = startIndex; i < endIndex; i++)
            tmp += pow(array[i] - mu, 2) / N;

        return tmp;
    }

    @Override
    public double dot(Vec v) {
        if (this.length() != v.length())
            throw new ArithmeticException("Vectors must have the same length");

        if (v.isSparse())
            return v.dot(this);

        double dot = 0;
        for (int i = startIndex; i < endIndex; i++)
            dot += array[i] * v.get(i - startIndex);

        return dot;
    }

    public Vector deepCopy() {
        return new Vector(Arrays.copyOf(array, array.length));
    }

    @Override
    public void multiply(double c, Matrix A, Vec b) {
        if (this.length() != A.rows())
            throw new ArithmeticException("Vector x Matrix dimensions do not agree [1," + this.length() + "] x [" + A.rows() + ", " + A.cols() + "]");
        if (b.length() != A.cols())
            throw new ArithmeticException("Destination vector is not the right size");

        for (int i = 0; i < this.length(); i++) {
            double this_i = c * this.array[i + this.startIndex];
            for (int j = 0; j < A.cols(); j++)
                b.increment(j, this_i * A.get(i, j));
        }
    }

    @Override
    public void mutableAdd(double c) {
        for (int i = startIndex; i < endIndex; i++)
            array[i] += c;
    }

    @Override
    public void mutableAdd(double c, Vec b) {
        if (this.length() != b.length())
            throw new ArithmeticException("Can not add vectors of unequal length");

        if (b.isSparse())
            for (IndexValue iv : b)
                array[iv.getIndex()] += c * iv.getValue();
        else
            for (int i = startIndex; i < endIndex; i++)
                array[i] += c * b.get(i);
    }

    @Override
    public void mutableSubtract(double c) {
        for (int i = startIndex; i < endIndex; i++)
            array[i] -= c;
    }

    @Override
    public void mutableMultiply(double c) {
        for (int i = startIndex; i < endIndex; i++)
            array[i] *= c;
    }

    @Override
    public void mutableDivide(double c) {
        for (int i = startIndex; i < endIndex; i++)
            array[i] /= c;
    }

    @Override
    public double pNormDist(double p, Vec y) {
        if (this.length() != y.length())
            throw new ArithmeticException("Vectors must be of the same length");

        double norm = 0;
        if (y.isSparse()) {
            int lastIndx = -1;
            for (IndexValue iv : y) {
                for (int i = lastIndx + 1; i < iv.getIndex(); i++)//add all the indecies we skipped
                    norm += Math.pow(Math.abs(array[i]), p);
                lastIndx = iv.getIndex();
                //add current
                norm += Math.pow(Math.abs(array[iv.getIndex()] - iv.getValue()), p);
            }

            //Tailing zeros
            for (int i = lastIndx + 1; i < y.length(); i++)
                norm += Math.pow(Math.abs(array[i]), p);
        } else {
            for (int i = startIndex; i < endIndex; i++)
                norm += Math.pow(Math.abs(array[i] - y.get(i)), p);
        }
        return Math.pow(norm, 1.0 / p);
    }

    @Override
    public double pNorm(double p) {
        if (p <= 0)
            throw new IllegalArgumentException("norm must be a positive value, not " + p);
        double result = 0;
        if (p == 1) {
            for (int i = startIndex; i < endIndex; i++)
                result += abs(array[i]);
        } else if (p == 2) {
            for (int i = startIndex; i < endIndex; i++)
                result += array[i] * array[i];
            result = Math.sqrt(result);
        } else if (Double.isInfinite(p)) {
            for (int i = startIndex; i < endIndex; i++)
                result = Math.max(result, abs(array[i]));
        } else {
            for (int i = startIndex; i < endIndex; i++)
                result += Math.pow(Math.abs(array[i]), p);
            result = pow(result, 1 / p);
        }
        return result;
    }

    @Override
    public Vec clone() {
        Vector copy = new Vector(length());

        System.arraycopy(this.array, startIndex, copy.array, 0, length());

        return copy;
    }

    @Override
    public void reset(double[] array) {
        this.endIndex = array.length;
        this.startIndex = 0;
        this.array = array;
    }

    @Override
    public boolean load(DataInput dataInput) throws IOException {
        startIndex = dataInput.readInt();
        endIndex = dataInput.readInt();
        double[] array = new double[endIndex - startIndex];
        for (int i = startIndex; i < endIndex; ++i)
            array[i] = dataInput.readDouble();
        return true;
    }

    @Override
    public boolean store(DataOutput dataOutput) throws IOException {
//        double[] array
//        int startIndex
//        int endIndex
        dataOutput.writeInt(startIndex);
        dataOutput.writeInt(endIndex);
        for (double d : array)
            dataOutput.writeDouble(d);
        return true;
    }

    @Override
    public void normalize() {
        double sum = 0;

        for (int i = startIndex; i < endIndex; i++)
            sum += array[i] * array[i];

        sum = Math.sqrt(sum);

        mutableDivide(Math.max(sum, 1e-10));
    }

    @Override
    public void mutablePairwiseMultiply(Vec b) {
        if (this.length() != b.length())
            throw new ArithmeticException("Vectors must have the same length");
        for (int i = startIndex; i < endIndex; i++)
            this.array[i] *= b.get(i);
    }

    @Override
    public void mutablePairwiseDivide(Vec b) {
        if (this.length() != b.length())
            throw new ArithmeticException("Vectors must have the same length");
        for (int i = startIndex; i < endIndex; i++)
            this.array[i] /= b.get(i);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec))
            return false;
        Vec otherVec = (Vec) obj;

        if (this.length() != otherVec.length())
            return false;
        for (int i = startIndex; i < endIndex; i++)
            if (this.get(i) != otherVec.get(i))
                if (Double.isNaN(this.get(i)) && Double.isNaN(otherVec.get(i)))//NaN != NaN is always true, so check special
                    return true;
                else
                    return false;

        return true;
    }

    @Override
    public boolean equals(Object obj, double range) {
        if (!(obj instanceof Vec))
            return false;
        Vec otherVec = (Vec) obj;
        range = Math.abs(range);

        if (this.length() != otherVec.length())
            return false;
        for (int i = startIndex; i < endIndex; i++)
            if (Math.abs(this.get(i) - otherVec.get(i)) > range)
                if (Double.isNaN(this.get(i)) && Double.isNaN(otherVec.get(i)))//NaN != NaN is always true, so check special
                    return true;
                else
                    return false;

        return true;
    }

    /**
     * Returns a new dense vector backed by the given array. This is a weak
     * reference, the given array should no longer be altered - as it will
     * effect the values of the dense vector.
     *
     * @param array the array to use as the backing of a dense vector
     * @return a Dense Vector that is backed using the given array
     */
    public static Vector of(double... array) {
        return new Vector(array);
    }

    @Override
    public double[] arrayCopy() {
        return Arrays.copyOfRange(array, startIndex, endIndex);
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public void setLength(int length) {
        if (length < 0)
            throw new ArithmeticException("Can not create an array of negative length");
        if (length > length()) {
            if (length < length())//make sure we aren't destroying anything
                for (int i = length() - length; i < length(); i++)
                    if (get(i) != 0)
                        throw new RuntimeException("Can't decrease the length of this vector from " + length() + " to " + length + " due to non-zero value");
            array = Arrays.copyOf(array, startIndex + length);
            endIndex = startIndex + length;
        }
    }

    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        this.array = new double[in.readInt()];
        this.startIndex = 0;
        this.endIndex = this.array.length;
        for (int i = 0; i < this.length(); i++)
            this.array[i] = in.readDouble();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.length());
        for (int i = 0; i < this.length(); i++)
            out.writeDouble(this.get(i));
    }

    @Override
    public String toString() {
        return "Vector{" +
                "array=" + Arrays.toString(array) +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), startIndex, endIndex);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }
}
