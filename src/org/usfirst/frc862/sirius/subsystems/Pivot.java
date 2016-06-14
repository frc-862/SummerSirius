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

import org.usfirst.frc862.sirius.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Pivot extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController angleMotor = RobotMap.pivotAngleMotor;
    private final Encoder angleEncoder = RobotMap.pivotAngleEncoder;
    private final DigitalInput hallEffect = RobotMap.pivotHallEffect;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    private final static double HARD_STOP = 12.0;
    private final static double UP_POWER = 0.5;
    private final static double DOWN_POWER = -0.1;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void up() {
        angleMotor.set(UP_POWER);
    }

    public void down() {
        angleMotor.set(DOWN_POWER);
    }

    public double position() {
        return angleEncoder.getDistance() + HARD_STOP;
    }

    public void resetAtBottom() {
        if (hallEffect.get()) {
            angleEncoder.reset();
        }
    }
}
