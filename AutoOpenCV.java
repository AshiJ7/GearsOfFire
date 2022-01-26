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

@Autonomous(name = "Vision Autonomous Try 2")

//@Config

public class AutoOpenCV extends LinearOpMode {

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

        while(!opModeIsActive())
            waitForStart();
        //code while robot is running
        if (level.equals("ONE")) {
            robot.setsClawPosition(0.27);
            robot.intakeSetPower(0.15);
            moveencoder.Drive(0.5, -12, 12, 12, -12);
            moveencoder.Drive(0.4, 9, 9, 9, 9);
            robot.arm.setTargetPosition(-1100);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);
            sleep(3500);
            robot.setsClawPosition(0.5);
            sleep(2000);
            robot.setsClawPosition(0.27);
        }
        if (level.equals("TWO")) {

            robot.arm.setTargetPosition(-1000);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.armSetPower(0.2);
            sleep(3500);
            robot.setsClawPosition(0.5);
            sleep(2000);
            robot.setsClawPosition(0.27);
        }

        if (level.equals("THREE")) {
            robot.setsClawPosition(0.27);
            robot.intakeSetPower(0.15);
            moveencoder.Drive(0.5, -12, 12, 12, -12);
            moveencoder.Drive(0.4, 9, 9, 9, 9);
            
            robot.arm.setTargetPosition(-750);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.armSetPower(0.2);

            while (robot.arm.isBusy() && Math.abs(gamepad2.right_stick_y) == 0 && robot.digitalTouch.getState() == true)  {
                telemetry.addData("TargetPosition ", -760);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                telemetry.update();
            }
            robot.armSetPower(0.2);
            sleep(3500);
            robot.setsClawPosition(0.5);
            sleep(2000);
            robot.setsClawPosition(0.27);
        }

        telemetry.update();
    }
}