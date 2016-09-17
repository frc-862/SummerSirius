package org.usfirst.frc862.sirius.commands;
import org.usfirst.frc862.sirius.Robot;

public class VisionPivot extends PivotToAngle {
    public VisionPivot() {
        super(0);
    }

    protected void initialize() {
        this.m_angle = Robot.vision.getInterpolatedPivot();
        System.out.println("VisionPivot: " + m_angle);
        super.initialize();
    }
    
    protected boolean isFinished() {
        return (m_angle == 0) || super.isFinished();
}

}
