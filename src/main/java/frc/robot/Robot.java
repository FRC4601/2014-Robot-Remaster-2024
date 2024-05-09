//CB-2 "BLACKBIRD" REMASTER

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

//import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

   //Motors
   private final PWMVictorSPX frontleftMotor = new PWMVictorSPX(1);
   private final PWMVictorSPX backleftMotor = new PWMVictorSPX(2);
   private final PWMVictorSPX frontrightMotor = new PWMVictorSPX(3);
   private final PWMVictorSPX backrightMotor = new PWMVictorSPX(4);
   private final PWMVictorSPX spinMotor = new PWMVictorSPX(5);
   private final PWMVictorSPX winchMotor = new PWMVictorSPX(6);
   private final PWMVictorSPX pickupMotor = new PWMVictorSPX(7);
   private final PWMVictorSPX flapperMotor = new PWMVictorSPX(8);

   // Speed Controller Groups
   private final MotorControllerGroup leftSpeedGroup = new MotorControllerGroup(frontleftMotor, backleftMotor);
   private final MotorControllerGroup rightSpeedGroup = new MotorControllerGroup(frontrightMotor, backrightMotor);

   //drivetrain 
   DifferentialDrive drivetrain = new DifferentialDrive(leftSpeedGroup, rightSpeedGroup);

   //Controls
   private final Joystick rightstick = new Joystick(0);
   private final Joystick leftstick = new Joystick(1);
   private final XboxController xbox = new XboxController(2);

   //Pneumatics
   //private final Compressor Kyle = new Compressor(0, PneumaticsModuleType.CTREPCM);
   private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
   

  @Override
  public void robotInit() {

    frontleftMotor.setInverted(true);
    backleftMotor.setInverted(true);
    
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  //  Kyle.enableDigital();
  //  Kyle.disable();
  }

  @Override
  public void teleopPeriodic() {

  // Tank drive with a given left and right rates
    drivetrain.tankDrive(leftstick.getY(), rightstick.getY());

  // Spin Motors Control
    if(xbox.getRawAxis(3) >=0.01 || xbox.getRawAxis(3) <=-0.01) { //Intake Arm Move
      spinMotor.set(xbox.getRawAxis(3)*.5);
   } else  { //Intake Arm Stop
      spinMotor.set(0);
   } 

  // Winch Motors Control
    if(xbox.getRawButton(3)) { //Winch Retract
      winchMotor.set(-1);
    } else if(xbox.getRawButton(2)) { //Winch Extend
      winchMotor.set(1);
    }  else {
      winchMotor.set(0.0);  
    }

  // Pickup Motor Control
    if(rightstick.getRawButton(1)) { //Spins Ball In
      pickupMotor.set(-1);
    } else if(leftstick.getRawButton(1)) { //Spits Ball Out
      pickupMotor.set(1);
    } else {
      pickupMotor.set(0.0);  
    }

  // Flapper Motor Control
    if(rightstick.getRawButton(3)) { //Open Flapper
      flapperMotor.set(0.4);
    } else if(rightstick.getRawButton(2)) { //Closes Flapper
      flapperMotor.set(-0.4);
    } else {
      flapperMotor.set(0.0);  
    }

  // Pneumatics Control
    if(xbox.getRawButton(9)) { //Launcher Lock
      solenoid.set(DoubleSolenoid.Value.kForward);
    } else if(xbox.getRawButton(10)) { //Launcher Unlocked
      solenoid.set(DoubleSolenoid.Value.kReverse);
   }

  }
      
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
