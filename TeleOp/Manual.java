package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="TeleOp", group="Robot")

public class Manual extends LinearOpMode {
    Hardware robot = Hardware.getInstance();
    private ElapsedTime run = new ElapsedTime();

    public void runOpMode()  {
        double drive = 0;
        double turn = 0;
        double max;
        double strafe = 0;
        boolean pressingrt = false;
        boolean pressinglt = false;
        boolean moveForward = false;
        boolean moveBackward = false;
        boolean servostatus = false;
        boolean pressingdup = false;
        boolean pressingddown = false;

        robot.init(hardwareMap);

        //int number = 0;
        //robot.intake.setTargetPosition(0);
        //robot.intake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //robot.intakeSetPower(0.35);

        waitForStart();
        while(opModeIsActive())
        {
            drive = -gamepad1.left_stick_y;
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.left_stick_x;

            //intake = gamepad2.left_stick_y;

            //move carousel servo with a and b - a for blue, b for red
            /**
            if ((gamepad1.a)) {
                robot.setsCarslPosition(0.9);
            }

            else if((gamepad1.b)) {
                robot.setsCarslPosition(-0.6);
            }
            else {
                robot.setsCarslPosition(0.5);
            }*/


            //move carousel motor with b and a - b for blue, a for red
             if((gamepad2.dpad_down)) {
                robot.duckSetPower(0.8);
            }
            else {
                robot.intakeSetPower(0);
            }

            if((gamepad2.dpad_up)) {
                robot.duckSetPower(-0.8);
            }
            else {
                robot.intakeSetPower(0);
            }


            //set power to intake motor with right bumper
            if (gamepad2.right_bumper) {
                robot.intakeSetPower(-1);
                telemetry.addData("Intake Motor Power: ", robot.intake.getPower());
                telemetry.update();
            }

            else {
                robot.intakeSetPower(0);
            }

            if (gamepad2.left_bumper) {
                robot.intakeSetPower(1);
                telemetry.addData("Intake Motor Power: ", robot.intake.getPower());
                telemetry.update();
            }
            else {
                robot.intakeSetPower(0);
            }

            //move servo box up and down
            if (gamepad2.y) {
                robot.setsIntPosition(0.15);
            }
            if(gamepad2.a) {
                robot.setsIntPosition(0.44);
            }
            if(gamepad2.b)
            {
                robot.setsIntPosition(0.3);
            }
            /**
            else if (gamepad2.dpad_down) {
                robot.setsIntPosition(0.5);
            }
            else {
                robot.setsIntPosition(0.5);
            }*/

            //set power to intake motor, slower speed
            /**if(gamepad2.dpad_up) {
                robot.intakeSetPower(-0.4);
            }
            else {
                robot.capSetPower(0);
            }

            if(gamepad2.dpad_down) {
                robot.capSetPower(0.4);
            }
            else {
                robot.capSetPower(0);
            }*/

            /**
             if (gamepad1.right_bumper) {
             robot.intake.setTargetPosition(400);
             while((robot.intake.getCurrentPosition() + 10 < robot.intake.getTargetPosition()) ||
             (robot.intake.getCurrentPosition() - 10 > robot.intake.getTargetPosition()))
             {
             robot.intakeSetPower(0.3);
             }
             }
             else {
             robot.intakeSetPower(0);
             }
             if (gamepad1.left_bumper) {
             robot.intake.setTargetPosition(0);
             while((robot.intake.getCurrentPosition() + 10 < robot.intake.getTargetPosition()) ||
             (robot.intake.getCurrentPosition() - 10 > robot.intake.getTargetPosition()))
             {
             robot.intakeSetPower(0.3);
             }
             }
             else {
             robot.intakeSetPower(0);
             }*/

            /**
             //move intake servo to grab freight
             if ((gamepad1.dpad_up) && !pressingdup) {
             if (servostatus) {
             robot.setsIntPosition(0.6);
             servostatus = false;
             } else {
             robot.setsIntPosition(0.5);
             servostatus = true;
             }
             pressingdup = true;
             } else if (!(gamepad1.dpad_up)) {
             //action
             pressingdup = false;
             }*/

            /**
             * //move intake servo to hold freight
             if ((gamepad1.dpad_down) && !pressingddown) {
             if (servostatus) {
             robot.setsIntPosition(-0.6);
             servostatus = false;
             } else {
             robot.setsIntPosition(0.5);
             servostatus = true;
             }
             pressingddown = true;
             } else if (!(gamepad1.dpad_down)) {
             //action
             pressingddown = false;
             }

             //move forward slowly with right trigger
             if ((gamepad1.right_trigger > 0.1) && !pressingrt) {
             if (moveForward) {
             robot.setPower(0.35, 0.35, 0.35, 0.35);

             while (gamepad1.right_trigger > 0.1) {
             drive = 0.35;
             }
             moveForward = false;
             }
             else {
             moveForward = true;
             robot.setPower(-0.35,-0.35,-0.35,-0.35);
             drive = -0.35;
             }
             pressingrt = true;

             } else if (!(gamepad1.right_trigger > 0.1)) {
             //action
             pressingrt = false;
             }


             //move backward slowly with left trigger
             if ((gamepad1.left_trigger > 0.1) && !pressinglt) {
             if (moveBackward) {
             robot.setPower(-0.35, -0.35, -0.35, -0.35);

             while (gamepad1.left_trigger > 0.1) {
             drive = -0.35;
             }
             moveBackward = false;
             }
             else {
             moveBackward = true;
             robot.setPower(0.35,0.35,0.35,0.35);
             drive = 0.35;
             }
             pressinglt = true;

             } else if (!(gamepad1.left_trigger > 0.1)) {
             //action
             pressinglt = false;
             }*/


            //decrease forward speed slowly with right trigger
            if ((gamepad1.right_trigger > 0.1) && !pressingrt) {
                if (moveForward) {
                    robot.setPower(0.35, 0.35, 0.35, 0.35);

                    int counter = 0;
                    while (gamepad1.right_trigger > 0.1) {
                        drive = 1-(counter*0.5);
                        counter++;
                    }
                    moveForward = false;
                }
                else {
                    moveForward = true;
                    robot.setPower(-0.35,-0.35,-0.35,-0.35);
                    drive = -0.35;
                }
                pressingrt = true;

            } else if (!(gamepad1.right_trigger > 0.1)) {
                //action
                pressingrt = false;
            }


            //decrease backward speed slowly with left trigger
            if ((gamepad1.left_trigger > 0.1) && !pressinglt) {
                if (moveBackward) {
                    robot.setPower(-0.35, -0.35, -0.35, -0.35);

                    int counter = 0;
                    while (gamepad1.left_trigger > 0.1) {
                        drive = -(1- (counter*0.5));
                        counter++;
                    }
                    moveBackward = false;
                }
                else {
                    moveBackward = true;
                    robot.setPower(0.35,0.35,0.35,0.35);
                    drive = 0.35;
                }
                pressinglt = true;

            } else if (!(gamepad1.left_trigger > 0.1)) {
                //action
                pressinglt = false;
            }

            //movement
            max = Math.max(Math.abs(drive - strafe - turn), Math.max(Math.abs(drive + strafe - turn),
                    Math.max(Math.abs(drive + strafe + turn), Math.abs(drive + turn - strafe))));
            if (max < robot.maxSpeed) {
                robot.setPower(drive - strafe - turn, drive + strafe - turn,
                        drive + strafe + turn, drive + turn - strafe);
            }
            else {
                double scaleFactor = max / robot.maxSpeed;
                robot.setPower((drive - strafe - turn) * scaleFactor, (drive + strafe - turn) * scaleFactor,
                        (drive + strafe + turn) * scaleFactor, (drive + turn - strafe) * scaleFactor);
            }

        }//close while loop for opmode is active
    }//close opmode
}//close class
