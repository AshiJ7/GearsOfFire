package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="The 300 Point TeleOp Program")

public class TheTeleop extends LinearOpMode {
    Hardware robot = Hardware.getInstance();
    private ElapsedTime run = new ElapsedTime();

    //-830 to -266 higher position
    //-1040 to -80 lower pos
    //-1017 to -168

    public void runOpMode() {
        double drive = 0;
        double turn = 0;
        double max;
        double strafe = 0;
        double duckiepower = 0;
        boolean clawclosetimer = false;
        double clawclosetimetarget = 0;
        boolean rtpressed = false;
        boolean ltpressed = false;
        boolean clawopentimer = false;
        double clawopentimetarget = 0;
        boolean bpressed = false;
        boolean clawcaptimer = false;
        double clawcaptimetarget = 0;
        boolean apressed = false;
        boolean clawclosecaptimer = false;
        double clawclosecaptimertarget = 0;
        boolean clawopencaptimer = false;
        double clawopencaptimetarget = 0;
        double armpow = 0.3;

        int armtargetup;
        int armtargetdown;

        robot.init(hardwareMap);

        robot.wheel1.setPosition(0.5);
        robot.wheel2.setPosition(0.5);

        waitForStart();
        while(opModeIsActive()) {
            drive = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x/3;
            strafe = -gamepad1.left_stick_x;
            duckiepower = gamepad2.right_stick_y;

            //ARM AND TOUCH SENSOR!!
            // telemetry for state of digital sensor
            if (!robot.digitalTouch.getState()) {
                telemetry.addData("Digital Touch: ", "Pressed");
            }
            else {
                telemetry.addData("Digital Touch: ", "Not Pressed");
            }

            //some telemetry values for arm and claw
            telemetry.addData("EncoderValue: ",robot.arm.getCurrentPosition());
            telemetry.addData("TargetPosition: ", robot.arm.getTargetPosition());

            if(robot.sensorDistance.getDistance(DistanceUnit.CM) < 15) {
                telemetry.addData("Distance(cm): ", robot.sensorDistance.getDistance(DistanceUnit.CM));
                if(robot.sensorDistance.getDistance(DistanceUnit.CM) < 6) {
                    telemetry.addLine("STOP INTAKE!!!! ONLY ALLOW OUTTAKE");

                }
                else {
                    telemetry.addLine("Allow Intake");
                }
            }
            else {
                telemetry.addData("Distance(cm): ", 99);
                telemetry.addLine("Out of Range so allow intake");
            }

            telemetry.addData("Alpha: ", robot.sensorcolor.alpha());
            telemetry.addData("Red: ", robot.sensorcolor.red());
            telemetry.addData("Green: ", robot.sensorcolor.green());
            telemetry.addData("Blue: ", robot.sensorcolor.blue());

            telemetry.update();

            //gamepad 1 driving
            max = Math.max(Math.abs(drive - strafe - turn), Math.max(Math.abs(drive + strafe - turn),
                    Math.max(Math.abs(drive + strafe + turn), Math.abs(drive + turn - strafe))));
            double scaleFactor;
            if (max < robot.maxSpeed) {
                scaleFactor = 1;
            }
            else {
                scaleFactor = (robot.maxSpeed / max);
            }
            //scaleFactor = scaleFactor - [something]
            scaleFactor *= (1 - Math.min(gamepad1.right_trigger, 0.95));
            drive = adjust(drive);
            turn = adjust(turn);
            strafe = adjust(strafe);
            robot.setPower((drive - strafe - turn) * scaleFactor, (drive + strafe - turn) * scaleFactor,
                    (drive + strafe + turn) * scaleFactor,(drive + turn - strafe) * scaleFactor);


            if (robot.arm != null) {
                if (gamepad2.left_stick_y > 0.2) {
                    robot.arm.setTargetPosition(-90);
                    robot.armSetPower(armpow);
                }
                if (gamepad2.left_stick_y < -0.2) {
                    //robot.arm.setTargetPosition(-830);
                    //robot.armSetPower(armpow);
                    robot.arm.setTargetPosition(-570);
                    robot.armSetPower(1);
                    robot.arm.setTargetPosition(-300);
                    robot.armSetPower(0.1);
                }
                if (Math.abs(gamepad2.left_stick_x) > 0.2) {
                    robot.arm.setTargetPosition(-186);
                    robot.armSetPower(armpow);
                }
                if (gamepad2.x){
                    robot.arm.setTargetPosition(-850);
                    robot.armSetPower(armpow);
                }
                if (clawclosetimer && System.currentTimeMillis() >= clawclosetimetarget){
                    clawclosetimer = false;
                    robot.arm.setTargetPosition(-300);
                    robot.armSetPower(1);// lift up after the timer
                }
                if (clawopentimer && System.currentTimeMillis() >= clawopentimetarget) {
                    clawopentimer = false;
                    robot.arm.setTargetPosition(-600);
                    robot.armSetPower(1);
                    robot.arm.setTargetPosition(0);
                    robot.armSetPower(.375);//go back once claw is opened
                }
                if (clawcaptimer && System.currentTimeMillis() >= clawcaptimetarget) {
                    clawcaptimer = false;
                    robot.arm.setTargetPosition(-90);
                    robot.armSetPower(armpow);
                }
                if (clawclosecaptimer && System.currentTimeMillis() >= clawclosecaptimertarget) {
                    clawclosecaptimer = false;
                    robot.arm.setTargetPosition(-1085);
                    robot.armSetPower(armpow);
                    clawopencaptimer = true;
                    clawopencaptimetarget = System.currentTimeMillis()+600;
                }

                if (Math.abs(robot.arm.getTargetPosition() - robot.arm.getCurrentPosition()) <= 5 || (robot.digitalTouch != null && !robot.digitalTouch.getState() && robot.arm.getTargetPosition() > robot.arm.getCurrentPosition())){
                    robot.armSetPower(0);
                }
            }

            run.reset();

            /**if (gamepad2.left_bumper) {
                robot.setsRetract(0.43);

                robot.duckPower(-.51);
            }
            else if (gamepad2.right_bumper) {
                robot.setsRetract(0.43);
                robot.duckPower(.51);
            }
            else {
                robot.duckPower(0);
            }*/

                robot.duckPower(duckiepower);

            if (gamepad2.right_trigger > 0.2 && !rtpressed){
                //robot.setsClawPosition(clawclose);
                clawclosetimer = true;
                clawclosetimetarget = System.currentTimeMillis()+500;
                rtpressed = true;
            }
            if (!(gamepad2.right_trigger > 0.2)){
                rtpressed = false;
            }// making sure it is just pressed once

            if (gamepad2.left_trigger > 0.2 && !ltpressed){
                //robot.setsClawPosition(clawopen);
                clawopentimer = true;
                clawopentimetarget = System.currentTimeMillis()+500;
                ltpressed = true;
            }
            if (!(gamepad2.left_trigger > 0.2)){
                ltpressed = false;
            }

            if (gamepad1.left_trigger > 0.2 && !ltpressed) {
                //robot.setsClawPosition(clawopen);
                clawopentimer = true;
                clawopentimetarget = System.currentTimeMillis()+500;
                ltpressed = true;
            }

            if (!(gamepad1.left_trigger > 0.2)){
                ltpressed = false;
            }

            if (gamepad2.b && !bpressed){
                //robot.setsClawPosition(clawclose);
                clawcaptimer = true;
                clawcaptimetarget = System.currentTimeMillis()+300;
                bpressed = true;
            }

            if (!gamepad2.b){
                bpressed = false;
            }

            if (gamepad2.a && !apressed){
                //robot.setsClawPosition(clawclose);
                clawclosecaptimer = true;
                clawclosecaptimertarget = System.currentTimeMillis()+300;
                apressed = true;
            }  //0.38 open 0.28 close
            if (!gamepad2.a){
                apressed = false;
            }
            if (clawopencaptimer && System.currentTimeMillis() >= clawopencaptimetarget){
                //robot.setsClawPosition(clawopen);
                clawopencaptimer = false;
            }

            if (gamepad2.y){
                robot.setsRetract(0);
            }

            if(gamepad2.dpad_left && robot.sensorDistance.getDistance(DistanceUnit.CM) > 6) { //intake
                robot.wheel1.setPosition(0.9);
                robot.wheel2.setPosition(0.1);
            }
            else if(gamepad2.dpad_right) { //outtake
                robot.wheel1.setPosition(0.1);
                robot.wheel2.setPosition(0.9);
            }
            else {
                robot.wheel1.setPosition(0.5);
                robot.wheel2.setPosition(0.5);
            }

            if(gamepad1.a) {
                robot.arm.setTargetPosition(-1000);
                robot.armSetPower(0.8);
            }

            if(gamepad2.dpad_down) {
                armtargetup = robot.arm.getCurrentPosition() - 25;
                robot.arm.setTargetPosition(armtargetup);
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armSetPower(1);

                //robot.arm.getTargetPosition = robot.arm.getCurrentPosition();
            }
            if(gamepad2.dpad_up) {
                armtargetdown = robot.arm.getCurrentPosition() + 25;
                robot.arm.setTargetPosition(armtargetdown);
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armSetPower(1);
            }


        }//close while loop for opmode is active
    }//close opmode

    public double adjust (double varToAdjust) {
        return (Math.atan(5 * varToAdjust) / Math.atan(5));
    }

    boolean movingByEncoder = false;
    public void liftByEncoder(double speed, int newTarget) {
        if (opModeIsActive()) {
            movingByEncoder = true;
            robot.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.arm.setTargetPosition(newTarget);
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            run.reset();
            robot.arm.setPower(Math.abs(speed));

            //telemetry for encoder positions
            while (robot.arm.isBusy() && Math.abs(gamepad2.right_stick_y) == 0 && robot.digitalTouch.getState() == true )  {
                telemetry.addData("TargetPosition ", newTarget);
                telemetry.addData("CurrentPosition ", robot.arm.getCurrentPosition());
                telemetry.update();
            }
            //robot.arm.setPower(0);
            robot.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //movingByEncoder = false;
        }
    }
}//close class


