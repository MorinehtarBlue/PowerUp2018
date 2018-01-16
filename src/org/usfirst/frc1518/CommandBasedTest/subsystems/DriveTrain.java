// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1518.CommandBasedTest.subsystems;

import org.usfirst.frc1518.CommandBasedTest.OI;
import org.usfirst.frc1518.CommandBasedTest.Robot;
import org.usfirst.frc1518.CommandBasedTest.RobotMap;
import org.usfirst.frc1518.CommandBasedTest.commands.*;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 */
public class DriveTrain extends Subsystem {


	public static double circumferenceInInches = 20.1;  // 2017=20.5
	public static int pulsesPerRotation = 4096;
	public static RobotDrive drive;
	
	public DriveTrain(){
		super();
		if(Robot.isTestBot == false){
		drive = new RobotDrive(RobotMap.driveTrainFrontLeftWheel, RobotMap.driveTrainRearLeftWheel, RobotMap.driveTrainFrontRightWheel, RobotMap.driveTrainRearRightWheel);
		}
		else if(Robot.isTestBot == true){
			drive = new RobotDrive(RobotMap.pwmDriveFL, RobotMap.pwmDriveRL, RobotMap.pwmDriveFR, RobotMap.pwmDriveRR);
		}
	}
	
	//public void drive(GenericHID mainstick){
		/*double zAxis = Robot.oi.mainstick.getZ();
		double yAxis = Robot.oi.mainstick.getY();
		double xAxis = Robot.oi.mainstick.getX() * 0.5;
    	//yAxis = (yAxis * yAxis);
    	zAxis = (zAxis * Math.abs(zAxis)) + xAxis;
		
		drive.arcadeDrive(yAxis,zAxis);*/
		
	//}

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {


        setDefaultCommand(new Drive());

        

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    public void takeJoystickInputs(Double inputOne, Double inputTwo) {
		//double xAxis = (zAxis + Robot.oi.mainstick.getX()) * 0.75;
		//yAxis = (yAxis * Math.abs(yAxis));
    	// multiplying the zAxis by itself causes a slower power curve on the steering
    	// adding 50% of the xAxis to enable better steering while moving forward
    	//zAxis = (zAxis * Math.abs(zAxis) * 0.75);
    	//xAxis = (xAxis * Math.abs(xAxis) * 0.75);
    	
    	if (Robot.isTankMode == true) { // Drive in TANK MODE
        	drive.tankDrive(inputOne, inputTwo); // inputOne = LEFT SIDE; inputTwo = RIGHT SIDE
    	}
    	else { // Drive in ARCADE MODE
        	drive.arcadeDrive(inputOne, inputTwo); // inputOne = Y ; inputTwo = X
    		
    	}
    	
    }
    public void stop() {
    	drive.tankDrive(0,0);
    }
    
}
