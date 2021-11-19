package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "EncoderTest")
public class EncoderTest extends LinearOpMode {

    Hardware robot = new Hardware(); // Uses the robot's hardware.

    static final double cpmv = 1440; // Counts per motor rev. ???
    static final double reduction = 2.0; // Drive gear reduction. ???
    static final double diameter = 4.0; // Wheel diameter (in). Used to calculate circumference.
    static final double cpi = (cpmv * reduction) / (diameter * 3.1415); // Counts per inch. ???

    static final double forwardSpd = 1.0; // Forward speed.
    static final double turnSpd = 0.75; // Turn speed.
    static final double strafeSpd = 0.5; // Strafe speed.

    @Override
    public void runOpMode() { // It moves...?

        robot.init(hardwareMap); // Initializes variables.

        // Telemetry tells that robot is waiting.
        telemetry.addData("ENCODERS WHEN", "BOTTOM TEXT");
        telemetry.update();

        // Ensures that the robot is not moving and resets encoders.
        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Uses encoders.
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Telemetry tells that encoder reset was successful.
        telemetry.addData("Encoders, yeah.", "Starting at",
                robot.rf.getCurrentPosition(), robot.rb.getCurrentPosition(),
                robot.lf.getCurrentPosition(), robot.lb.getCurrentPosition());
        telemetry.update();

        waitForStart(); // Waits for driver to press PLAY.

        /*
         * Step 1: Moves forward 60 in, 5 s timeout.
         * Step 2: Turns left 12 in, 2.5 s timeout.
         * Step 3: Moves backward 30 in, 2.5 s timeout.
         * Step 4: Strafes left 60 in, 2.5 s timeout.
         */
        encoderDrive(forwardSpd, 60, 60,60, 60);
        encoderDrive(turnSpd, 12, 12, -12, -12);
        encoderDrive(forwardSpd, -30, -30, -30, -30);
        encoderDrive(strafeSpd, 60, -60, -60, 60);

        // Telemetry tells that robot is done moving.
        telemetry.addData("Encoders, yeah.", "You survived.");
        telemetry.update();
    }

    /*
     * It moves, yeah. Takes values for speed,
     * distances (in) moved by right front, right back, left front, left back wheels,
     * timeout (ms).
     */
    public void encoderDrive(double spd, double rfIn, double rbIn, double lfIn, double lbIn) {

        // Declares target positions for each wheel to move to.
        int rfTarget, rbTarget, lfTarget, lbTarget;

        if (opModeIsActive()) { // Checks if opMode is running.

            // Initializes target positions.
            rfTarget = robot.rf.getCurrentPosition() + (int)(rfIn * cpi);
            rbTarget = robot.rb.getCurrentPosition() + (int)(rbIn * cpi);
            lfTarget = robot.lf.getCurrentPosition() + (int)(lfIn * cpi);
            lbTarget = robot.lb.getCurrentPosition() + (int)(lbIn * cpi);

            // Sets motor targets.
            robot.rf.setTargetPosition(rfTarget);
            robot.rb.setTargetPosition(rbTarget);
            robot.lf.setTargetPosition(lfTarget);
            robot.lb.setTargetPosition(lbTarget);

            // Turns on RUN_TO_POSITION. Sets motors to move to targets.
            robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Resets motors' start motion.
            robot.rf.setPower(Math.abs(spd));
            robot.rb.setPower(Math.abs(spd));
            robot.lf.setPower(Math.abs(spd));
            robot.lb.setPower(Math.abs(spd));

            // Checks that opMode is running, there is runtime left, and all motors are running.
            while (opModeIsActive() &&
                    robot.rf.isBusy() && robot.rb.isBusy() && robot.lf.isBusy() && robot.lb.isBusy()) {

                // Telemetry tells targets and current positions.
                telemetry.addData("Targets--",  "RF, RB, LF, LB:", rfTarget, rbTarget, lfTarget,
                        lbTarget);
                telemetry.addData("Current positions--",  "RF, RB, LF, LB",
                        robot.rf.getCurrentPosition(), robot.rb.getCurrentPosition(),
                        robot.lf.getCurrentPosition(), robot.lb.getCurrentPosition());
                telemetry.update();
            }

            // Stops motion.
            robot.rf.setPower(0);
            robot.rb.setPower(0);
            robot.lf.setPower(0);
            robot.lb.setPower(0);

            // Turns off RUN_TO_POSITION.
            robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(3000);   // Timeout.
        }
    }
}