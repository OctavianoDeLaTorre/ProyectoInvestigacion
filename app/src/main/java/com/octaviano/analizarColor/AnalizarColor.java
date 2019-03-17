package com.octaviano.analizarColor;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class AnalizarColor {

    //public static final Scalar LEUCOCORIA_LEVEL_LOW [] = new Scalar[]{new Scalar(0,38,30), new Scalar(60,60,60)};
    public static final Scalar LEUKOCORIA_LEVEL_LOW [] = new Scalar[]{new Scalar(110,50,50),new Scalar(130,255,255)};
    public static final Scalar LEUKOCORIA_LEVEL_MEDIUM [] = new Scalar[]{new Scalar(0,20,61), new Scalar(60,37,80)};
    public static final Scalar LEUKOCORIA_LEVEL_HIGH [] = new Scalar[]{new Scalar(0,0,81), new Scalar(60,19,100)};

    public Mat analizarColor(Mat imagen, Scalar level[]){
        Mat masck = new Mat();
        Mat imagenHSV = new Mat();
        Imgproc.cvtColor(imagen,imagenHSV,Imgproc.COLOR_BGR2HSV);
        Core.inRange(imagenHSV, level[0],level[1],masck);
        return masck;
    }
}
