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

@Autonomous(name = "camera red side 1")

//@Config

public class AutoOpenCV extends LinearOpMode {
    //red side 1
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
            telemetry.addData("CentreTotal: ", detector.centreTotal);
            telemetry.addData("RightTotal: ", detector.rightTotal);
            telemetry.update();
        }

        //waitForStart();

        while (!opModeIsActive())
            waitForStart();

        //code while robot is running - detect level and place freight
        if (level.equals("ONE")) { //bottom tier
            robot.setsClawPosition(0.27);
            robot.intakeSetPower(0.15);
            moveencoder.Drive(0.5, -13, 13, 13, -13);
            moveencoder.Drive(0.4, 8, 8, 8, 8);
            robot.arm.setTargetPosition(-1126);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && Math.abs(gamepad2.right_stick_y) == 0 && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", -1126);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                telemetry.update();
            }
            sleep(2000);
        }

        if (level.equals("TWO")) { //middle tier
            robot.setsClawPosition(0.27);
            robot.intakeSetPower(0.15);
            driveForward(0.2, 4);
            moveencoder.Drive(0.5, -18, 18, 18, -18);
            moveencoder.Drive(0.4, 10, 10, 10, 10);
            robot.arm.setTargetPosition(-760);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                //telemetry.update();
            }
            sleep(1000);
            robot.setsClawPosition(0.5);
            sleep(1000);
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
            turn(90);
            driveForward(20, 0.3);
        }

        if (level.equals("THREE")) {  //top tier
            robot.setsClawPosition(0.27);
            //sleep(1000);
            //moveencoder.Drive(0.4, 5,5,5,5);
            //driveForward(0.6, 8);
            //moveencoder.Drive(0.4, 18, -18, -18, 18);
            strafeLeft(0.4, 18);
            wait(3000);
            moveencoder.Drive(0.4, 12, 12, 12, 12);
            robot.intakeSetPower(0.15);
            robot.arm.setTargetPosition(-760);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            while (robot.arm.isBusy() && robot.digitalTouch.getState() == true) {
                telemetry.addData("TargetPosition ", robot.arm.getTargetPosition());
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                //telemetry.update();
            }
            sleep(1000);
            robot.setsClawPosition(0.5);
            sleep(1000);
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
            turn(90);
            driveForward(20, 0.3);
            robot.setsRetract(0.43);
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
                telemetry.addData("Current: ", robot.rf.getCurrentPosition());
                telemetry.addData("Target: ", robot.rf.getTargetPosition());
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
