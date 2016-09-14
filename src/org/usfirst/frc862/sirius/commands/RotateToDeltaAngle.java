// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc862.sirius.commands;

import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc862.sirius.Robot;
import org.usfirst.frc862.sirius.RobotMap;

/**
 *
 */
public class RotateToDeltaAngle extends PIDCommand {
    private static final double TOLERANCE = 2.0;
    double deltaAngle;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public RotateToDeltaAngle(double angle) {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("RotatetoDeltaAngle", 0.01, 0.0, 0.0, 0.02);
        getPIDController().setContinuous(true);
        getPIDController().setAbsoluteTolerance(2.0);
        getPIDController().setInputRange(-180.0, 180.0);
        getPIDController().setOutputRange(-1.0, 1.0);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        requires(Robot.driveTrain);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        
        // TODO externalize into config file
        getPIDController().setPID(0.15, 0, 0);
        getPIDController().setToleranceBuffer(3);
        getPIDController().setAbsoluteTolerance(TOLERANCE);
        
        deltaAngle = angle;
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;

        /*
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return RobotMap.driveTrainLeftEncoder.pidGet();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
         * 
         */
        
        return Robot.driveTrain.getYAW();
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);

        /*
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        RobotMap.driveTrainLeftMotor1.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
         * 
         */
        
//        final double FLOOR_POWER = 0.3;
//        final double PID_EPSILON = getPIDController().getP() * TOLERANCE;
//        
//        if (Math.abs(output) < 0.01) {
//            output = 0;
//        } if (Math.abs(output) < FLOOR_POWER) {
//            output = Math.signum(output) * FLOOR_POWER;
//        }
        
        SmartDashboard.putNumber("PID Angle Output", output);
        Robot.driveTrain.rotate(-output);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Robot.driveTrain.resetYAW();
        getPIDController().setSetpoint(deltaAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        SmartDashboard.putNumber("PID set point", getPIDController().getSetpoint());
        SmartDashboard.putNumber("PID error", getPIDController().getError());
        return getPIDController().onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
