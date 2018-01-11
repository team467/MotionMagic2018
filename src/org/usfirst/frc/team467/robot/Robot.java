
package org.usfirst.frc.team467.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.apache.log4j.Logger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private static final Logger LOGGER = Logger.getLogger(Robot.class);
	
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
    final int flID = 4;
    final int frID = 5;
    final int blID = 3;
    final int brID = 6;
    
    WPI_TalonSRX fl;
    WPI_TalonSRX fr;
    WPI_TalonSRX bl;
    WPI_TalonSRX br;
    
    Joystick stick;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	Logging.init();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        stick = new Joystick(0);
        fl = new WPI_TalonSRX(flID);
        fr = new WPI_TalonSRX(frID);
        bl = new WPI_TalonSRX(blID);
        br = new WPI_TalonSRX(brID);
        
        fl.set(ControlMode.PercentOutput, 0.0);
        fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        fl.setSensorPhase(false);
        fl.configNominalOutputForward(0.0f, 0);
        fl.configNominalOutputReverse(0.0f, 0);
        fl.configPeakOutputForward(+12.0f, 0);
        fl.configPeakOutputReverse(-12.0f, 0);
        fl.selectProfileSlot(0, 0);
        fl.config_kF(0, 0, flID);
        fl.config_kP(0, 0, flID);
        fl.config_kI(0, 0, flID);
        fl.config_kD(0, 0, flID);
        fl.configMotionCruiseVelocity(0, 0);
        fl.configMotionAcceleration(0, 0);
        
        bl.set(ControlMode.Follower, flID);

        fr.set(ControlMode.PercentOutput, 0.0);
        fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        fr.setSensorPhase(false);
        fr.configNominalOutputForward(0.0f, 0);
        fr.configNominalOutputReverse(0.0f, 0);
        fr.configPeakOutputForward(+12.0f, 0);
        fr.configPeakOutputReverse(-12.0f, 0);
        fr.selectProfileSlot(0, 0);
        fr.config_kF(0, 0, frID);
        fr.config_kP(0, 0, frID);
        fr.config_kI(0, 0, frID);
        fr.config_kD(0, 0, frID);
        fr.configMotionCruiseVelocity(0, 0);
        fr.configMotionAcceleration(0, 0);
        
        br.set(ControlMode.Follower, frID);
        
        System.out.println(stick);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        while (isOperatorControl() && isEnabled()) {
        	if (stick.getRawButton(1)) {
        		double targetPos = stick.getY();
        		
        		fl.set(ControlMode.MotionMagic, targetPos);
        		fr.set(ControlMode.MotionMagic, targetPos);
        		LOGGER.debug("FL = " + getData(fl));
        		LOGGER.debug("FR = " + getData(fr));
        	} else {
        		double Xaxis = stick.getRawAxis(4);
                double Yaxis = stick.getY();
                
//                LOGGER.debug("X=" + Xaxis + ", Y=" + Yaxis);
                LOGGER.debug("FL = " + getData(fl));
        		LOGGER.debug("FR = " + getData(fr));
                arcadeDrive(Xaxis, Yaxis);

                Timer.delay(0.005); // wait for a motor update time
        	}
            
        }
    }
    
    private String getData(WPI_TalonSRX motor) {
		return "RPM:" + motor.getActiveTrajectoryVelocity() +
				", Pos:" + motor.getActiveTrajectoryPosition() +
				", throttle:" + motor.getMotorOutputVoltage()/motor.getBusVoltage() + 
				", Closed Loop Error:" + motor.getClosedLoopError(0) + 
				"";
	}

	/**
     * Copied from WPILib
     * 
     * @param turn
     * @param speed
     */
    public void arcadeDrive(double turn, double speed) {
        final double left;
        final double right;
//        final double maxTurn = 0.9; // Double.valueOf(SmartDashboard.getString("DB/String 1", "0.9"));
//        final double minTurn = 0.5; // Double.valueOf(SmartDashboard.getString("DB/String 2", "0.5"));
//        SmartDashboard.putString("DB/String 6", String.valueOf(maxTurn));
//        SmartDashboard.putString("DB/String 7", String.valueOf(minTurn));
//
//        turn *= (1.0 - Math.abs(speed)) * (maxTurn - minTurn) + minTurn;
//        turn = square(turn);
        
        // turn;
        if (speed > 0.0) {
            if (turn > 0.0) {
              left = speed - turn;
              right = Math.max(speed, turn);
            } else {
              left = Math.max(speed, -turn);
              right = speed + turn;
            }
        } else {
            if (turn > 0.0) {
              left = -Math.max(-speed, turn);
              right = speed + turn;
            } else {
              left = speed - turn;
              right = -Math.max(-speed, -turn);
            }
        }
        drive(left, right);
    }
    
    /**
     * Squares a number but keeps the sign
     * 
     * @param number Usually speed
     * @return Squared value
     */
    private double square(double number)
    {
        if (number >= 0.0)
        {
            return number * number;
        }
        else
        {
            return -(number * number);
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    private void drive(double left, double right) {
    	fl.set(-left);
    	bl.set(flID);
    	
    	fr.set(right);
    	br.set(frID);
    }
    
}
