package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import frc.robot.subsystems.OutShooterSubsystem;
import frc.robot.subsystems.InShooterSubsystem;

public class ShootDriveShootAuto extends SequentialCommandGroup {

    public ShootDriveShootAuto(SwerveSubsystem drivebase,
                              OutShooterSubsystem outShooter,
                              InShooterSubsystem inShooter) {

        // Step 1: Shoot for 2 seconds
        Command shootFirst = new ParallelCommandGroup(
            outShooter.runShooterauto(-0.8, 2.0),
            inShooter.runShooterauto(-0.8, 2.0)
        );

        // // Step 2: Drive using YAGSL path helper
        // Command driveCommand = drivebase.followPath("ShootDriveShootAuto"); // <-- NO PathPlanner imports needed

        // // // Step 3: Shoot again
        // // Command shootAgain = new ParallelCommandGroup(
        //     outShooter.runShooterauto(0.8, 2.0),
        //     inShooter.runShooterauto(0.8, 2.0)
        // );

        // Run sequentially
        addCommands(
            shootFirst
           // driveCommand
           // shootAgain
        );
    }
}