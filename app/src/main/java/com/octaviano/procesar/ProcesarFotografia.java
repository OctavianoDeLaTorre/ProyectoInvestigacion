package com.octaviano.procesar;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ProcesarFotografia {

    public Mat getGrayScale(Mat mat){
        Mat matGray = new Mat();
        Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_BGR2GRAY);
        return matGray;
    }

    public Mat getCannyEdges(Mat mat,Mat matCanny){
        Imgproc.Canny(mat, matCanny, 10, 100);
        return matCanny;
    }

    public Mat getGaussianBlur(Mat mat,Mat matGB){
        Imgproc.GaussianBlur(mat, matGB, new Size(9,9), 3);
        return matGB;
    }

    public Mat toMat(Bitmap bitmap){
        Mat mat = new Mat();
        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);
        return mat;
    }

    public Bitmap toBitmap(Mat mat, Bitmap bitmap){
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

    public Mat drawCircles(Mat mat,Mat circles){
        for (int i = 0; i < circles.cols(); i++) {
            double vCircle[] = circles.get(0, i);
            Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            int radius = (int) Math.round(vCircle[2]);
            Imgproc.circle(mat, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
            // circle outline
            Imgproc.circle(mat, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
        }
        return mat;
    }
}
