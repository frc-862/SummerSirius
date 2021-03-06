// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc862.sirius;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc862.sirius.commands.AutomatedShoot;
import org.usfirst.frc862.sirius.commands.DoNothing;
import org.usfirst.frc862.sirius.commands.DriveForNSeconds;
import org.usfirst.frc862.sirius.commands.DriveOverObstacle;
import org.usfirst.frc862.sirius.commands.PivottoHardstop;
import org.usfirst.frc862.sirius.commands.VisionAuton;
import org.usfirst.frc862.sirius.config.Configuration;
import org.usfirst.frc862.sirius.subsystems.Collector;
import org.usfirst.frc862.sirius.subsystems.DriveTrain;
import org.usfirst.frc862.sirius.subsystems.Pivot;
import org.usfirst.frc862.sirius.subsystems.Shooter;
import org.usfirst.frc862.sirius.subsystems.Vision;

import java.io.File;
import java.io.IOException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;
    SendableChooser autoChooser;

    public static OI oi;
    public static Configuration configuration;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static Collector collector;
    public static Shooter shooter;
    public static DriveTrain driveTrain;
    public static Pivot pivot;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static Vision vision;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // Load configuration
        try {
            File configurationFile = new File("/home/lvuser/config.yaml");
            if (!configurationFile.exists()) {
                configurationFile.createNewFile();
                Robot.configuration = new Configuration(true);
                configuration.serialize(configurationFile);
            } else {
                Robot.configuration = Configuration.deserialize(configurationFile);
            }
        } catch (IOException ex) {
            System.err.printf("Failed to load configuration: %s", ex.getMessage());
        }

        RobotMap.init();

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        collector = new Collector();
        shooter = new Shooter();
        driveTrain = new DriveTrain();
        pivot = new Pivot();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        vision = new Vision();

        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period
        autonomousCommand = new VisionAuton();
//        autonomousCommand = new DriveForNSeconds(Robot.configuration.nAutonSeconds);
        
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        autoChooser = new SendableChooser();
        autoChooser.addDefault("Cross and Shoot", new VisionAuton());
        autoChooser.addDefault("Do Nother", new DoNothing());
        autoChooser.addDefault("Cross Defense", new DriveOverObstacle());
        autoChooser.addDefault("Drive for N Seconds", new DriveForNSeconds(Robot.configuration.nAutonSeconds));
    }

    /**
     * This function is called when the disabled button is hit. You can use it
     * to reset subsystems before shutting down.
     */
    public void disabledInit() {

    }

    public void disabledPeriodic() {
        generalPeriodic();
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
//        autonomousCommand = (Command) autoChooser.getSelected();

        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        generalPeriodic();
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null)
            autonomousCommand.cancel();
        Scheduler.getInstance().add(new PivottoHardstop());
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        generalPeriodic();
        
        if (oi.gamepad.getRawAxis(3) > 0.7 || oi.gamepad.getRawAxis(4) > 0.7) {
            Scheduler.getInstance().add(new AutomatedShoot());
        }
        Scheduler.getInstance().run();
    }

    public void testInit() {
        super.testInit();
        driveTrain.stop();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        generalPeriodic();
        LiveWindow.run();
    }

    public void generalPeriodic() {
        SmartDashboard.putNumber("At angle", driveTrain.getYAW());
        Robot.pivot.checkHardStop();
    }
}
