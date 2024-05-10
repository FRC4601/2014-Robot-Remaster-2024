//CB-2 "BLACKBIRD" REMASTER

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

  //Auto Chooser
  private static final String NoAutoSelected = "No Auto Selected";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

   //Motors
   private final PWMVictorSPX frontleftMotor = new PWMVictorSPX(1);
   private final PWMVictorSPX backleftMotor = new PWMVictorSPX(2);
   private final PWMVictorSPX frontrightMotor = new PWMVictorSPX(3);
   private final PWMVictorSPX backrightMotor = new PWMVictorSPX(4);
   private final PWMVictorSPX spinMotor = new PWMVictorSPX(5);
   private final PWMVictorSPX winchMotor = new PWMVictorSPX(6);
   private final PWMVictorSPX pickupMotor = new PWMVictorSPX(7);
   private final PWMVictorSPX flapperMotor = new PWMVictorSPX(8);

   //Drivetrain 
   private DifferentialDrive m_drive;

   //Controls
   private final Joystick rightstick = new Joystick(1);
   private final Joystick leftstick = new Joystick(0);
   private final XboxController xbox = new XboxController(2);

   //Timers
   private final Timer autoTimer = new Timer();
   private final Timer teleTimer = new Timer();

   //Pneumatics
   private final DoubleSolenoid solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
   

  @Override
  public void robotInit() {
    
    //Auto Chooser
    m_chooser.setDefaultOption(NoAutoSelected, NoAutoSelected);
    SmartDashboard.putData("Auto Chooser", m_chooser);
    
    //Camera
    CameraServer.startAutomaticCapture();

    //Drivetrain
    frontleftMotor.setInverted(true);
    frontleftMotor.addFollower(backleftMotor);
    frontrightMotor.addFollower(backrightMotor);
    m_drive = new DifferentialDrive(frontleftMotor, frontrightMotor);

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    
    //Auto Chooser
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);

    //Auto Timer
    autoTimer.reset();
    autoTimer.start();
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    
    //Auto Timer
    autoTimer.stop();
    autoTimer.reset();
    
    //Tele Timer
    teleTimer.reset();
    teleTimer.start();
  }

  @Override
  public void teleopPeriodic() {

  // Tank drive with a given left and right rates
    m_drive.tankDrive(leftstick.getY(), rightstick.getY());

  // Spin Motors Control
    if(xbox.getLeftY() >=0.5 || xbox.getLeftY() <=-0.5) { //Intake Arm Move
      spinMotor.set(xbox.getLeftY()*.5);
   } else  { //Intake Arm Stop
      spinMotor.set(0);
   } 

  // Winch Motors Control
    if(xbox.getAButton()) { //Winch Retract
      winchMotor.set(-1);
    } else if(xbox.getBButton()) { //Winch Release
      winchMotor.set(1);
    }  else {
      winchMotor.set(0.0);  
    }

  // Intake Motor Control
    if(rightstick.getRawButton(1)) { //Spits Ball Out - Trigger on right Joystick
      pickupMotor.set(-1);
    } else if(leftstick.getRawButton(1)) { //Ejects Ball in - Trigger on left Joystick
      pickupMotor.set(1);
    } else {
      pickupMotor.set(0.0);  
    }

  // Flapper Motor Control
    if(leftstick.getRawButton(2)) { //Open Flapper - Middle Button on left Joystick
      flapperMotor.set(0.4);
    } else if(rightstick.getRawButton(2)) { //Closes Flapper - Middle Button on right Joystick
      flapperMotor.set(-0.4);
    } else {
      flapperMotor.set(0.0);  
    }

  // Lock's Pneumatics Control
    if(xbox.getStartButton()) { //Launcher Lock
      solenoid.set(DoubleSolenoid.Value.kForward);
    } else if(xbox.getBackButton()) { //Launcher Unlocked
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
