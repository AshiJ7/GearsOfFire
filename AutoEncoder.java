package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@Autonomous(name = "AutoEncoder", group = "testing")
public class AutoEncoder extends LinearOpMode {

    private ElapsedTime run = new ElapsedTime();
    Hardware robot = Hardware.getInstance();
    private ElapsedTime runtime = new ElapsedTime();

    double count = 560/(Math.PI * 4);  //revolutions/pi * diameter

    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        robot.rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();

        //Drive(0.4, -12, -12, -12, -12);  //forward
        //Drive(0.4, 12, 12, 12, 12);      //backward
        //Drive(0.4, 10, -10, -10, 10);     //right
        //Drive(0.4, -10, 10, 10, -10);     //left
        //Drive(0.8, -5, 5, 5, -5);
        //Drive(0.8, 5, -5, -5, 5);

    }

    public void Drive(double speed, double rfdist, double lfdist, double rbdist, double lbdist)
    {
        //double count = 560/(Math.PI * 4); // revolutions/pi * diameter
        int rftarget, lftarget, rbtarget, lbtarget;

        rftarget = robot.rf.getCurrentPosition() + (int)(rfdist * count);
        lftarget = robot.lf.getCurrentPosition() + (int)(lfdist * count);
        rbtarget = robot.rb.getCurrentPosition() + (int)(rbdist * count);
        lbtarget = robot.lb.getCurrentPosition() + (int)(lbdist * count);

        robot.rf.setTargetPosition(rftarget);
        robot.rb.setTargetPosition(rbtarget);
        robot.lf.setTargetPosition(lftarget);
        robot.lb.setTargetPosition(lbtarget);

        robot.rf.setPower(speed);
        robot.rb.setPower(speed);
        robot.lf.setPower(speed);
        robot.lb.setPower(speed);

        robot.rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive() && (robot.rf.getCurrentPosition() + 10 < rftarget) ||
                (robot.rf.getCurrentPosition() - 10 > rftarget))
        {
            //telemetry.addData("Current Position: ", robot.rf.getCurrentPosition());
            //telemetry.addData("Distance from Target Position: ", (robot.rf.getTargetPosition() - robot.rf.getCurrentPosition()));
            //telemetry.addData("Speed: ", robot.rf.getPower());
            //telemetry.update();
        }
    }
}
