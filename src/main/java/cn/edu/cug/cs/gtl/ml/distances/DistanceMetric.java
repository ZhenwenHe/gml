package cn.edu.cug.cs.gtl.ml.distances;

import cn.edu.cug.cs.gtl.array.Array;
import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import cn.edu.cug.cs.gtl.ml.dataset.Vector;
import jsat.linear.*;

import java.util.List;

public interface DistanceMetric<KernelType extends NumericalData> extends jsat.linear.distancemetrics.DistanceMetric{
    double distance(Object a, Object b);

    /**
     * Computes the distance between 2 vectors.
     * The smaller the value, the closer, and there for,
     * more similar, the vectors are. 0 indicates the vectors are the same.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the distance between them
     */
    default double dist(Vec a, Vec b){
        Object ao=null;
        Object bo=null;

        if((a instanceof SubVector)||(b instanceof SubVector)) {
            try {
                throw new Exception("SubVector can not be used in DistanceMetric<KernelType extends NumericalData>");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


        if(a instanceof VecPaired || a instanceof VecPairedComparable)
            ao=(Object)((VecPaired)a).getVector();
        else if(a instanceof VecWithNorm)
            ao = (Object)((VecWithNorm)a).getBase();
        else if(a instanceof DenseVector || a instanceof SparseVector)
            ao = new Vector(b.arrayCopy());
        else
            ao=a;


        if(b instanceof VecPaired || b instanceof VecPairedComparable)
            bo=(Object)((VecPaired)b).getVector();
        else if(b instanceof VecWithNorm)
            bo = (Object)((VecWithNorm)b).getBase();
        else if(b instanceof DenseVector|| b instanceof SparseVector)
            bo = new Vector(b.arrayCopy());
        else
            bo=b;

        return distance(ao,bo);
    }
    /**
     * 计算两个数据集合中每条时序数据对象之间的距离
     *
     * @param a m条时序数据的集合
     * @param b n条时序数据的集合
     * @return 返回n行m列的2D数组 a
     * 也即，s1中的第0条与s2中的n条时序数据的距离存储在第0列；
     * s1中的第i条与s2中的第j条时序数据之间的距离为 a.get(j,i);
     * 获取s1中第i条与s2中所有时序数据对象的距离为一个n元列向量，也即 a.col(i)
     */
    default Array distances(List<KernelType> a, List<KernelType> b) {
        int m = a.size();
        int n = b.size();
        double[] dist = new double[m * n];
        int k = 0;
        for (int i = 0; i < m; ++i) {
            KernelType s11 = a.get(i);
            for (int j = 0; j < n; ++j) {
                KernelType s22 = b.get(j);
                dist[k] = distance(s11, s22);
                ++k;
            }
        }
        return Array.of(n, m, dist);
    }

    /**
     * 计算两个数据集合中每条时序数据对象之间的距离
     *
     * @param a m条时序数据的集合
     * @param b n条时序数据的集合
     * @return 返回n行m列的2D数组 a
     * 也即，s1中的第0条与s2中的n条时序数据的距离存储在第0列；
     * s1中的第i条与s2中的第j条时序数据之间的距离为 a.get(j,i);
     * 获取s1中第i条与s2中所有时序数据对象的距离为一个n元列向量，也即 a.col(i)
     */
    default Array distances(KernelType[] a, KernelType[] b) {
        int m = a.length;
        int n = b.length;
        double[] dist = new double[m * n];
        int k = 0;
        for (int i = 0; i < m; ++i) {
            KernelType s11 = a[i];
            for (int j = 0; j < n; ++j) {
                KernelType s22 = b[j];
                dist[k] = distance(s11, s22);
                ++k;
            }
        }
        return Array.of(n, m, dist);
    }

    default boolean isSymmetric()
    {
        return true;
    }


    default boolean isSubadditive()
    {
        return true;
    }

    default boolean isIndiscemible()
    {
        return true;
    }

    default double metricBound()
    {
        return Double.POSITIVE_INFINITY;
    }


    default DistanceMetric<KernelType> clone(){
        return null;
    }
}
