package org.usfirst.frc.team384.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain {
	private WPI_TalonSRX frontRightMotor = new WPI_TalonSRX(12);
	private WPI_TalonSRX rearRightMotor = new WPI_TalonSRX(8);
	private WPI_TalonSRX frontLeftMotor = new WPI_TalonSRX(6);		// -1.0 to run forward
	private WPI_TalonSRX rearLeftMotor = new WPI_TalonSRX(2);		// -1.0 to run forward
	
	private SpeedControllerGroup leftDrivetrain = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
	private SpeedControllerGroup rightDrivetrain = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
	
	private DifferentialDrive diffDrive = new DifferentialDrive(frontLeftMotor, frontRightMotor);
	
	double leftStickValue = 0.0;			
	double rightStickValue = 0.0;
	
	private final double DRIVE_ENC_PPR = 512;
	private final double DRIVE_DIST_PER_PULSE = 5.875 * Math.PI / DRIVE_ENC_PPR;
	
	public Drivetrain() {
		
		frontRightMotor.setSafetyEnabled(false);
		frontLeftMotor.setSafetyEnabled(false);
		rearRightMotor.setSafetyEnabled(false);
		rearLeftMotor.setSafetyEnabled(false);
		
		frontLeftMotor.setInverted(true);
		frontRightMotor.setInverted(true);
		
		rearRightMotor.follow(frontRightMotor);
		rearLeftMotor.follow(frontLeftMotor);		
	}
	
	public void initializeEncoders()	{
		frontLeftMotor.getSensorCollection().setQuadraturePosition(0, 0);
		frontRightMotor.getSensorCollection().setQuadraturePosition(0, 0);
	}

	/*
	 * Get encoder position
	 * 0 = left side drive rail
	 * 1 = right side drive rail
	 */
	public int getEncoderPosition(int side)	{
		int result;
		
		if(side == 0)	{
			result = frontLeftMotor.getSelectedSensorPosition(0);
		} else if (side == 1)	{
			result = frontRightMotor.getSelectedSensorPosition(0);
		}	else	{	// an illegal value was sent
			result = 0;
		}
		
		return result;
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
			frontLeftMotor.setNeutralMode(NeutralMode.Brake);
			rearRightMotor.setNeutralMode(NeutralMode.Brake);
			frontLeftMotor.setNeutralMode(NeutralMode.Brake);
		}	else {
			frontRightMotor.setNeutralMode(NeutralMode.Coast);
			frontLeftMotor.setNeutralMode(NeutralMode.Coast);
			rearRightMotor.setNeutralMode(NeutralMode.Coast);
			frontLeftMotor.setNeutralMode(NeutralMode.Coast);
		}
	}
	
	


}
