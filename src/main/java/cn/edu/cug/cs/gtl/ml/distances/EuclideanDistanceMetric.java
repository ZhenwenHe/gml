package cn.edu.cug.cs.gtl.ml.distances;

import cn.edu.cug.cs.gtl.ml.dataset.NumericalData;
import jsat.linear.Vec;


public class EuclideanDistanceMetric<KernelType extends NumericalData> implements DistanceMetric<KernelType>{
    @Override
    public double distance(Object a, Object b) {
        if((a instanceof NumericalData)&&(b instanceof NumericalData)){
            return ((NumericalData)a).pNormDist(2, (NumericalData)b);
        }
        else {
            return ((Vec)a).pNormDist(2, ((Vec)b));
        }
    }

    @Override
    public DistanceMetric<KernelType> clone() {
        return new EuclideanDistanceMetric<KernelType>();
    }
}
