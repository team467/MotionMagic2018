	package org.usfirst.frc.team467.robot;

	import edu.wpi.first.wpilibj.IterativeRobot;
	import edu.wpi.first.wpilibj.Joystick;
	import edu.wpi.first.wpilibj.Joystick.AxisType;
	import edu.wpi.first.wpilibj.Timer;
	import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

	import org.apache.log4j.Logger;

	import com.ctre.CANTalon;
	import com.ctre.CANTalon.FeedbackDevice;
	import com.ctre.CANTalon.TalonControlMode;


public class Drive {
	private static final Logger LOGGER = Logger.getLogger(Robot.class);
	/**
	 * The VM is configured to automatically run this class, and to call the
	 * functions corresponding to each mode, as described in the IterativeRobot
	 * documentation. If you change the name of this class or the package after
	 * creating this project, you must also update the manifest file in the resource
	 * directory.
	 */
	    

	    final int frontLeftLDID = 4;
	    final int frID = 5;
	    final int bottomLeftLDID = 3;
	    final int brID = 6;
	    CANTalon frontLeftLD; //front left Leader
	    CANTalon fr;
	    CANTalon bottomLeftLD;
	    CANTalon br;
	    
	    Joystick stick;
	    XboxController controller;
	   
	    
	    /**
	     * This function is run when the robot is first started up and should be
	     * used for any initialization code.
	     */
	    public void init() {
	    	Logging.init();
	        stick = new Joystick(0);
	        controller = new XboxController(0);
	        frontLeftLD = new CANTalon(frontLeftLDID);
	        fr = new CANTalon(frID);
	        bottomLeftLD = new CANTalon(bottomLeftLDID);
	        br = new CANTalon(brID);
	        controller = new XboxController(0);
	        
	        frontLeftLD.changeControlMode(TalonControlMode.PercentVbus);
	        frontLeftLD.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
	        frontLeftLD.reverseSensor(false);
	        frontLeftLD.configNominalOutputVoltage(+0.0f, -0.0f);
	        frontLeftLD.configPeakOutputVoltage(+6.0f, -6.0f);
	        frontLeftLD.setProfile(0);
	        frontLeftLD.setF(0);
	        frontLeftLD.setP(0);
	        frontLeftLD.setI(0);
	        frontLeftLD.setD(0);
	        frontLeftLD.setMotionMagicCruiseVelocity(0);
	        frontLeftLD.setMotionMagicAcceleration(0);
	        
	        bottomLeftLD.changeControlMode(TalonControlMode.Follower);

	        fr.changeControlMode(TalonControlMode.PercentVbus);
	        fr.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
	        fr.reverseSensor(false);
	        fr.configNominalOutputVoltage(+0.0f, -0.0f);
	        fr.configPeakOutputVoltage(+12.0f, -12.0f);
	        fr.setProfile(0);
	        fr.setF(0);
	        fr.setP(0);
	        fr.setI(0);
	        fr.setD(0);
	        fr.setMotionMagicCruiseVelocity(0);
	        fr.setMotionMagicAcceleration(0);
	        
	        br.changeControlMode(TalonControlMode.Follower);
	        
	        System.out.println(stick);
	        
            Logging.init();

	    	
	        /**/
            
	       // stick = new Joystick(0);
	        
	        
	        //stick = new Joystick(0);
	        //controller = new XboxController(0);
	        
	        System.out.println(stick);
	    }
	    /*public void robotInit() {
	    	
	    }*/
	    
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
//			autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
			
	    }


	    /**
	     * This function is called periodically during operator control
	     */
	    public void buttons() {
	        	if (stick.getRawButton(1)) {
	        		frontLeftLD.changeControlMode(TalonControlMode.MotionMagic);
	        		fr.changeControlMode(TalonControlMode.MotionMagic);
	        		double targetPos = stick.getAxis(AxisType.kY);
	        		frontLeftLD.set(targetPos);
	        		fr.set(targetPos);
	        		LOGGER.debug("FL = " + getData(frontLeftLD));
	        		LOGGER.debug("FR = " + getData(fr));
	        	} else {
	        		frontLeftLD.changeControlMode(TalonControlMode.PercentVbus);
	        		fr.changeControlMode(TalonControlMode.PercentVbus);
	        		double Xaxis = stick.getRawAxis(4);
	                double Yaxis = stick.getAxis(AxisType.kY);
	                
//	                LOGGER.debug("X=" + Xaxis + ", Y=" + Yaxis);
	                LOGGER.debug("FL = " + getData(frontLeftLD));
	        		LOGGER.debug("FR = " + getData(fr));
	                arcadeDrive(Xaxis, Yaxis);

	                Timer.delay(0.005); // wait for a motor update time
	        	}
	            
	        }
	    
	    private String getData(CANTalon motor) {
			return "RPM:" + motor.getSpeed() +
					", Pos:" + motor.getPosition() +
					", throttle:" + motor.getOutputVoltage()/motor.getBusVoltage() + 
					", Closed Loop Error:" + motor.getClosedLoopError() + 
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
	        
//	        final double maxTurn = 0.9; // Double.valueOf(SmartDashboard.getString("DB/String 1", "0.9"));
//	        final double minTurn = 0.5; // Double.valueOf(SmartDashboard.getString("DB/String 2", "0.5"));
//	        SmartDashboard.putString("DB/String 6", String.valueOf(maxTurn));
//	        SmartDashboard.putString("DB/String 7", String.valueOf(minTurn));
	//
//	        turn *= (1.0 - Math.abs(speed)) * (maxTurn - minTurn) + minTurn;
//	        turn = square(turn);
	        
	        if (controller.getBButton() == false) {
	        	speed *= 0.5;
	        }
	        
	        if (controller.getBButton() == false) {
	        	turn *= 0.5;
	        }
	        
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
	    

	    public void testPeriodic() {
	    
	    }
	    
	    private void drive(double left, double right) {
	    	frontLeftLD.set(-left);
	    	bottomLeftLD.set(frontLeftLDID);
	    	
	    	fr.set(right);
	    	br.set(frID);
	    }

	    
	}
