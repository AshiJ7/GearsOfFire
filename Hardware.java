package org.firstinspires.ftc.teamcode;

import android.service.autofill.DateValueSanitizer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    public BNO055IMU gyro;
    public DcMotor rf;
    public DcMotor rb;
    public DcMotor lf;
    public DcMotor lb;
    public DcMotor intake;
    public DcMotor duckwheel;
    public DcMotor arm;
    public Servo sClaw;

    //public ModernRoboticsI2cGyro gyro;
    private static Hardware myInstance = null;

    public double maxSpeed = 1;

    public static Hardware getInstance() {
        if (myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }

    public void init(HardwareMap hwMap) {

        //right front
        try {
            rf = hwMap.get(DcMotor.class, "rf");
            rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rf.setPower(0);
        } catch (Exception p_exception) {
            rf = null;
        }

        //right back
        try {
            rb = hwMap.get(DcMotor.class, "rb");
            rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // run using encoders or run with encoders
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rb.setPower(0);
        } catch (Exception p_exception) {
            rb = null;
        }

        //left front
        try {
            lf = hwMap.get(DcMotor.class, "lf");
            lf.setDirection(DcMotorSimple.Direction.REVERSE);
            lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lf.setPower(0);
        } catch (Exception p_exception) {
            lf = null;
        }

        //left back
        try {
            lb = hwMap.get(DcMotor.class, "lb");
            lb.setDirection(DcMotorSimple.Direction.REVERSE);
            lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lb.setPower(0);
        } catch (Exception p_exception) {
            lb = null;
        }

        //gyro
        try {
            gyro = hwMap.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
            gyro.initialize(parameters);
        } catch (Exception p_exception) {
            gyro = null;
        }

        //motor for carousel
        try {
            duckwheel = hwMap.get(DcMotor.class, "carousel motor");
            duckwheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            duckwheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            duckwheel.setPower(0);
        }
        catch(Exception p_exception) {
            duckwheel = null;
        }

        //motor for intake
        try {
            intake = hwMap.get(DcMotor.class, "intake motor");
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            intake.setPower(0);
        }
        catch(Exception p_exception) {
            intake = null;
        }

        //servo for claw
        try {
            sClaw = hwMap.get(Servo.class, "claw servo");
            sClaw.setDirection(Servo.Direction.REVERSE);
        }
        catch (Exception p_exception) {
            sClaw = null;
        }

        //motor for arm
        try {
            arm = hwMap.get(DcMotor.class, "arm motor");
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setPower(0);
        }
        catch(Exception p_exception) {
            arm = null;
        }

    }

    public void setPower ( double fr, double br, double fl, double bl){
        if (rf != null) {
            rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        }
        if (rb != null) {
            rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
        }
        if (lf != null) {
            lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        }
        if (lb != null) {
            lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));
        }
    }

    public void duckPower(double dpow) {
        if (duckwheel != null) {
            duckwheel.setPower(dpow);
        }
    }

    public void intakeSetPower(double power) {
        if (intake != null) {
            intake.setPower(Range.clip(power, -maxSpeed, maxSpeed));
        }
    }

    public void armSetPower(double apow) {
        if (arm != null) {
            arm.setPower(apow);
        }
    }

     public void setsClawPosition(double pos) {
        if (sClaw != null) {
            sClaw.setPosition(pos);
        }
     }
}
