package org.usfirst.frc.team384.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {
	
	Joystick joystick0 = new Joystick(0);
	Joystick joystick1 = new Joystick(1);
	
	boolean[][] joyButtonStatus = {
			{false, false, false, false, false, false, false, false, false, false, false, false},	// Joysyick 0, buttons 1 thru 11
			{false, false, false, false, false, false, false, false, false, false, false, false}	// Joysyick 1, buttons 1 thru 11
	};
	
	public boolean getButtonHeld(int stick, int b)	{
		boolean result = false;
		
		if (stick == 0) {
			result = joystick0.getRawButton(b);
		} else if (stick == 1)	{
			result = joystick1.getRawButton(b);
		}
		
		// this should not be relevent to the current robot
		if (result)
			joyButtonStatus[stick] [b] = true;	// store the pressed state of the button
		//System.out.println("Recorded button held for stick " + stick + " button " + b);
		return result;
	}
	
	public boolean getButtonPressed(int stick, int b)	{
		boolean result = false;
		
		if (stick == 0) {
			result = joystick0.getRawButtonPressed(b);
		} else if (stick == 1)	{
			result = joystick1.getRawButtonPressed(b);
		}
		if (result)
		//if (stick > 0 && stick <= 1 && b > 0 && b <= 9)
			joyButtonStatus[stick] [b] = true;	// store the pressed state of the button
		//System.out.println("Recorded button pressed for stick " + stick + " button " + b);
		return result;
	}
	
	public double getAxis(int stick, int axis) {
		double axisval = 0.0;
		
		if(stick == 0) {
			if (axis == 0)	{
				axisval = joystick0.getX();
			} else if (axis == 1)	{
				axisval = joystick0.getY();
			} else if (axis == 2)	{
				axisval = joystick0.getZ();
			}
			
		}	else if (stick == 1) {
			if (axis == 0)	{
				axisval = joystick1.getX();
			} else if (axis == 1)	{
				axisval = joystick1.getY();
			} else if (axis == 2)	{
				axisval = joystick1.getZ();
			}
		}	else	{	// an illegal value of stick was selected
			axisval = 0.0;
		}
		
		return axisval;
	}

}
