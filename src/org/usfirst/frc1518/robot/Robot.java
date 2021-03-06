// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1518.robot;

import org.usfirst.frc1518.robot.commands.*;
import org.usfirst.frc1518.robot.subsystems.*;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

//import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

    Command autoMode;

    public static OI oi;
    public static RobotMap rm;
    public static Pneumatics pn;
    
    //Hardware
    public static MecanumDrive m_drive;
	public static UsbCamera cam0;
	public static AxisCamera cam1;
	
    //SubSystems
    public static Pneumatics pneumatics;
    public static boolean turbo;

    //Setup
    public static boolean isTestBot = false;			//<------------------- Determine Drive Train Here
    public static double feedSpeed;
	public static boolean isReversed = true;
	public static boolean intakeOn;
	public static String alliance = "";
	public static double xDrive;
	
	//Joystick Deadspace Controls
	public static double mainstickX;
	public static double mainstickY;
	public static double mainstickZ;
	
		// AUX "encoders"
	public static int boxswitch;
	public static int climbswitch;

	SendableChooser<Command> m_chooser = new SendableChooser<>();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void robotInit() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        oi = new OI();
    	rm = new RobotMap();
    	rm.init();
    	pn = new Pneumatics();
    	
    	rm.comp0.setClosedLoopControl(true);
    	if (isTestBot == true) {
    		m_drive = new MecanumDrive(rm.testdriveTrainFrontLeftWheel, rm.testdriveTrainRearLeftWheel, rm.testdriveTrainFrontRightWheel, rm.testdriveTrainRearRightWheel);
    	}
    	else {
    	m_drive = new MecanumDrive(rm.driveTrainFrontLeftWheel, rm.driveTrainRearLeftWheel, rm.driveTrainFrontRightWheel, rm.driveTrainRearRightWheel);
    	}
    	
        rm.dio8.set(true);
        rm.dio9.set(true);
        rm.rioGyro.calibrate();
        rm.encoderLRear.reset();
        rm.encoderRRear.reset();
        turbo=false;
        boxswitch = 1;
        climbswitch = 0;

        //Camera setup
/*        cam0 = CameraServer.getInstance().startAutomaticCapture();
        cam0.setResolution(160, 120);
        cam0.setFPS(15);
        cam0.setBrightness(35);  */
        cam1 = CameraServer.getInstance().addAxisCamera("10.15.18.100");
        cam1.setResolution(320, 240);
        cam1.setBrightness(40);

        //Get Alliance from FMS
        alliance = DriverStation.getInstance().getAlliance().toString();
        SmartDashboard.putString("Alliance", alliance);
        
        // instantiate the command used for the autonomous period
        m_chooser = new SendableChooser();
        m_chooser.addDefault("No Auto", null);
        m_chooser.addObject("Opposite Switch No Drop", new Auto1());
        m_chooser.addObject("Home Switch From Middle", new Auto2());
        m_chooser.addObject("Robot Left (Switch)", new Auto3());
        m_chooser.addObject("Robot Right (Switch)", new Auto4());
        m_chooser.addObject("Robot Left (Scale)", new Auto5());
        m_chooser.addObject("Robot Right (Scale)", new Auto6());
        SmartDashboard.putData("AutoMode", m_chooser);

        //SETTING BRAKE MODE ON DRIVE MOTORS
        rm.driveTrainFrontLeftWheel.setNeutralMode(NeutralMode.Brake);
        rm.driveTrainFrontRightWheel.setNeutralMode(NeutralMode.Brake);
        rm.driveTrainRearLeftWheel.setNeutralMode(NeutralMode.Brake);
        rm.driveTrainRearRightWheel.setNeutralMode(NeutralMode.Brake);
        rm.testdriveTrainFrontLeftWheel.setNeutralMode(NeutralMode.Brake);
        rm.testdriveTrainFrontRightWheel.setNeutralMode(NeutralMode.Brake);
        rm.testdriveTrainRearLeftWheel.setNeutralMode(NeutralMode.Brake);
        rm.testdriveTrainRearRightWheel.setNeutralMode(NeutralMode.Brake);
        rm.lift.setNeutralMode(NeutralMode.Brake);
        rm.climb.setNeutralMode(NeutralMode.Brake);
        rm.lift.setInverted(true);
        rm.climb.setInverted(true);
    }

    /**
     * This function is called when the disabled button is hit and first time thru Init().
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	RobotMap.dio8.set(true);
    	RobotMap.dio9.set(true);
    	rm.rioGyro.reset();
        rm.encoderLRear.reset();
        rm.encoderRRear.reset();
    	isReversed = true;
    	intakeOn = false;
    	m_drive.stopMotor();
    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        setLights();
    	m_drive.setSafetyEnabled(true);

        autoMode = (Command) m_chooser.getSelected();
        if (autoMode != null) autoMode.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autoMode != null) autoMode.cancel();
    	rm.rioGyro.reset();
        setLights();
    	m_drive.setSafetyEnabled(true);
    	boxswitch = 0;
        climbswitch = 0;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        //turbo = true;
        if (Robot.oi.turbo.get()) {
        	xDrive = 1.0;
        }
        
        else {
        	xDrive = 0.65;
        }
        
    	//COMPUTE JOYSTICK VALUES GIVING DEADSPACE
    	if (Math.abs(oi.mainstick.getX()) >= 0.30) {	
    		mainstickX = oi.mainstick.getX(); 
    	}
    	
    	else { 
    		mainstickX = 0; 
    	}
    	
    	if (Math.abs(oi.mainstick.getY()) >= 0.30) {	
    		mainstickY = oi.mainstick.getY(); 
    	}
    	
    	else { 
    		mainstickY = 0; 
    	}
    	
    	if (Math.abs(oi.mainstick.getZ()) >= 0.25) {	
    		mainstickZ = oi.mainstick.getZ(); 
    	}
    	
    	else { 
    		mainstickZ = 0; 
    	}
    	
    	double gyroAngle = rm.rioGyro.getAngle();
    	SmartDashboard.putNumber("Gyro Angle", gyroAngle);
    	SmartDashboard.putNumber("Left Encoder Count", rm.encoderLRear.get());    // PUT BACK
    	SmartDashboard.putNumber("Right Encoder Count", rm.encoderRRear.get());  //  PUT BACK
    	m_drive.driveCartesian((Math.pow(mainstickX, 3) * xDrive), (Math.pow(mainstickY, 3) * -xDrive), (Math.pow(mainstickZ, 3) * .6), 0.0);
    	//RobotMap.dio8.pulse(1);
    	//RobotMap.dio9.pulse(0);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        //LiveWindow..run();
    }
    
    public void setLights() {
        alliance = DriverStation.getInstance().getAlliance().toString();
    	if (alliance == "Red") {
    		RobotMap.dio8.set(false);
    		RobotMap.dio9.set(true);
       	}
    	
    	else if (alliance == "Blue"){
    		RobotMap.dio8.set(true);
    		RobotMap.dio9.set(false);
    	}
    	
    	else {
    		RobotMap.dio8.set(true);
    		RobotMap.dio9.set(true);
       	}
    	
    }
    
}