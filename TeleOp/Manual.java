package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

        waitForStart();
        while(opModeIsActive())
        {
            drive = -gamepad1.left_stick_y;
            turn = -gamepad1.right_stick_x;
            strafe = -gamepad1.left_stick_x;

            //move carousel servo with a and b - a for blue, b for red
            if ((gamepad1.a)) {
                robot.setsCarslPosition(0.9);
            }

                else if((gamepad1.b)) {
                    robot.setsCarslPosition(-0.6);
                }
                else{
                    robot.setsCarslPosition(0.5);
                }


                //set power to intake motor with right bumper
            if (gamepad1.right_bumper) {
                robot.intakeSetPower(0.7);
            }
            else {
                robot.intakeSetPower(0);
            }

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