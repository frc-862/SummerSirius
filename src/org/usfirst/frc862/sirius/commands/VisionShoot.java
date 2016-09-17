package org.usfirst.frc862.sirius.commands;
import org.usfirst.frc862.sirius.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class VisionShoot extends CommandGroup {
    public VisionShoot() {
        addSequential(new VisionPivot());
        addSequential(new AutomatedShoot());
        addSequential(new PivottoHardstop());
    } 

    protected boolean isFinished() {
        return (Robot.vision.getInterpolatedPivot() == 0) || super.isFinished();
    }

}
