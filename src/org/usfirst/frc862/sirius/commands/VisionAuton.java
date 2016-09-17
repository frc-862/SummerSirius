package org.usfirst.frc862.sirius.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc862.sirius.Robot;
import org.usfirst.frc862.sirius.subsystems.*;

public class VisionAuton extends CommandGroup {

    public VisionAuton() {
        addSequential(new RaiseArm());
        addSequential(new DriveForNSeconds(Robot.configuration.nAutonSeconds));
        addSequential(new LowerArm());
        addSequential(new WaitForVision());
        addSequential(new VisionRotate());
        addSequential(new VisionShoot());
    } 
}
