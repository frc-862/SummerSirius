package org.usfirst.frc862.jlib.math.interp;

import org.usfirst.frc862.jlib.collection.DoubleLookupTable;

import java.util.Arrays;

public class Interpolate {

    /**
     * Creates an array of doubles linearly interpolated from the ceiling and floor of the specified key
     *
     * @param lookupTable Lookup table to get values from
     * @param key Key to get values for
     * @return Interpolated values
     */
    // TODO unit test
    public static double[] interpolate(DoubleLookupTable<double[]> lookupTable, Interpolator interpolator, double key) {
        DoubleLookupTable<double[]>.DoubleValuePair floor = lookupTable.floorEntry(key);
        if(floor == null)
            floor = lookupTable.firstEntry();

        if(floor.getKey() == key) { // Exact value was in the table
            // copyOf so changes by the caller don't break stuff in the table
            return Arrays.copyOf(floor.getValue(), floor.getValue().length);
        }

        DoubleLookupTable<double[]>.DoubleValuePair ceil = lookupTable.ceilingEntry(key);
        if(ceil == null)
            ceil = lookupTable.lastEntry();

        double[] f = floor.getValue();
        double[] c = ceil.getValue();
        double[] interpolated = new double[f.length];
        for(int i = 0; i < interpolated.length; ++i) {
            interpolated[i] = interpolator.interpolate(floor.getKey(), ceil.getKey(), key, f[i], c[i]);
        }

        return interpolated;
    }


}
