package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "autored1", group = "auto")
public class autored1 extends LinearOpMode {

    Hardware robot = Hardware.getInstance();
    public AutoGyro movegyro = new AutoGyro();
    public AutoEncoder moveencoder = new AutoEncoder();
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {
        robot.init(hardwareMap);

        waitForStart();
        //move towards carousel
        moveencoder.Drive(0.8, 5, -5, -5, 5);
        movegyro.Drive(0.8, 19, 0, robot, telemetry);


        //move.DriveEncoder(0.5, -3, 3, 3, -3);

        runtime.reset();

        //drop duck from crsl
        while (opModeIsActive() && (runtime.seconds() < 3.0))
        {
            robot.setsCarslPosition(-0.6);
            //telemetry.update("Servo Position: ", robot.sCarsl.getPosition());
        }
        runtime.reset();

        movegyro.Drive(0.7, -10, 0, robot, telemetry);

        moveencoder.Drive(0.9, -8, 8, 8, -8);

        movegyro.Drive(0.6, -40, 0, robot, telemetry);


        // move.DriveEncoder(0.5, -50, -50, -50, -50);

        //spin around
        //move.Drive(0.5, 0, 180, robot, telemetry);

        //go to alliance shipping hub
        //move.Drive(0.5, 45, 20, robot, telemetry);

        //use servo here to drop freight onto shipping hub - elevator mechanism

        //go to freight
        //move.Drive(0.5, 45, 60, robot, telemetry);

        //pick up freight with intake


        //spin around
        //move.Drive(0.5, 0, 180, robot, telemetry);

        //go back to alliance shipping hub
        //move.Drive(0.5, 20, 90, robot, telemetry);

        //drop freight onto shipping hub


        //spin around
        //move.Drive(0.5, 0, 100, robot, telemetry);

        //park into warehouse
        //move.Drive(0.5, 50, 10, robot, telemetry);
    }
}