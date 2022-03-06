package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="The 400 Point TeleOp Program")

public class TheTeleOP extends LinearOpMode {
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
        boolean clawclosetimer = false;
        double clawclosetimetarget = 0;
        boolean rtpressed = false;
        boolean ystatus = false;
        boolean pressingy = false;
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
        double armpow = .95;
        double capposup = -0.4;
        double capposdown = 0.75;

        int armtargetup;
        int armtargetdown;

        robot.init(hardwareMap);

        waitForStart();
        while(opModeIsActive()) {
            drive = gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x / 3;
            strafe = -gamepad1.left_stick_x;

            //ARM AND TOUCH SENSOR!!
            // telemetry for state of digital sensor
            if (!robot.digitalTouch.getState()) {
                telemetry.addData("Digital Touch: ", "Pressed");
            } else {
                telemetry.addData("Digital Touch: ", "Not Pressed");
            }

            //some telemetry values for arm and claw
            telemetry.addData("EncoderValue: ", robot.arm.getCurrentPosition());
            telemetry.addData("TargetPosition: ", robot.arm.getTargetPosition());
            telemetry.addData("Cap Pos: ", robot.capp.getPosition());

            if (robot.sensorDistance.getDistance(DistanceUnit.CM) < 15) {
                telemetry.addData("Distance(cm): ", robot.sensorDistance.getDistance(DistanceUnit.CM));
                if (robot.sensorDistance.getDistance(DistanceUnit.CM) < 5.5) {
                    telemetry.addLine("STOP INTAKE!!!! ONLY ALLOW OUTTAKE");
                    robot.redLED.setState(true);
                    robot.greenLED.setState(false);

                    robot.wheel1.setPosition(0.5);
                    robot.wheel2.setPosition(0.5);
                } else {
                    telemetry.addLine("Allow Intake");
                    robot.redLED.setState(false);
                    robot.greenLED.setState(true);
                }
            } else {
                telemetry.addData("Distance(cm): ", 99);
                telemetry.addLine("Out of Range so allow intake");
                robot.redLED.setState(false);
                robot.greenLED.setState(true);
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
            } else {
                scaleFactor = (robot.maxSpeed / max);
            }
            //scaleFactor = scaleFactor - [something]
            scaleFactor *= (1 - Math.min(gamepad1.right_trigger, 0.95));
            drive = adjust(drive);
            turn = adjust(turn);
            strafe = adjust(strafe);
            robot.setPower((drive - strafe - turn) * scaleFactor, (drive + strafe - turn) * scaleFactor,
                    (drive + strafe + turn) * scaleFactor, (drive + turn - strafe) * scaleFactor);

            if (robot.arm != null) {
                if (gamepad2.left_stick_y > 0.2) {
                    robot.arm.setTargetPosition(-420);
                    robot.armSetPower(armpow);
                }
                if (gamepad2.left_stick_y < -0.2) {
                    robot.arm.setTargetPosition(-830);
                    robot.armSetPower(armpow);
                    robot.arm.setTargetPosition(-570);
                    robot.armSetPower(1);
                    robot.arm.setTargetPosition(-1350);
                    robot.armSetPower(armpow);
                }
                if (Math.abs(gamepad2.left_stick_x) > 0.2) {
                    robot.arm.setTargetPosition(-870);
                    robot.armSetPower(armpow);
                }

                if (clawclosetimer && System.currentTimeMillis() >= clawclosetimetarget) {
                    clawclosetimer = false;
                    robot.arm.setTargetPosition(-1300);
                    robot.armSetPower(1);// lift up after the timer
                }
                if (clawopentimer && System.currentTimeMillis() >= clawopentimetarget) {
                    clawopentimer = false;
                    //robot.arm.setTargetPosition(-600);
                    //robot.armSetPower(1);
                    robot.arm.setTargetPosition(0);
                    robot.armSetPower(.9);//go back once claw is opened
                }
                if (clawcaptimer && System.currentTimeMillis() >= clawcaptimetarget) {
                    clawcaptimer = false;
                    robot.arm.setTargetPosition(-420);
                    robot.armSetPower(armpow);
                }
                if (clawclosecaptimer && System.currentTimeMillis() >= clawclosecaptimertarget) {
                    clawclosecaptimer = false;
                    robot.arm.setTargetPosition(-1085);
                    robot.armSetPower(armpow);
                    clawopencaptimer = true;
                    clawopencaptimetarget = System.currentTimeMillis() + 600;
                }

                if (Math.abs(robot.arm.getTargetPosition() - robot.arm.getCurrentPosition()) <= 5 || (robot.digitalTouch != null && !robot.digitalTouch.getState() && robot.arm.getTargetPosition() > robot.arm.getCurrentPosition())) {
                    robot.armSetPower(0);
                }

                if (gamepad1.a) {
                    robot.arm.setTargetPosition(-6000);
                    robot.armSetPower(armpow);
                }
            }

            run.reset();

            if(gamepad2.x) {
                robot.setsRetract(0);
            }
            else if(gamepad2.y) {
                robot.setsRetract(0.43);
            }

            if(gamepad2.right_trigger > 0.1 && gamepad2.right_trigger < 0.5) {
                robot.duckPower(0.26);
            }
            else if(gamepad2.right_trigger > 0.4) {
                robot.duckPower(gamepad2.right_trigger/2);
            }
            else if(gamepad2.left_trigger > 0.1 && gamepad2.left_trigger < 0.5) {
                robot.duckPower(-0.26);
            }
            else if(gamepad2.left_trigger > 0.4) {
                robot.duckPower(-gamepad2.left_trigger/2);
            }
            else {
                robot.duckPower(0);
            }

            if(gamepad2.dpad_right) {
                robot.setcappos(0.75);
            }
            else if(gamepad2.dpad_left) {
                robot.setcappos(-0.4);
            }
            else if(gamepad2.a) {
                robot.setcappos(robot.capp.getPosition() + 0.02);
            }
            else if(gamepad2.b) {
                robot.setcappos(robot.capp.getPosition() - 0.02);
            }


            /**if (gamepad2.right_trigger > 0.2 && !rtpressed){
             //robot.setsClawPosition(clawclose);
             clawclosetimer = true;
             clawclosetimetarget = System.currentTimeMillis()+0.00;
             rtpressed = true;
             }
             if (!(gamepad2.right_trigger > 0.2)){
             rtpressed = false;
             } // making sure it is just pressed once*/

            if (gamepad2.right_stick_y > 0.1 && !ltpressed) {
                //robot.setsClawPosition(clawopen);
                clawopentimer = true;
                clawopentimetarget = System.currentTimeMillis() + 0;
                ltpressed = true;
            }

            if (!(gamepad2.right_stick_y > 0.1)) {
                ltpressed = false;
            }

            if (gamepad1.left_trigger > 0.2 && !ltpressed) {
                //robot.setsClawPosition(clawopen);
                clawopentimer = true;
                clawopentimetarget = System.currentTimeMillis() + 500;
                ltpressed = true;
            }

            if (!(gamepad1.left_trigger > 0.2)) {
                ltpressed = false;
            }

            /**if (gamepad2.b && !bpressed) {
             //robot.setsClawPosition(clawclose);
             clawcaptimer = true;
             clawcaptimetarget = System.currentTimeMillis() + 300;
             bpressed = true;
             }

             if (!gamepad2.b) {
             bpressed = false;
             }

             if (gamepad2.a && !apressed) {
             //robot.setsClawPosition(clawclose);
             clawclosecaptimer = true;
             clawclosecaptimertarget = System.currentTimeMillis() + 300;
             apressed = true;
             }  //0.38 open 0.28 close
             if (!gamepad2.a) {
             apressed = false;
             }*/
            if (clawopencaptimer && System.currentTimeMillis() >= clawopencaptimetarget) {
                //robot.setsClawPosition(clawopen);
                clawopencaptimer = false;
            }

            if (gamepad2.right_bumper && robot.sensorDistance.getDistance(DistanceUnit.CM) > 5.3) { //intake
                robot.wheel1.setPosition(1);
                robot.wheel2.setPosition(0);
            } else if (gamepad2.left_bumper) { //outtake
                robot.wheel1.setPosition(0);
                robot.wheel2.setPosition(1);

            }

            /**else if(gamepad1.left_bumper && robot.sensorDistance.getDistance(DistanceUnit.CM) > 5.3) { //intake
             robot.wheel1.setPosition(1);
             robot.wheel2.setPosition(0);
             }
             else if(gamepad1.right_bumper) { //outtake
             robot.wheel1.setPosition(0);
             robot.wheel2.setPosition(1);
             }*/

            else {
                robot.wheel1.setPosition(0.5);
                robot.wheel2.setPosition(0.5);
            }

            if(gamepad1.x) {
                robot.arm.setTargetPosition(-1000);
                robot.armSetPower(0.8);
            }

            if(gamepad2.dpad_up) {
                armtargetup = robot.arm.getCurrentPosition() - 65;
                robot.arm.setTargetPosition(armtargetup);
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armSetPower(1);
                //robot.arm.getTargetPosition = robot.arm.getCurrentPosition();
            }

            if(gamepad2.dpad_down) {
                armtargetdown = robot.arm.getCurrentPosition() + 65;
                robot.arm.setTargetPosition(armtargetdown);
                robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armSetPower(1);
            }

            if (gamepad1.x && robot.sensorDistance.getDistance(DistanceUnit.CM) > 5.3) { //intake
                robot.wheel1.setPosition(1);
                robot.wheel2.setPosition(0);
            } else if (gamepad1.b) { //outtake
                robot.wheel1.setPosition(0);
                robot.wheel2.setPosition(1);
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
