/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team384.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	public OI	oi;
	public Drivetrain drivetrain;
	public static final int STICK0 = 0;
	public static final int YAXIS	= 1;
	public static final int XAXIS	= 0;
	
	public Robot() {
		oi = new OI();
		drivetrain = new Drivetrain();
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//m_chooser.addDefault("Default Auto", kDefaultAuto);
		//m_chooser.addObject("My Auto", kCustomAuto);
		//SmartDashboard.putData("Auto choices", m_chooser);
		
		drivetrain.setBrakeMode(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putBoolean("Trigger", oi.getButtonHeld(STICK0,1));
		SmartDashboard.putNumber("XAxis", oi.getAxis(STICK0, XAXIS));
		SmartDashboard.putNumber("YAxis", oi.getAxis(STICK0, YAXIS));
		
		SmartDashboard.putNumber("Left Front", drivetrain.getSingleMotorOutput(0));
		SmartDashboard.putNumber("Left Rear", drivetrain.getSingleMotorOutput(1));
		SmartDashboard.putNumber("Right Front", drivetrain.getSingleMotorOutput(2));
		SmartDashboard.putNumber("Right Rear", drivetrain.getSingleMotorOutput(3));
		if (oi.getButtonHeld(STICK0,1))	{
			drivetrain.arcadeDrive(oi.getAxis(STICK0, YAXIS), -oi.getAxis(STICK0, XAXIS));
			SmartDashboard.putNumber("Left Front", drivetrain.getSingleMotorOutput(0));
			SmartDashboard.putNumber("Left Rear", drivetrain.getSingleMotorOutput(1));
			SmartDashboard.putNumber("Right Front", drivetrain.getSingleMotorOutput(2));
			SmartDashboard.putNumber("Right Rear", drivetrain.getSingleMotorOutput(3));
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}
