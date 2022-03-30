package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "camera red side 2 with cycle")

public class Red2Cycle extends LinearOpMode {
    //red side 1
    Hardware robot = Hardware.getInstance();

    private static OpenCvCamera webCam;
    public AutoEncoder moveencoder = new AutoEncoder();
    private DuckDetector detector;
    public AutoGyro movegyro = new AutoGyro();
    private static String level = "test";

    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();

    @Override
    public void runOpMode() {

        int lowtarget = -90;
        int midtarget = -170;
        int hightarget = -278;

        robot.init(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        detector = new DuckDetector();
        webCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "duck identifier"), cameraMonitorViewId);
        webCam.openCameraDevice();
        FtcDashboard.getInstance().startCameraStream(webCam, 0);
        webCam.setPipeline(detector);
        webCam.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);

        while (!isStarted()) {
            level = detector.level;
            dashboardTelemetry.addData("Level: ", level);
            dashboardTelemetry.addData("LeftTotal: ", detector.leftTotal);
            dashboardTelemetry.addData("CentreTotal: ", detector.centerTotal);
            dashboardTelemetry.addData("RightTotal: ", detector.rightTotal);
            telemetry.addData("Level: ", level);
            telemetry.addData("LeftTotal: ", detector.leftTotal);
            telemetry.addData("CentreTotal: ", detector.centerTotal);
            telemetry.addData("RightTotal: ", detector.rightTotal);
            telemetry.update();
            dashboardTelemetry.update();

        }

        //waitForStart();

        while (!opModeIsActive())
            waitForStart();

        moveencoder.Drive(0.7, -2.2, -2.2, -2.2, -2.2);
        wait(500);
        turn(50);
        moveencoder.Drive(0.5, -15, -15, -15, -15);
        robot.arm.setTargetPosition(midtarget);
        robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armSetPower(0.5);
        if(Math.abs(robot.arm.getTargetPosition() - robot.arm.getCurrentPosition()) < 10) {
            robot.armSetPower(0);
        }
        wait(1000);
        while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
            telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
            telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
            telemetry.update();
        }
        sleep(500);
        //moveencoder.Drive(0.4,-4, -4, -4, -4);
        robot.wheel1.setPosition(0.1);
        robot.wheel2.setPosition(0.9);
        wait(1000);
        robot.wheel1.setPosition(0.5);
        robot.wheel2.setPosition(0.5);
        moveencoder.Drive(0.7, 5, 5, 5, 5);
        sleep(500);
        robot.arm.setTargetPosition(0);
        //robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.armSetPower(0.4);
        while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
            telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
            telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
        }
        turn(40);
        sleep(500);
        movegyro.Drive(0.4, 40, 50, robot, telemetry);
        robot.wheel1.setPosition(0.9);
        robot.wheel2.setPosition(0.1);
        if(robot.sensorDistance.getDistance(DistanceUnit.CM) < 15) {
            telemetry.addData("Distance(cm): ", robot.sensorDistance.getDistance(DistanceUnit.CM));
            if(robot.sensorDistance.getDistance(DistanceUnit.CM) < 5.5) {
                telemetry.addLine("STOP INTAKE!! ONLY ALLOW OUTTAKE");
                robot.redLED.setState(true);
                robot.wheel1.setPosition(0.5);
                robot.wheel2.setPosition(0.5);
            }
            else {
                telemetry.addLine("Allow Intake");
                robot.wheel1.setPosition(0.9);
                robot.wheel2.setPosition(0.1);
                robot.redLED.setState(false);
            }
        }
        else {
            telemetry.addData("Distance(cm): ", 99);
            telemetry.addLine("Out of Range so allow intake");
            robot.redLED.setState(false);
            //robot.greenLED.setState(true);
        }

        telemetry.update();
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception p_exception) {
            //ms = null;
        }
    }

    public void turn (double degree){
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double currentAngle = robot.gyro.getAngularOrientation().firstAngle;
        double targetAngle = currentAngle + degree;
        if (targetAngle > 180) {
            targetAngle -= 360;
        } else if (targetAngle < -180) {
            targetAngle += 360;
        }
        if (degree >= 0) {
            double error = degree;
            while (Math.abs(error) > 3) {
                robot.setPower(-0.0028 * error, -0.0028 * error, 0.0028 * error, 0.0028 * error);
                error = targetAngle - robot.gyro.getAngularOrientation().firstAngle;
                if (error > 180) {
                    error -= 360;
                } else if (error < -180) {
                    error += 360;
                }
                error = Math.abs(error);
            }
        } else {
            double error = degree;
            while (Math.abs(error) > 3) {
                robot.setPower(0.0028 * error, 0.0028 * error, -0.0028 * error, -0.0028 * error);
                error = targetAngle - robot.gyro.getAngularOrientation().firstAngle;
                if (error > 180) {
                    error -= 360;
                } else if (error < -180) {
                    error += 360;
                }
                error = Math.abs(error);
            }
        }
        robot.setPower(0, 0, 0, 0);
    }

    public void driveForward ( double distance, double speed){
        double wheelCircumference = 4 * Math.PI;
        double wheelMotor = 560;
        double count = (distance * (wheelMotor / wheelCircumference));

        robot.setPower(0, 0, 0, 0);

        //switched set target position and reset encoders

        robot.rf.setTargetPosition((int) Math.round(count));
        robot.rb.setTargetPosition((int) Math.round(count));
        robot.lf.setTargetPosition((int) Math.round(count));
        robot.lb.setTargetPosition((int) Math.round(count));

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setPower(speed, speed, speed, speed);

        while (robot.rf.getCurrentPosition() < robot.rf.getTargetPosition()) {
            telemetry.addData("Current: ", robot.rf.getCurrentPosition());
            telemetry.addData("Target: ", robot.rf.getTargetPosition());
            telemetry.addData("Speed: ", robot.rf.getPower());
            telemetry.update();

            //robot.setPower(speed, speed, speed, speed);
        }

        //robot.setPower(0, 0, 0, 0);

        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeLeft ( double distance, double speed){
        double wheelCircumference = 4 * Math.PI;
        double wheelMotor = 560;
        double ticksPos = (distance * (wheelMotor / wheelCircumference));
        double ticksNeg = (-distance * (wheelMotor / wheelCircumference));

        robot.setPower(0, 0, 0, 0);

        robot.rf.setTargetPosition((int) Math.round(ticksPos));
        robot.rb.setTargetPosition((int) Math.round(ticksNeg));
        robot.lf.setTargetPosition((int) Math.round(ticksNeg));
        robot.lb.setTargetPosition((int) Math.round(ticksPos));


        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.setPower(speed, speed, speed, speed);

        //robot.setPower(0, 0, 0, 0);

        while (robot.rf.getCurrentPosition() < robot.rf.getTargetPosition()) {
            telemetry.addData("RF Current: ", robot.rf.getCurrentPosition());
            telemetry.addData("RF Target: ", robot.rf.getTargetPosition());
            telemetry.addData("RB Current: ", robot.rb.getCurrentPosition());
            telemetry.addData("RB Target: ", robot.rb.getTargetPosition());
            telemetry.addData("LF Current: ", robot.lf.getCurrentPosition());
            telemetry.addData("LF Target: ", robot.lf.getTargetPosition());
            telemetry.addData("LB Current: ", robot.lb.getCurrentPosition());
            telemetry.addData("LB Target: ", robot.lb.getTargetPosition());
            telemetry.addData("Speed: ", robot.rf.getPower());
            telemetry.update();
            //robot.setPower(speed, speed, speed, speed);
        }

        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}