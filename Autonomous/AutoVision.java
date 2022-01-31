package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "camera blue side 2")

//@Config

public class AutoVision extends LinearOpMode {
    Hardware robot = Hardware.getInstance();

    private static OpenCvCamera webCam;
    public AutoEncoder moveencoder = new AutoEncoder();
    private DuckDetector detector;
    private static String level = "test";

    @Override
    public void runOpMode() {

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
            telemetry.addData("Level: ", level);
            telemetry.addData("LeftTotal: ", detector.leftTotal);
            telemetry.addData("CentreTotal: ", detector.centerTotal);
            telemetry.addData("RightTotal: ", detector.rightTotal);
            telemetry.update();
        }

        //waitForStart();

        while (!opModeIsActive())
            waitForStart();

        //code while robot is running - detect level and place freight
        if (level.equals("ONE")) { //bottom tier
            moveencoder.Drive(0.4, 3, 3, 3, 3);  //5 for level 3 and 2
            robot.setsClawPosition(0.27);

            wait(1000);
            moveencoder.Drive(0.6, -28, 28, 28, -28);
            moveencoder.Drive(0.5, 2, 2, 2, 2);  //8 for level 3
            sleep(1000);
            robot.intakeSetPower(0.2);
            robot.arm.setTargetPosition(-1065);  //-800 for level 3, -950 for level 2
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            wait(3000);
            moveencoder.Drive(0.4, 5.3, 5.3, 5.3, 5.3); //not there for level 3, 2 for level 2
            robot.setsClawPosition(0.27);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                //telemetry.update();
            }
            sleep(1000);
            robot.setsClawPosition(0.5);
            sleep(1000);
            moveencoder.Drive(0.5, -5, -5, -5, -5);

            robot.setsClawPosition(0.27);
            sleep(1000);
            robot.intakeSetPower(0);
            robot.arm.setTargetPosition(7);
            //robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", 7);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
            }
            telemetry.update();
            moveencoder.Drive(0.3, -3, -3, -3, -3);
            turn(90);
            //moveencoder.Drive(0.2, -7, 7, 7, -7);
            strafeLeft(5, 0.4);
            driveForward(45, 0.4);
            robot.intakeSetPower(0.6);
        }

        if (level.equals("TWO")) { //middle tier

            moveencoder.Drive(0.4, 3, 3, 3, 3);  //5 for level 3 and 2
            robot.setsClawPosition(0.27);

            wait(1000);
            moveencoder.Drive(0.4, -26, 26, 26, -26);
            moveencoder.Drive(0.5, 2.7, 2.7, 2.7, 2.7);  //8 for level 3
            sleep(2000);
            robot.intakeSetPower(0.15);
            robot.arm.setTargetPosition(-962);  //-800 for level 3, -950 for level 2
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            wait(4000);
            moveencoder.Drive(0.4, 4, 4, 4, 4); //not there for level 3, 2 for level 2
            robot.setsClawPosition(0.27);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                //telemetry.update();
            }
            sleep(1000);
            robot.setsClawPosition(0.5);
            sleep(2000);
            moveencoder.Drive(0.4, -4, -4, -4, -4);

            robot.setsClawPosition(0.27);
            sleep(1000);
            robot.intakeSetPower(0);
            robot.arm.setTargetPosition(7);
            //robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", 7);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
            }
            telemetry.update();
            moveencoder.Drive(0.3, -3, -3, -3, -3);
            turn(90);
            //moveencoder.Drive(0.2, -7, 7, 7, -7);
            strafeLeft(5, 0.4);
            driveForward(45, 0.4);
            robot.intakeSetPower(0.6);
        }

        if (level.equals("THREE")) {  //top tier

            moveencoder.Drive(0.4, 4, 4, 4, 4);  //5 for level 3 and 2
            robot.setsClawPosition(0.27);

            wait(1000);
            moveencoder.Drive(0.4, -25, 25, 25, -25);
            moveencoder.Drive(0.5, 6, 6, 6, 6);  //8 for level 3
            sleep(2000);
            robot.intakeSetPower(0.15);
            robot.arm.setTargetPosition(-833);  //-800 for level 3, -950 for level 2
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            wait(3000);
            robot.setsClawPosition(0.27);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                //telemetry.update();
            }
            sleep(1000);
            robot.setsClawPosition(0.5);
            sleep(2000);
            moveencoder.Drive(0.4, -5, -5, -5, -5);

            robot.setsClawPosition(0.27);
            sleep(1000);
            robot.intakeSetPower(0);
            robot.arm.setTargetPosition(7);
            //robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", 5);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
            }
            telemetry.update();
            moveencoder.Drive(0.3, -3, -3, -3, -3);
            turn(90);
            //moveencoder.Drive(0.2, -7, 7, 7, -7);
            strafeLeft(5, 0.4);
            driveForward(45, 0.4);
            //robot.intakeSetPower(0.6);
        }
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
