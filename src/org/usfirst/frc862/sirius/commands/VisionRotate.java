package org.usfirst.frc862.sirius.commands;

import org.usfirst.frc862.sirius.Robot;

public class VisionRotate extends RotateToDeltaAngle {
    static long lastTimeStamp = 0;
    
    public VisionRotate() {
        super(0);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        long newTimestamp = Robot.vision.getTimestamp();
        
        if (newTimestamp == lastTimeStamp) {
            this.deltaAngle = 0;
        } else {
            lastTimeStamp = newTimestamp;
            this.deltaAngle = -Robot.vision.getThetaToTarget();
        }
        super.initialize();

        System.out.println("\nVision Rotate: " + this.deltaAngle);
    }
}
