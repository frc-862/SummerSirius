package org.usfirst.frc862.util.math.interp;

/**
 * Interpolator that performs linear interpolation
 */
public class LinearInterpolator implements Interpolator {

	@Override
	public double interpolate(double xLow, double xHigh, double xActual, double yLow, double yHigh) {
		if(xLow > xHigh)
			throw new IllegalArgumentException("xLow must be smaller than or equal to xHigh");
		if(yLow > yHigh)
			throw new IllegalArgumentException("yLow must be smaller than or equal to yHigh");
		if(xActual > xHigh || xActual < xLow)
			throw new IllegalArgumentException("xHigh must be between xLow and xHigh");
		
		double pct = getPercent(xLow, xHigh, xActual);
		double distance = yHigh - yLow;
		return yLow + (distance * pct);
	}
	
	private double getPercent(double low, double high, double actual) {
		return (actual - low) / (high - low);
	}
	
}
