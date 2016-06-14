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
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Shooter extends Subsystem {
    private static final double INTAKE_POWER = -0.4;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon rightFly = RobotMap.shooterRightFly;
    private final CANTalon leftFly = RobotMap.shooterLeftFly;
    private final DigitalInput beamBreak = RobotMap.shooterBeamBreak;
    private final DoubleSolenoid kicker = RobotMap.shooterKicker;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void spinUp() {
        rightFly.set(1);
        leftFly.set(1);
    }

    public void spinStop() {
        rightFly.set(0);
        leftFly.set(0);
    }

    public void intake() {
        rightFly.set(INTAKE_POWER);
        leftFly.set(INTAKE_POWER);
    }

    public void kick() {
        kicker.set(Value.kForward);
    }

    public void kickBack() {
        kicker.set(Value.kReverse);
    }

    public boolean hasBoulder() {
        return beamBreak.get();
    }
}
