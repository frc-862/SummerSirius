package org.usfirst.frc862.jlib.math.interp;

public interface Interpolator {
	/**
	 * Interpolate a value
	 * 
	 * @param xLow Low source value, must be less than or equal to xHigh
	 * @param xHigh High source value, must be greater than or equal to xLow
	 * @param xActual Actual source value, should be between xLow and xHigh
	 * @param yLow Low result value, must be less than or equal to yHigh
	 * @param yHigh High result value, must be greater than or equal to yLow
	 * @return Estimated result value for xActual
	 */
	double interpolate(double xLow, double xHigh, double xActual, 
			double yLow, double yHigh);
}
