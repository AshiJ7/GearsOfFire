package org.firstinspires.ftc.teamcode;

import android.service.autofill.DateValueSanitizer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Hardware {

    public BNO055IMU gyro;
    public DcMotor rf;
    public DcMotor rb;
    public DcMotor lf;
    public DcMotor lb;
    public DcMotor duckwheel;
    public DcMotor arm;
    public Servo sRetract;
    public Servo wheel1;
    public Servo wheel2;
    String errorMsg;
    DigitalChannel digitalTouch;  // Hardware Device Object
    DistanceSensor sensorDistance;
    ColorSensor sensorcolor;


    //public ModernRoboticsI2cGyro gyro;
    private static Hardware myInstance = null;

    public double maxSpeed = 1;

    private Hardware () {}

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

        //servo for wheel retractor
        try {
            sRetract = hwMap.get(Servo.class, "wheel retractor");
            //sRetract.setDirection(Servo.Direction.REVERSE);
        }
        catch(Exception p_exception) {
            sRetract = null;
            errorMsg = "wheel retractor servo failed";
        }

        //servo for wheel 1
        try {
            wheel1 = hwMap.get(Servo.class, "wheel1");
            //sRetract.setDirection(Servo.Direction.REVERSE);
        }
        catch(Exception p_exception) {
            wheel1 = null;
        }

        //servo for wheel 2
        try {
            wheel2 = hwMap.get(Servo.class, "wheel2");
            //sRetract.setDirection(Servo.Direction.REVERSE);
        }
        catch(Exception p_exception) {
            wheel2 = null;
        }

        //motor for arm
        try {
            arm = hwMap.get(DcMotor.class, "arm motor");
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            arm.setTargetPosition(0);
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //arm.setDirection(DcMotorSimple.Direction.REVERSE);
            arm.setPower(0);
        }
        catch(Exception p_exception) {
            arm = null;
            errorMsg = "arm motor failed";
        }

        //touch sensor
        try {
            digitalTouch = hwMap.get(DigitalChannel.class, "touch sensor");
            digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        }
        catch(Exception p_exception) {
            digitalTouch = null;
            errorMsg = "touch sensor failed";
        }

        //color sensor
        try {
            sensorcolor = hwMap.get(ColorSensor.class, "color sensor");
            sensorDistance = hwMap.get(DistanceSensor.class, "color sensor");
        }
        catch(Exception p_exception) {
            sensorcolor = null;
        }
        //y.addData("Error Message: ", errorMsg);

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

    public void armSetPower(double apow) {
        if (arm != null) {
            arm.setPower(apow);
        }
    }

    public void setwheel1Pos(double pos) {
        if (wheel1 != null) {
            wheel1.setPosition(pos);
        }
    }

    public void setwheel2Pos(double pos) {
        if (wheel1 != null) {
            wheel1.setPosition(pos);
        }
    }

    public void setsRetract(double pos) {
        if(sRetract != null) {
            sRetract.setPosition(pos);
        }
    }
}
