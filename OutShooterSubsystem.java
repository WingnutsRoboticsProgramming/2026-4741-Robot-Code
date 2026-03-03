package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;


public class OutShooterSubsystem extends SubsystemBase {
    private final SparkMax m_left1Shooter; // Define the left motor
    private final SparkMaxConfig config_Left = new SparkMaxConfig(); // Create the config for left motor


    private final SparkMax m_right1Shooter; // Define the right motor
    private final SparkMaxConfig config_Right = new SparkMaxConfig(); // Create the config for right motor


    public OutShooterSubsystem() {
        // Set the motors values
        m_left1Shooter = new SparkMax(Constants.SubMotorIDs.kShooterM1Left, MotorType.kBrushless);


        // Set the motor configs
        config_Left
            .idleMode(IdleMode.kBrake)
           .inverted(false)
            ; // Leave this here to make config happy


        // Apply the motor configuration
        m_left1Shooter.configure(config_Left, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        // ------------------------------------------------------------------------------------------------- //
       
        // Set the motors values
        m_right1Shooter = new SparkMax(Constants.SubMotorIDs.kShooterM1Right, MotorType.kBrushless);
   
        // Set the motor configs
        config_Right
            .idleMode(IdleMode.kBrake)
            .inverted(true)
            ;
            
             // Leave this here to make config happy

             // Leave this here to make config happy


        // Apply the motor configuration
        m_right1Shooter.configure(config_Right, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

 /** Set both shooter motors to a percent output (-1.0 .. 1.0). */
    public void setPercent(double percent) {
        m_left1Shooter.set(percent);
        m_right1Shooter.set(percent);
    }

    // Create the main run command
    // Set the motors to the double speed
    public Command runShooter(double speed) {
        return(run(() -> {
            m_left1Shooter.set(speed);
            m_right1Shooter.set(speed);
        }));
    }


    // Create the stop command to well... Stop the motors
    public Command stopShooter() {
        return(run(() -> {
            m_left1Shooter.set(0);
            m_right1Shooter.set(0);
        }));
    }

    //    public Command runShooterauto(double speed, double duration) {
    //     return(run(() -> {
    //         m_left1Shooter.set(speed);
    //         m_right1Shooter.set(speed);
    //     }));
    // }
    public Command runShooterauto(double speed, double seconds) {
    return Commands.run(() -> setPercent(speed), this)
            .withTimeout(seconds)
            .andThen(stopShooter());
}


    // Create the stop command to well... Stop the motors
    public Command stopShooterauto() {
        return(run(() -> {
            m_left1Shooter.set(0);
            m_right1Shooter.set(0);
        }));
    }


//     // Run shooter at a speed (0.0-1.0)
// public Command runShooter(double speed, double durationSeconds) {
//     return new InstantCommand(() -> setPercent(speed))
//            .andThen(new WaitCommand(durationSeconds))
//            .andThen(stopShooter());
// }

// // Stop the shooter
// public Command stopShooter() {
//     return run(() -> setPercent(0));
// }
    public Object OutShooterMotorSpeed(int i, int j) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'OutShooterMotorSpeed'");
    }

    // public void runForRotations(int i, double d) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'runForRotations'");
    // }

    public boolean isFinished() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFinished'");
    }
}
