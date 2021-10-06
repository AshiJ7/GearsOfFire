package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    public BNO055IMU gyro;
    public DcMotor rf;
    public DcMotor rb;
    public DcMotor lf;
    public DcMotor lb;
    public DcMotor wGg;
    //public DcMotor iM;
    public DcMotor intake;
    //public Servo wGa;
    public Servo sCarsl;

    //public ModernRoboticsI2cGyro gyro;
    private static Hardware myInstance = null;

    public double maxSpeed = 0.8;

    public static Hardware getInstance() {
        if(myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }
    public void init(HardwareMap hwMap) {
        /* Return motor */
        try {
            rf =  hwMap.get(DcMotor.class, "rf");
            rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rf.setPower(0);
        }
        catch(Exception p_exception) {
            rf = null;
        }
        try {
            rb = hwMap.get(DcMotor.class, "rb");
            rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            // run using encoders or run with encoders
            rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rb.setPower(0);
        }
        catch(Exception p_exception){
            rb = null;
        }
        try {
            lf = hwMap.get(DcMotor.class, "lf");
            lf.setDirection(DcMotorSimple.Direction.REVERSE);
            lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lf.setPower(0);
        }
        catch(Exception p_exception) {
            lf = null;
        }
        try {
            lb = hwMap.get(DcMotor.class, "lb");
            lb.setDirection(DcMotorSimple.Direction.REVERSE);
            lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lb.setPower(0);
        }
        catch(Exception p_exception) {
            lb = null;
        }
        try {
            gyro = hwMap.get(BNO055IMU.class, "imu");
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
            gyro.initialize(parameters);
        }
        catch(Exception p_exception) {
            gyro = null;
        }

        //motor - wobble
        try {
            wGg = hwMap.get(DcMotor.class, "wGg");
            wGg.setDirection(DcMotorSimple.Direction.REVERSE);
            wGg.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            wGg.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wGg.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            wGg.setPower(0);
        } catch(Exception p_exception) {
            wGg = null;
        }


        //servo - wobble
        /**try {
            wGa = hwMap.get(Servo.class, "wGa");
        } catch(Exception p_exception) {
            wGa = null;
        }**/

        //servo - carousel
        try {
            sCarsl = hwMap.get(Servo.class, "servo carousel");
        }
        catch(Exception p_exception) {
            sCarsl = null;
        }

        /**
         //motor - intake 2020
        try {
            iM = hwMap.get(DcMotor.class, "iM");
            iM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            iM.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            iM.setPower(0);
        }
        catch(Exception p_exception) {
            iM = null;
        }**/

        //motor - intake twirl mechanism
        try {

            intake = hwMap.get(DcMotor.class, "iM");
            intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            intake.setPower(0);
        }
        catch(Exception p_exception) {
            intake = null;
        }
    }

    public void motorWobble (double gGw) {
        if (wGg != null) {
            wGg.setPower(Range.clip(gGw, -maxSpeed, maxSpeed));
        }
    }

    //NEVER SET POSITION >= 1 OR =< 0!!
    /**public void servoWobble (double aGw){
        if (wGa != null) {
            wGa.setPosition(aGw);
        }
    }**/

    //implement servo for carousel
    public void servoCarsl (double carsl){
        if(sCarsl != null) {
            sCarsl.setPosition(carsl);
        }
    }

    /**
    public void powerSet(double Mi){
        if (iM != null) {
            iM.setPower(Range.clip(Mi, -maxSpeed, maxSpeed));
        }
    }**/

    //setting power for intake motor
    public void powerSet(double in){
        if (intake != null) {
            intake.setPower(Range.clip(in, -maxSpeed, maxSpeed));
        }
    }


    public void setPower(double fr, double br, double fl, double bl){
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
}