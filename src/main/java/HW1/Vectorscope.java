package HW1;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Vectorscope {

    private static Mat original, vectorscope;
    private static final int SIZE = 500;

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadLocally();
        vectorscope = new Mat(SIZE, SIZE, CvType.CV_8UC3);
        createMap();
        original = Imgcodecs.imread("src/main/resources/vector/fot.jfif");
        Mat transformed = transform(original);
        Imgcodecs.imwrite("src/main/resources/res/myYCbCr.tif", transformed);
        Imgcodecs.imwrite("src/main/resources/res/vectorscope.tif", vectorscope);

    }

    private static Mat transform(Mat input) {
        Mat transformed = new Mat(input.rows(), input.cols(), CvType.CV_8UC3);

        for (int i = 0; i < input.rows(); i++)
            for (int j = 0; j < input.cols(); j++) {
                transformed.put(i, j, util(i, j));
            }

        Imgproc.cvtColor(original, transformed, Imgproc.COLOR_RGB2YCrCb);
        return transformed;
    }

    private static double[] util(int i, int j) {

        double[] rbg = original.get(i, j);
        float fr = (float) rbg[0];
        float fg = (float) rbg[1];
        float fb = (float) rbg[2];
        float Y = (float) (0.299 * +0.587 * fg + 0.114 * fb);
        float Cb = (float) (-0.169 * fr - 0.331 * fg + 0.500 * fb);
        float Cr = (float) (0.500 * fr - 0.418 * fg - 0.082 * fb);
        makePoint(Cb, Cr);
        double[] newd = {Y, Cb, Cr};
        return newd;
    }

    private static void drawText(Mat vectorscope) {
        Imgproc.putText(
                vectorscope,
                "Cr",
                new Point(SIZE / 2 + SIZE / 30, SIZE * 0.1 + SIZE / 10),
                Core.FONT_HERSHEY_SIMPLEX,
                1,
                new Scalar(0, 0, 255),
                2
        );
        Imgproc.putText(
                vectorscope,
                "Cb",
                new Point(SIZE * 0.9 - SIZE / 10, SIZE / 2 - SIZE / 30),
                Core.FONT_HERSHEY_SIMPLEX,
                1,
                new Scalar(0, 0, 255),
                2
        );
    }

    private static void makePoint(float cb, float cr) {
        int center = SIZE / 2;
        Imgproc.line(
                vectorscope,
                new Point(center + cr, center - cb),
                new Point(center + cr, center - cb),
                new Scalar(0, 255, 0),
                1
        );
    }


    private static void createMap() {
        Mat mat = vectorscope;

        drawText(mat);
        Imgproc.line(mat,
                new Point(SIZE * 0.1, SIZE / 2),
                new Point(SIZE * 0.9, SIZE / 2),
                new Scalar(0, 0, 255),
                2
        );

        Imgproc.line(mat,
                new Point(SIZE / 2, SIZE * 0.1),
                new Point(SIZE / 2, SIZE * 0.9),
                new Scalar(0, 0, 255),
                2
        );

        Point center = new Point(SIZE / 2, SIZE / 2);
        Imgproc.circle(mat, center, (int) (SIZE * 0.4), new Scalar(0, 0, 255), 2);
    }
}


