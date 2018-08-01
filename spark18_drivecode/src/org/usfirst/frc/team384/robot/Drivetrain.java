package org.usfirst.frc.team384.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain {
	private WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(12);	// THIS IS A RIGHT MOTOR MUST BE INVERSED
	private WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(6);	// originally was rear right
	private WPI_TalonSRX rearRightMotor = new WPI_TalonSRX(8);		// originally was front left, THIS IS A RIGHT MOTOR MUST BE INVERTED
	private WPI_TalonSRX rearLeftMotor = new WPI_TalonSRX(2);		// -THIS IS A LEFT MOTOR, MUST BE INVERSED
	
	private SpeedControllerGroup leftDrivetrain;
	private SpeedControllerGroup rightDrivetrain;
	
	private DifferentialDrive diffDrive;
	
	private Encoder rightEncoder = new Encoder(1, 2, false, CounterBase.EncodingType.k1X);
	private Encoder leftEncoder = new Encoder(4, 5, false, CounterBase.EncodingType.k2X);
	
	double leftStickValue = 0.0;			
	double rightStickValue = 0.0;
	
	private final double DRIVE_ENC_PPR = 512;
	private final double DRIVE_DIST_PER_PULSE = 3.75 * Math.PI / DRIVE_ENC_PPR;
	
	public Drivetrain() {
		// i dont think this is necessary
		frontRightMotor.setSafetyEnabled(false);
		frontLeftMotor.setSafetyEnabled(false);
		rearRightMotor.setSafetyEnabled(false);
		rearLeftMotor.setSafetyEnabled(false);
		
		frontLeftMotor.setInverted(true);
		rearLeftMotor.setInverted(true);
		frontRightMotor.setInverted(true);
		rearRightMotor.setInverted(true);
		
		rearRightMotor.follow(frontRightMotor);
		rightDrivetrain = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
		
		rearLeftMotor.follow(frontLeftMotor);
		leftDrivetrain = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
		
		// moved this to the constructor, must invert first
		diffDrive = new DifferentialDrive(leftDrivetrain, rightDrivetrain);	// was using front right
	}
	
	public void initializeEncoders()	{
		rearLeftMotor.getSensorCollection().setQuadraturePosition(0, 0);
		rearRightMotor.getSensorCollection().setQuadraturePosition(0, 0);
		
		leftEncoder.setDistancePerPulse(DRIVE_DIST_PER_PULSE);
		rightEncoder.setDistancePerPulse(DRIVE_DIST_PER_PULSE);
		leftEncoder.reset();
		rightEncoder.reset();
	}

	/*
	 * Get encoder position
	 * 0 = left side drive rail
	 * 1 = right side drive rail
	 */
	public int getEncoderPosition(int side)	{
		int result;
		
		if(side == 0)	{
			result = rearLeftMotor.getSelectedSensorPosition(0);
			result = leftEncoder.getRaw();
		} else if (side == 1)	{
			result = rearRightMotor.getSelectedSensorPosition(0);
			result = rightEncoder.getRaw();
		}	else	{	// an illegal value was sent
			result = 0;
		}
		
		return result;
	}

	/*
	 * 0 = left
	 * 1 = right
	 */
	public double getEncoderDistance(int side) {
		if (side == 0)
			return leftEncoder.getDistance();
		else if (side == 1)
			return rightEncoder.getDistance();
		else
			return -1;	
	}
	
	/*
	 * Get the encoder position as a double, averaging both sides
	 * This returns the distance in inches.
	 * Make sure that both encoder values are of same polarity
	 */
	public double getEncoderDistance()	{
		int leftEncoderDistance;
		int rightEncoderDistance;
		
		leftEncoderDistance = -frontLeftMotor.getSelectedSensorPosition(0);	// negative when forward
		rightEncoderDistance = frontRightMotor.getSelectedSensorPosition(0);
	
		// Debug
		//SmartDashboard.putNumber("Left encoder: ", leftEncoderDistance);
		//SmartDashboard.putNumber("Right encoder: ", rightEncoderDistance);
		
		/*
		 * I'm getting half of the pulses that I should on the right encoder, as if 
		 * one of the channels is not working.  I'll use the left only for now
		 * 
		 * Also, left is negative when going forward.
		 */
		
		// Return only the left encoder until the right encoder gets fixed
		return leftEncoderDistance * DRIVE_DIST_PER_PULSE;
	}
	
	 public int getEncoderVelocity(int side)	{
		int result;
		
		if(side == 0)	{
			result = frontLeftMotor.getSelectedSensorVelocity(0);
		} else if (side == 1)	{
			result = frontRightMotor.getSelectedSensorVelocity(0);
		}	else	{	// an illegal value was sent
			result = 0;
		}
		
		return result;
	}
	
	// 0 = front left, 1 = rear left, 2 = front right, 3 = rear right
	public double getSingleMotorOutput(int motorId) {
		double val = -1;
		
		switch (motorId)
		{
		case 0:
			val = frontLeftMotor.getMotorOutputPercent();
			break;
		case 1:
			val = rearLeftMotor.getMotorOutputPercent();
			break;
		case 2:
			val = frontRightMotor.getMotorOutputPercent();
			break;
		case 3:
			val = rearRightMotor.getMotorOutputPercent();
			break;
		}
		return val;
	}
	
	public double getDriveMotorOutput(int side)	{
		double result = 0;
		
		if(side == 0)	{
			result = frontLeftMotor.getMotorOutputPercent();
		} else if (side == 1)	{
			result = frontRightMotor.getMotorOutputPercent();
		}
		
		return result;
	}
	
	public void arcadeDrive(double speedaxis,  double turnaxis)	{
		diffDrive.arcadeDrive(speedaxis, turnaxis);
	}
	
	public void setBrakeMode(boolean brakeon)	{
		if (brakeon)	{
			frontRightMotor.setNeutralMode(NeutralMode.Brake);
			rearLeftMotor.setNeutralMode(NeutralMode.Brake);
			rearRightMotor.setNeutralMode(NeutralMode.Brake);
			frontLeftMotor.setNeutralMode(NeutralMode.Brake);
		}	else {
			frontRightMotor.setNeutralMode(NeutralMode.Coast);
			rearLeftMotor.setNeutralMode(NeutralMode.Coast);
			rearRightMotor.setNeutralMode(NeutralMode.Coast);
			frontLeftMotor.setNeutralMode(NeutralMode.Coast);
		}
	}
	
	
}
