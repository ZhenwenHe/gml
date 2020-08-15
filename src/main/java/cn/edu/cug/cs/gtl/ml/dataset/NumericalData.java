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

}
