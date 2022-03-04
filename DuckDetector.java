package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Rect;

public class DuckDetector extends OpenCvPipeline {

    private Mat workingMatrix = new Mat();
    public String level = "failed :(";
    public double leftTotal = 0;
    //public double twoTotal = 0;
    public double centerTotal = 0;
    public double rightTotal = 0;

    public DuckDetector () {
    }

    @Override
    public final Mat processFrame(Mat input) {
        input.copyTo(workingMatrix);

        if (workingMatrix.empty()) {
            return input;
        }

        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb);

        //Mat leftmat = workingMatrix.submat(120, 150, 10, 150);
        //Mat centremat = workingMatrix.submat(120, 150, 80, 120);
        //Mat rightmat = workingMatrix.submat(120, 150, 150, 190);

        //Imgproc.rectangle(workingMatrix, new Rect(10, 120, 40, 30), new Scalar(0, 255, 0));
        //Imgproc.rectangle(workingMatrix, new Rect(80, 120, 40, 30), new Scalar(0, 255, 0));
        //Imgproc.rectangle(workingMatrix, new Rect(150, 120, 40, 30), new Scalar(0, 255, 0));

        //rowstart, rowend, colstart, colend
        //Mat leftmat = workingMatrix.submat(100, 220, 20, 110);
        //Mat centremat = workingMatrix.submat(100, 220, 105, 195);
        //Mat rightmat = workingMatrix.submat(100, 220,200, 290);

        Mat leftmat = workingMatrix.submat(125, 220, 20, 100);
        Mat centermat = workingMatrix.submat(125, 220, 150, 230);
        Mat rightmat = workingMatrix.submat(125, 220,240, 320);

        Imgproc.rectangle(workingMatrix, new Rect(20, 125, 80, 95), new Scalar(0, 255, 0));
        Imgproc.rectangle(workingMatrix, new Rect(150, 125, 80, 95), new Scalar(0, 255, 0));
        Imgproc.rectangle(workingMatrix, new Rect(240, 125, 80, 95), new Scalar(0, 255, 0));

        //Imgproc.rectangle(workingMatrix, new Rect(0, 0, 30, 30), new Scalar(0, 255, 0));
        //x, y, width, height
        //Imgproc.rectangle(workingMatrix, new Rect(5, 150, 70, 80), new Scalar(0, 255, 0));
        //Imgproc.rectangle(workingMatrix, new Rect(120, 150, 70, 80), new Scalar(0, 255, 0));
        //Imgproc.rectangle(workingMatrix, new Rect(250, 150, 70, 80), new Scalar(0, 255, 0));

        leftTotal = Core.sumElems(leftmat).val[2];
        leftTotal /= leftmat.cols() * leftmat.rows();
        centerTotal = Core.sumElems(centermat).val[2];
        centerTotal /= centermat.cols() * centermat.rows();
        rightTotal = Core.sumElems(rightmat).val[2];
        rightTotal /= rightmat.cols() * rightmat.rows();

        if (leftTotal < centerTotal) {
            if (leftTotal < rightTotal) {
                //left has duck
                level = "ONE";
            }
            else {
                //right has duck
                level = "THREE";
            }
        }
        else {
            if (centerTotal < rightTotal) {
                //centre has duck
                level = "TWO";
            }
            else {
                //right has duck
                level = "THREE";
            }
        }
        return workingMatrix;
    }
}