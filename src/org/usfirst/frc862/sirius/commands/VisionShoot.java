package org.usfirst.frc862.sirius.commands;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class VisionShoot extends CommandGroup {
    public VisionShoot() {
        addSequential(new VisionPivot());
        addSequential(new AutomatedShoot());
        addSequential(new PivottoHardstop());
    } 
}
