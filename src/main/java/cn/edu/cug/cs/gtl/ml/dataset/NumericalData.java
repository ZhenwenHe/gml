package cn.edu.cug.cs.gtl.ml.dataset;

import jsat.linear.Vec;
/**
 * Numerical data 具有实际测量bai的物理意义，比如人的身高、体重、duIQ、血压等zhi等，统计学中，Numerical data也称作quantitative data，Numerical data又分为两种类型：dao
 *
 * 1、离散型数据（Discrete data）代表数量是可以被数出来的，它可能是有限的，也可能是无限的。比如掷硬币100次人头朝上的次数（次数范围为0到100，是有限的）；又如，掷硬币直到有100次是人头朝上的次数（次数范围为100到无穷大，是无限的）。
 *
 * 2、连续数据代表测量的结果是不能被数出来的，它只能被区间所描述。比如桶里有20L水，随机倒掉一部分，剩余的水量为[0,20]区间内的某一个值，9.4L，9.41L，9.416789L等等，任何在[0,20]区间内的值都有可能。
 *
 * Categorical data代表了被描述对象的性质，比如一个人的性别、婚姻状况、家乡等等， Categorical data 可以用Numerical data来表示，比如说描述性别时，1代表男，2代表女，但是这些数据并没有数学意义，你不能拿他做运算。Categorical data也叫作qualitative data或是Yes/No data。
 */
public abstract class NumericalData extends Vec {
    /**
     * Returns the length of this vector
     *
     * @return the length of this vector
     */
    @Override
    public int length() {
        return 0;
    }

    /**
     * Gets the value stored at a specific index in the vector
     *
     * @param index the index to access
     * @return the double value in the vector
     * @throws IndexOutOfBoundsException if the index given is greater than or
     *                                   equal to its {@link #length() }
     */
    @Override
    public double get(int index) {
        return 0;
    }

    /**
     * Sets the value stored at a specified index in the vector
     *
     * @param index the index to access
     * @param val   the value to store in the index
     * @throws IndexOutOfBoundsException if the index given is greater than or
     *                                   equal to its {@link #length() }
     */
    @Override
    public void set(int index, double val) {

    }

    /**
     * Indicates whether or not this vector is optimized for sparce computation,
     * meaning that most values in the vector are zero - and considered
     * implicit. Only non-zero values are stored.
     *
     * @return <tt>true</tt> if the vector is sparce, <tt>false</tt> otherwise.
     */
    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public NumericalData clone() {
        return null;
    }

    /**
     * Changes the length of the given vector. The length can always be
     * extended. The length can be reduced down to the size of the largest non
     * zero element.<br>
     * <br>
     * NOTE: this function is not mandatory. Vectors that are views or implicit
     * transformations of another vector are unlikely to support this
     * functionality. Vectors that are base level representations should support
     * this function.
     *
     * @param length the new length for this vector
     */
    @Override
    public void setLength(int length) {

    }
}
