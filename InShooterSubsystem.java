package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;


import edu.wpi.first.math.Pair;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class InShooterSubsystem extends SubsystemBase {
    private final SparkMax m_left2Shooter; // Define the left motor
    private final SparkMaxConfig config_left2shooter = new SparkMaxConfig(); // Create the config for left motor


    private final SparkMax m_right2Shooter; // Define the right motor
    private final SparkMaxConfig config_right2Shooter = new SparkMaxConfig(); // Create the config for right motor


    public InShooterSubsystem() {
        // Set the motors values
        m_left2Shooter = new SparkMax(Constants.SubMotorIDs.kShooterM2Left, MotorType.kBrushless);


        // Set the motor configs
        config_left2shooter
            .idleMode(IdleMode.kBrake)
            .inverted(true)
            ; // Leave this here to make config happy

        // Apply the motor configuration
        m_left2Shooter.configure(config_left2shooter, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        // ------------------------------------------------------------------------------------------------- //
       
        // Set the motors values
        m_right2Shooter = new SparkMax(Constants.SubMotorIDs.kShooterM2Right, MotorType.kBrushless);
   
        // Set the motor configs
        config_right2Shooter
            .idleMode(IdleMode.kBrake)
            .inverted(true)
            ; // Leave this here to make config happy


        // Apply the motor configuration
        m_right2Shooter.configure(config_right2Shooter, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

 /** Set both shooter motors to a percent output (-1.0 .. 1.0). */
    public void setPercent(double percent) {
        m_left2Shooter.set(percent);
        m_right2Shooter.set(percent);
    }

    // Create the main run command
    // Set the motors to the double speed
    public Command runShooter(double speed) {
        return(run(() -> {
            m_left2Shooter.set(speed);
            m_right2Shooter.set(speed);
        }));
    }


    // Create the stop command to well... Stop the motors
    public Command stopShooter() {
        return(run(() -> {
            m_left2Shooter.set(0);
            m_right2Shooter.set(0);
        }));
    }

    //    public Command runShooterauto(double speed, double duration) {
    //     return(run(() -> {
    //         m_left2Shooter.set(speed);
    //         m_right2Shooter.set(speed);
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
            m_left2Shooter.set(0);
            m_right2Shooter.set(0);
        }));
    }

    public Object InShooterMotorSpeed(int i, double d) {
    //     // TODO Auto-generated method stub
         throw new UnsupportedOperationException("Unimplemented method 'InShooterMotorSpeed'");
     }

    // public void runForRotations(int i, double d) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'runForRotations'");
    // }

     public boolean isFinished() {
    //     // TODO Auto-generated method stub
         throw new UnsupportedOperationException("Unimplemented method 'isFinished'");
     }
}
