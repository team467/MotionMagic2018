package org.usfirst.frc.team467.robot;

import org.apache.log4j.Logger;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {


	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	
	final float deadZoneLimit = 0.1f;
	
	final int leftMotorLeader = 1;
	final int rightMotorLeader = 4;
	final int leftMotorFollower1 = 2;
	final int leftMotorFollower2 = 3;
	final int rightMotorFollower1 = 5;
	final int rightMotorFollower2 = 6;
	
	
	//LD means leader
	//FW means follower
	CANTalon leftMotorLD;
	CANTalon rightMotorLD;
	CANTalon leftMotorFW1;
	CANTalon leftMotorFW2;
	CANTalon rightMotorFW1;
	CANTalon rightMotorFW2;

	Joystick stick;
	XboxController controller;
	private boolean isScaled = true;
	private boolean diScaled = false;
	private double speedScaler = 1.0;
	private double turnScaler = 1.0;
	private double initialSpeedScaler = 1.0;
	private double initialTurnScaler = 1.0;
	
	public void init() {
		Logging.init();
		speedScaler = Double.parseDouble(SmartDashboard.getString("DB/String 0", "0.5"));
		turnScaler = Double.parseDouble(SmartDashboard.getString("DB/String 1", "0.5"));
		initialSpeedScaler = speedScaler;
		initialTurnScaler = turnScaler;
		controller = new XboxController(0);
		stick = new Joystick(0);
		leftMotorLD = new CANTalon(leftMotorLeader);
		rightMotorLD = new CANTalon(rightMotorLeader);
		rightMotorFW1 = new CANTalon(rightMotorFollower1);
		rightMotorFW2 = new CANTalon(rightMotorFollower2);
		leftMotorFW1 = new CANTalon(leftMotorFollower1);
		leftMotorFW2 = new CANTalon(leftMotorFollower2);
		
		leftMotorLD.changeControlMode(TalonControlMode.PercentVbus);
		leftMotorLD.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		leftMotorLD.reverseSensor(false);
//		leftMotor.configNominalOutputVoltage(+0.0f, -0.0f);
//		leftMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		leftMotorLD.setProfile(0);
		leftMotorLD.setF(0);
		leftMotorLD.setP(0);
		leftMotorLD.setI(0);
		leftMotorLD.setD(0);
		leftMotorLD.setMotionMagicCruiseVelocity(0);
		leftMotorLD.setMotionMagicAcceleration(0);


		rightMotorLD.changeControlMode(TalonControlMode.PercentVbus);
		rightMotorLD.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		rightMotorLD.reverseSensor(false);
//		rightMotor.configNominalOutputVoltage(+0.0f, -0.0f);
//		rightMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		rightMotorLD.setProfile(0);
		rightMotorLD.setF(0);
		rightMotorLD.setP(0);
		rightMotorLD.setI(0);
		rightMotorLD.setD(0);
		rightMotorLD.setMotionMagicCruiseVelocity(0);
		rightMotorLD.setMotionMagicAcceleration(0);
		
		rightMotorFW1.changeControlMode(TalonControlMode.Follower);
		rightMotorFW2.changeControlMode(TalonControlMode.Follower);
		leftMotorFW1.changeControlMode(TalonControlMode.Follower);
		leftMotorFW2.changeControlMode(TalonControlMode.Follower);

		System.out.println(stick);
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void buttons() {
			if (stick.getRawButton(3)) {
				leftMotorLD.changeControlMode(TalonControlMode.MotionMagic);
				rightMotorLD.changeControlMode(TalonControlMode.MotionMagic);
				System.out.println("Motion Magic Called");
				double targetPos = stick.getAxis(AxisType.kY);
				leftMotorLD.set(targetPos);
				rightMotorLD.set(targetPos);
//				LOGGER.debug("FL = " + getData(leftMotor));
//				LOGGER.debug("FR = " + getData(rightMotor));
			} else {
				leftMotorLD.changeControlMode(TalonControlMode.PercentVbus);
				rightMotorLD.changeControlMode(TalonControlMode.PercentVbus);
				double Xaxis = stick.getRawAxis(4);
				double Yaxis = stick.getAxis(AxisType.kY);
				
				Xaxis = (Math.abs(Xaxis) < deadZoneLimit) ? 0 : Xaxis; 
				Yaxis = (Math.abs(Yaxis) < deadZoneLimit) ? 0 : Yaxis; 

				// LOGGER.debug("X=" + Xaxis + ", Y=" + Yaxis);
//				LOGGER.debug("FL = " + getData(leftMotor));
//				LOGGER.debug("FR = " + getData(rightMotor));

				Timer.delay(0.005); // wait for a motor update time
				// TODO This makes isScaled true when the B Button is pressed.
				if (controller.getBButton()) {
					isScaled = false;
				} else {
					isScaled = true;
				}
				arcadeDrive(Xaxis, Yaxis);
				
				if (controller.getRawButton(1)) {
					isScaled = true;
					diScaled = true;
				}
				else {
					speedScaler = initialSpeedScaler;
					turnScaler = initialTurnScaler;
				}
			}

	}

	private String getData(CANTalon motor) {
		return "RPM:" + motor.getSpeed() + ", Pos:" + motor.getPosition() + ", throttle:"
				+ motor.getOutputVoltage() / motor.getBusVoltage() + ", Closed Loop Error:" + motor.getClosedLoopError()
				+ "";
	}

	/**
	 * Copied from WPILib
	 * 
	 * @param turn
	 * @param speed
	 */
	public void arcadeDrive(double turn, double speed) {
		if (isScaled) {
			speed *= speedScaler;
			turn *= turnScaler;
		}
		if (diScaled) {
			speed *= 0.986;
			turn *= 0.986;
			//LOGGER.debug("decelerate");
		}
		final double left;
		final double right;

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
		leftMotorLD.set(-left);
		leftMotorFW1.set(leftMotorLeader);
		leftMotorFW2.set(leftMotorLeader);

		rightMotorLD.set(right);
		rightMotorFW1.set(rightMotorLeader);
		rightMotorFW2.set(rightMotorLeader);
		
	}

}
