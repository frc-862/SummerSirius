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

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc862.sirius.RobotMap;
import org.usfirst.frc862.sirius.commands.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Pivot extends Subsystem {

    private static final double ANGLE_EPSILON = 1.0;

    class PowerTableValue {
        public double angle;
        public double up_power;
        public double down_power;
        public double hold_power;
        
        public PowerTableValue(double a, double u, double d, double h) {
            angle = a;
            up_power = u;
            down_power = d;
            hold_power = h;
        }
    }
    
    public List<PowerTableValue> powerTable;
    
    public Pivot() {
        powerTable = new ArrayList<>();
        
        // TODO externalize to a file and expose to 
        // smart dashboard -- verify list is always sorted
        powerTable.add(new PowerTableValue(0.0, -0.3, 0.1, -0.2));
        powerTable.add(new PowerTableValue(10.0, -0.4, 0.1, -0.25));
        powerTable.add(new PowerTableValue(40.0, -0.5, 0.1, -0.3));
    }
    
    public PowerTableValue getPowerValues(double angle) {
        // find floor/ceiling values
        PowerTableValue floor = powerTable.get(0);
        PowerTableValue ceil = powerTable.get(1);
        
        for (int i = 0; i < powerTable.size(); ++i) {
            if (angle < powerTable.get(i).angle) {
                floor = powerTable.get(i - 1);
                ceil = powerTable.get(i);
                break;
            }
        }
             
        // find position between
        double distance = ceil.angle - floor.angle;
        double percent = (angle - floor.angle) / distance;
        
        // interpolate to position
        return new PowerTableValue(angle,
                interpolate(percent, floor.up_power, ceil.up_power),
                interpolate(percent, floor.down_power, ceil.down_power),
                interpolate(percent, floor.hold_power, ceil.hold_power)
                );
    }
    
    public double interpolate(double dist, double low, double high) {
        return (high - low) * dist + low;
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
        if (atAngle(angle)) {
            angleMotor.set(val.hold_power);
        } else if (angleEncoder.getDistance() > angle) {
            angleMotor.set(val.down_power);
        } else {
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
}

