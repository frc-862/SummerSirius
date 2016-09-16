package org.usfirst.frc862.sirius.commands;
import org.usfirst.frc862.sirius.Robot;

public class VisionPivot extends PivotToAngle {
    public VisionPivot() {
        super(0);
        requires(Robot.pivot);
    }

    protected void initialize() {
        this.m_angle = Robot.vision.getInterpolatedPivot();
    }
}
