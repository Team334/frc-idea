package $package.commands;

import $package.subsystems.Collector;
import $package.subsystems.Pivot;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Spit the ball out into the low goal assuming that the robot is in front of
 * it.
 */
public class LowGoal extends CommandGroup {
    public LowGoal() {
        addSequential(new SetPivotSetpoint(Pivot.kLowGoal));
        addSequential(new SetCollectionSpeed(Collector.kReverse));
        addSequential(new ExtendShooter());
    }
}
