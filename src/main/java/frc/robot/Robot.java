package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

  // change these depending on the controller used have and the range of buttons used
  private final Joystick controller = new Joystick(0);
  private final int minButtonIndex = 1; // lowest button used (can be set from 1 to 10 currently)
  private final int maxButtonIndex = 10; // highest button used (can be set from 1 to 10 currently) make sure that maxButtonIndex >= minButtonIndex
  private final String[] buttons = { "A", "B", "X", "Y", "LB", "RB", "Change View", "Menu", "L", "R" }; // buttons on an Xbox 1 controller
  private final double gameTimeLimit = 20; // in seconds

  // don't change these
  private int score;
  private int buttonToPressIndex;
  private String buttonToPressString;
  private int pressedButton;
  private boolean firstTime;
  private Timer myTimer = new Timer();
  private double t0;
  private boolean isFinished;
  private double timeRemaining;

  @Override
  public void teleopInit() {
    myTimer.start();
    score = 0;
    firstTime = true;
    buttonToPressIndex = -1; // value must be out of range for the first iteration of the periodic loop
    isFinished = false;
  }

  @Override
  public void teleopPeriodic() {

    // prints the score to smart dashboard
    SmartDashboard.putNumber("score", score);

    if (!isFinished) {
      // figures out which button the user pressed
      pressedButton = 0;
      for (int i = minButtonIndex; i < maxButtonIndex + 1; i++) {
        if (controller.getRawButtonPressed(i)) {
          pressedButton = i;
        }
      }

      // checks if the user presses the right button or not and adjusts the score
      if (pressedButton == buttonToPressIndex) {
        score++;
      }

      // score can't go below 0
      if (score < 0) {
        score = 0;
      }

      if ((pressedButton != 0) || firstTime || ((myTimer.get() - t0) >= 2)) { // if you did press a button or if this is the first iteration of the periodic loop

        // only can be the first time once
        firstTime = false;

        // random int between min and max values, inclusive
        buttonToPressIndex = (int) ((Math.random() * (maxButtonIndex - minButtonIndex)) + minButtonIndex + 0.5);

        // figures out what button the user needs to press based on what buttonIndex is
        for (int i = minButtonIndex; i < maxButtonIndex + 1; i++) {
          if (buttonToPressIndex == i) {
            buttonToPressString = buttons[i - 1];
          }
        }

        // tells the user which button to press
        SmartDashboard.putString("press", buttonToPressString);

        // clear the timer
        t0 = myTimer.get();
      }

      // figures out how much time is left and puts it on smart dashboard
      timeRemaining = gameTimeLimit - myTimer.get();
      if (timeRemaining < 0) {
        timeRemaining = 0;
      }
      SmartDashboard.putNumber("Time Remaining", timeRemaining);

      // ends the game after a certain time limit
      if (myTimer.get() >= gameTimeLimit) {
        isFinished = true;
      }
    }
  }
}