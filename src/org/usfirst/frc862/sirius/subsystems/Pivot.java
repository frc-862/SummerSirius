// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc862.sirius.subsystems;

import org.usfirst.frc862.jlib.collection.DoubleLookupTable;
import org.usfirst.frc862.jlib.collection.DoubleLookupTable.DoubleValuePair;
import org.usfirst.frc862.jlib.math.interp.Interpolator;
import org.usfirst.frc862.jlib.math.interp.LinearInterpolator;
import org.usfirst.frc862.sirius.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class Pivot extends Subsystem {

    private static final double ANGLE_EPSILON = 1.0;

    class PowerTableValue {
        public double up_power;
        public double down_power;
        public double hold_power;

        public PowerTableValue(double u, double d, double h) {
            up_power = u;
            down_power = d;
            hold_power = h;
        }
    }

    private Interpolator interplator;

    // TODO switch to something that uses a primitive double key so we don't have to create an object for each get
    // Key = angle, value = associated powers
    private DoubleLookupTable<PowerTableValue> powerTable;

    public Pivot() {
        interplator = new LinearInterpolator();
        powerTable = new DoubleLookupTable<>(4);

        // TODO externalize to a file and expose to 
        // smart dashboard -- verify list is always sorted
        powerTable.put(-180.0, new PowerTableValue(-0.3, 0.15, 0.0));
        powerTable.put(0.0, new PowerTableValue(-0.3, 0.15, 0.0));
        powerTable.put(10.0, new PowerTableValue(-0.4, 0.15, -0.25));
        powerTable.put(40.0, new PowerTableValue(-0.5, 0.15, -0.3));
        
    }

    public PowerTableValue getPowerValues(double angle) {
        // find floor/ceiling values
        DoubleValuePair<PowerTableValue> floor = powerTable.floorEntry(angle);
        DoubleValuePair<PowerTableValue> ceil = powerTable.ceilingEntry(angle);
        if(ceil == null) {
            ceil = powerTable.lastEntry();
        }
        if(floor == null) {
            floor = powerTable.firstEntry();
        }

        // Pull angle and values from the ceil and floor
        double floorAngle = floor.getKey();
        PowerTableValue floorValues = floor.getValue();
        double ceilAngle = ceil.getKey();
        PowerTableValue ceilValues = ceil.getValue();

        // interpolate to position
        return new PowerTableValue(
                interplator.interpolate(floorAngle, ceilAngle, angle, 
                        floorValues.up_power, ceilValues.up_power),
                interplator.interpolate(floorAngle, ceilAngle, angle,
                        floorValues.down_power, ceilValues.down_power),
                interplator.interpolate(floorAngle, ceilAngle, angle,
                        floorValues.hold_power, ceilValues.hold_power)
                );
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final Encoder angleEncoder = RobotMap.pivotAngleEncoder;
    private final SpeedController angleMotor = RobotMap.pivotAngleMotor;
    private final DigitalInput hallEffect = RobotMap.pivotHallEffect;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void moveToAngle(double angle) {
        PowerTableValue val = getPowerValues(angle);
        SmartDashboard.putNumber("Going to angle", angle);
        SmartDashboard.putNumber("At angle", getAngle());
        
        if (atAngle(angle)) {
            SmartDashboard.putString("Direction", "hold");
            SmartDashboard.putNumber("Power", val.hold_power);
            angleMotor.set(val.hold_power);
        } else if (angleEncoder.getDistance() > angle) {
            SmartDashboard.putString("Direction", "down");
            SmartDashboard.putNumber("Power", val.down_power);
            angleMotor.set(val.down_power);
        } else {
            SmartDashboard.putString("Direction", "up");
            SmartDashboard.putNumber("Power", val.up_power);
            angleMotor.set(val.up_power);            
        }
    }

    public void hold() {
        PowerTableValue val = getPowerValues(angleEncoder.getDistance());
        angleMotor.set(val.hold_power);
    }

    public boolean atAngle(double intakeAngle) {
        return Math.abs(angleEncoder.getDistance() - intakeAngle) < ANGLE_EPSILON;
    }

    public double getAngle() {
        return angleEncoder.getDistance();
    }

    public void setPower(double v) {
        angleMotor.set(v);
    }

    public void resetAngleEncoder() {
        angleEncoder.reset();
    }
}
