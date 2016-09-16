package org.usfirst.frc862.sirius.commands;

import org.usfirst.frc862.sirius.Robot;

public class VisionRotate extends RotateToDeltaAngle {
    public VisionRotate() {
        super(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        this.deltaAngle = Robot.vision.getThetaToTarget();
        requires(Robot.driveTrain);
    }
}
