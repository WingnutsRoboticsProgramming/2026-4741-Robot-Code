package frc.robot.subsystems;


import java.security.spec.EncodedKeySpec;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.AbsoluteEncoder;

import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import frc.robot.Constants;
import frc.robot.Constants.SubMotorIDs;
import frc.robot.Robot;
import edu.wpi.first.math.controller.PIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;




public class HopperSubsystem extends SubsystemBase {


    // Motor + Encoder
    private final SparkFlex motor;
    private final SparkFlexConfig motorConfig;
    private final SparkClosedLoopController controller;
    private final RelativeEncoder encoder;
    private final AbsoluteEncoder absEncoder;


    // Gear Ratio Setup
    // motorRotations : hopperRotations
    private static final double GEAR_RATIO = 125.0; // CHANGE THIS to your real ratio should be 125:1


    // Choose your units
    private static final boolean USE_DEGREES = true;
    private static final boolean USE_ROTATIONS = false;
    private static final double POSITION_CONVERSION = (USE_DEGREES ? 360.0 / GEAR_RATIO : 1.0 / GEAR_RATIO);
    private static final double VELOCITY_CONVERSION = (USE_DEGREES ? 360.0 / GEAR_RATIO : 1.0 / GEAR_RATIO);


    // Control Variables
    private double targetPosition = 0;
    private double p = 0.4;
    private double i = 0.0;
    private double d = 0.0;


    /** Creates a new HopperSubsystem. */
    public HopperSubsystem() {
        motor = new SparkFlex(Constants.SubMotorIDs.kHopperMotor, MotorType.kBrushless);
        controller = motor.getClosedLoopController();
        encoder = motor.getEncoder();
        absEncoder = motor.getAbsoluteEncoder();
        // Reset relative encoder
        encoder.setPosition(0);

        //  if (USE_ROTATIONS) {

        //    absEncoder.setPositionConversionFactor(360/GEAR_RATIO);
        //  }

        // if (USE_DEGREES) {
        //     absEncoder.setpositionConversionFactor(360.0/GEAR_RATIO);
        //     absEncoder.setvelocityConversionFactor(360.0/GEAR_RATIO);
        // }

        // Motor Configuration
        motorConfig = new SparkFlexConfig();
        motorConfig.idleMode(IdleMode.kBrake);
        motorConfig.inverted(false);

        motorConfig.encoder
            .positionConversionFactor(POSITION_CONVERSION)
            .velocityConversionFactor(VELOCITY_CONVERSION);


        // Configure PID
        motorConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            .p(p)
            .i(i)
            .d(d)
            .outputRange(-1, 1);


        // MAXMotion Setup
        // Replace undefined Constants.MotorSetPoint.* with explicit values to avoid unresolved reference
        motorConfig.closedLoop.maxMotion
            .maxVelocity(5000.0) // example value, adjust to your robot
            .maxAcceleration(100.0) // example value, adjust to your robot
            .allowedClosedLoopError(0.5); // example allowed error


        motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        // Dashboard Tuning defaults
        SmartDashboard.putNumber("HopperManualPosition", 90);
        SmartDashboard.putNumber("HopperP", p);
        SmartDashboard.putNumber("HopperI", i);
        SmartDashboard.putNumber("HopperD", d);
    }


    // Public API


    public void goToPosition(double position) {
        targetPosition = position;
        controller.setReference(position, ControlType.kMAXMotionPositionControl);
        // controller expects native encoder units (rotations), so convert the desired position (degrees or rotations)
        // back into native encoder units before sending the reference
        double nativeSetpoint = position / POSITION_CONVERSION;
        controller.setReference(nativeSetpoint, ControlType.kMAXMotionPositionControl);
    }


    public boolean isAtPosition() {
        double absEncoderPosition = absEncoder.getPosition() * POSITION_CONVERSION;
        return Math.abs(absEncoderPosition - targetPosition) <= 1.0; // MARGIN OF ERROR (in chosen units)
    }


    /** Simple percent-out manual control (useful for whileHeld bindings). */
    public void setPercent(double percent) {
        motor.set(percent);
    }

    /** Stop the motor (convenience). */
    public void stopMotor() {
        motor.stopMotor();
    }

    @Override
    public void periodic() {
        // Telemetry
        SmartDashboard.putNumber("HopperManualPositionRaw", absEncoder.getPosition());
        SmartDashboard.putNumber("HopperManualPosition", absEncoder.getPosition() * POSITION_CONVERSION);
        //SmartDashboard.putNumber("Encoder Value", absEncoder.getDistance());

        // SmartDashboard.putNumber("Encoder Distance", encoder.getDistance());
        // SmartDashboard.putNumber("Encoder Raw Count", encoder.get());

        // SmartDashboard.putNumber("Motor Position (rotations)", position);
        // SmartDashboard.putNumber("Motor Velocity (RPM)", velocity);
        
        double manualPosition = SmartDashboard.getNumber("HopperManualPosition", 90);
        goToPosition(manualPosition);

        // Read PID coefficients from SmartDashboard
        double newP = SmartDashboard.getNumber("HopperP", p);
        double newI = SmartDashboard.getNumber("HopperI", i);
        double newD = SmartDashboard.getNumber("HopperD", d);


        // Update PID values if they have changed
        if (newP != p || newI != i || newD != d) {
            p = newP;
            i = newI;
            d = newD;


            // Update closed loop PID settings
            motorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .p(p)
                .i(i)
                .d(d)
                .outputRange(-1, 1);


            // Reapply the updated configuration
            motor.configure(motorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
            
            // Read manual position from SmartDashboard
    double newManualPosition = SmartDashboard.getNumber("HopperToAngleManualPosition", manualPosition);
    // If the manual position has been changed, update the setpoint
    if (newManualPosition != manualPosition) {
      manualPosition = newManualPosition;
      targetPosition = manualPosition;
      controller.setReference(
          targetPosition,
          ControlType.kMAXMotionPositionControl);

    }
        }
    }
}

