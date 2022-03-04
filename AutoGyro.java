package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
@Autonomous(name = "Gyro", group = "sensor")
public class AutoGyro extends LinearOpMode {

    Hardware robot = Hardware.getInstance();
    double count = 560 / (Math.PI * 4);  //revolutions/(pi * diameter)

    public void runOpMode() {

        robot.init(hardwareMap);

        robot.gyro.isGyroCalibrated();

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        //call to action
        // Drive(0.6, 20, 30, robot, telemetry);
        //Drive(0.4, 0, 180, robot, telemetry);  //fwd 1 ft at 30 deg angle
    }

    public void Drive(double speed, double distance, double angle, Hardware robot, Telemetry display) {
        //robot.init(hardwareMap);

        int rftarget, lftarget, rbtarget, lbtarget;
        double rfspeed, lfspeed, rbspeed, lbspeed;
        double max, error, steer;

        rftarget = robot.rf.getCurrentPosition() + (int)(distance * count);
        lftarget = robot.lf.getCurrentPosition() + (int)(distance * count);
        rbtarget = robot.rb.getCurrentPosition() + (int)(distance * count);
        lbtarget = robot.lb.getCurrentPosition() + (int)(distance * count);

        robot.rf.setTargetPosition(rftarget);
        robot.rb.setTargetPosition(rbtarget);
        robot.lf.setTargetPosition(lftarget);
        robot.lb.setTargetPosition(lbtarget);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        speed = Range.clip(Math.abs(speed), 0, 1);
        robot.rf.setPower(speed);
        robot.rb.setPower(speed);
        robot.lf.setPower(speed);
        robot.lb.setPower(speed);

        while ((robot.rf.isBusy() && robot.rb.isBusy() && robot.lf.isBusy() && robot.lb.isBusy()) ||
                (Math.abs(normalize(angle/**gyro read - target*/)) > rftarget))
        {
            error = getError(angle);
            steer = getSteer(error, 0.1);

            if (distance > 0) {
                steer *= -1.0;
            }

            rfspeed = speed + steer;
            rbspeed = speed + steer;
            lfspeed = speed - steer;
            lbspeed = speed - steer;

            // Normalize speeds if either one exceeds +/- 1.0;
            max = Math.max(Math.max(Math.abs(speed), Math.abs(speed)), Math.max(Math.abs(speed), Math.abs(speed)));
            if (max > 1.0) {
                lfspeed = lfspeed / max;
                lbspeed = lbspeed / max;
                rfspeed = rfspeed / max;
                rbspeed = rbspeed / max;
            }

            robot.lf.setPower(lfspeed);
            robot.lb.setPower(lbspeed);
            robot.rf.setPower(rfspeed);
            robot.rb.setPower(rbspeed);

            //display.addData("Target: ", rftarget);
            //display.addData("Current: ", robot.rf.getCurrentPosition());
            //display.update();
        }

        robot.rf.setPower(0);
        robot.rb.setPower(0);
        robot.lf.setPower(0);
        robot.lb.setPower(0);

        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public double getError(double angle) {
        double error;
        error = angle;
        while (error > 180 && error < 360) {
            error = error - 360;
        }
        while (error <= -180) {
            error = error + 360;
        }
        return error;
    }

    public double normalize (double angle) {
        double something;
        something = angle;
        while (angle > 180 && angle < 360) {
            angle = angle - 360;
        }
        while (angle <= -180) {
            angle = angle + 360;
        }
        return angle;
    }


    public double getSteer(double error, double PCoeff) {
        return Range.clip(error * PCoeff, -1, 1);
    }
}