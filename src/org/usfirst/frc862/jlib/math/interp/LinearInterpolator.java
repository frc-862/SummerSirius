package org.usfirst.frc862.jlib.math.interp;

/**
 * Interpolator that performs linear interpolation
 */
public class LinearInterpolator implements Interpolator {

	@Override
	public double interpolate(double xLow, double xHigh, double xActual, double yLow, double yHigh) {
	    if (xActual > Math.max(xLow, xHigh)) {
            System.out.printf("xLow %f xActual %f xHigh %f\n", xLow, xActual, xHigh);
	        xActual = Math.max(xLow, xHigh);
	    }
	    if (xActual < Math.min(xLow, xHigh)) {
            System.out.printf("xLow %f xActual %f xHigh %f\n", xLow, xActual, xHigh);
	        xActual = Math.min(xLow, xHigh);
	    }
		
		double pct = getPercent(xLow, xHigh, xActual);
		double distance = yHigh - yLow;
		return yLow + (distance * pct);
	}
	
	private double getPercent(double low, double high, double actual) {
	    if (high == low) 
	        return 1.0;
	    else	    
		    return (actual - low) / (high - low);
	}
	
}
