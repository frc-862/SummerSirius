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

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc862.sirius.RobotMap;
import org.usfirst.frc862.sirius.commands.TankDrive;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

/**
 *
 */
public class DriveTrain extends Subsystem {
    public double speed = 1;
    AHRS ahrs;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController leftMotor1 = RobotMap.driveTrainLeftMotor1;
    private final SpeedController leftMotor2 = RobotMap.driveTrainLeftMotor2;
    private final SpeedController rightMotor1 = RobotMap.driveTrainRightMotor1;
    private final SpeedController rightMotor2 = RobotMap.driveTrainRightMotor2;
    private final RobotDrive driveController = RobotMap.driveTrainDriveController;
    private final Encoder leftEncoder = RobotMap.driveTrainLeftEncoder;
    private final Encoder rightEncoder = RobotMap.driveTrainRightEncoder;
    private final AnalogInput frontAmbientLight = RobotMap.driveTrainFrontAmbientLight;
    private final AnalogInput backAmbientLight = RobotMap.driveTrainBackAmbientLight;
    private final DoubleSolenoid shifter = RobotMap.driveTrainShifter;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new TankDrive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        try {
            /* Communicate w/navX MXP via the MXP SPI Bus. */
            /*
             * Alternatively: I2C.Port.kMXP, SerialPort.Port.kMXP or
             * SerialPort.Port.kUSB
             */
            /*
             * See
             * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/
             * for details.
             */
            ahrs = new AHRS(SPI.Port.kMXP);
            ahrs.reset();
        } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
        }

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public RobotDrive getRobotDrive() {
        return driveController;
    }

    public double getYAW() {
        if (ahrs != null) {
            if (ahrs.isCalibrating()) {
                return ahrs.getYaw();
            } else {
                double yaw = ahrs.getFusedHeading();
                if (yaw > 180) {
                    yaw -= 360;
                }
                return yaw;
            }
        } else {
            return 0.0;
        }
    }
    
    public void resetYAW() {
        ahrs.zeroYaw();
    }
    
    public void forwardLeft() {
        leftMotor1.set(speed);
        leftMotor2.set(speed);
    }

    public void forwardRight() {
        rightMotor1.set(speed);
        rightMotor2.set(speed);
    }

    public void backwardLeft() {
        leftMotor1.set(-speed);
        leftMotor2.set(-speed);
    }

    public void backwardRight() {
        rightMotor1.set(-speed);
        rightMotor2.set(-speed);
    }

    public void stop() {
        leftMotor1.set(0.0);
        leftMotor2.set(0.0);
        rightMotor1.set(0.0);
        rightMotor2.set(0.0);
    }

    public void upShift() {
        shifter.set(Value.kForward);
    }

    public void downShift() {
        shifter.set(Value.kReverse);
    }

    public void rotate(double speed) {        
        leftMotor1.set(speed);
        leftMotor2.set(speed);
        rightMotor1.set(speed);
        rightMotor2.set(speed);
    }

    public double getBackAmbientLight() {
        return backAmbientLight.getVoltage();
    }

}
