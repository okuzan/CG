package HW4;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class JuliaSet {
    private static final int W = 800;
    private static final int H = 800;
    private static final int maxIter = 1500;

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadLocally();
        while (HighGui.pressedKey == -1) {

            Mat image = new Mat(W, H, CvType.CV_8UC3);
            drawJuliaSet(image);
//        Imgcodecs.imwrite("src/main/resources/res/julia.tif", image);
            HighGui.imshow("My Result", image);
            HighGui.waitKey();
        }
        System.exit(0);
    }

    public static void drawJuliaSet(Mat image) {
        double cX = -0.7;
        double cY = 0.27015;
        double moveX = 0, moveY = 0;
        double zx, zy;
        double zoom = 1.0;

        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                zx = 1.5 * (x - W / 2) / (0.5 * zoom * W) + moveX;
                zy = (y - H / 2) / (0.5 * zoom * H) + moveY;
                float i = maxIter;
                while (zx * zx + zy * zy < 4 && i > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    i--;
                }
                float[] arrColor = {(maxIter / i) % 1, 1, i > 0 ? 1 : 0};
                image.put(y, x, hsvToRgb(arrColor[0], arrColor[1], (int) arrColor[2]));
            }
        }
    }

    public static double[] hsvToRgb(float hue, float saturation, float value) {

        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0:
                return rgbToString(value, t, p);
            case 1:
                return rgbToString(q, value, p);
            case 2:
                return rgbToString(p, value, t);
            case 3:
                return rgbToString(p, q, value);
            case 4:
                return rgbToString(t, p, value);
            case 5:
                return rgbToString(value, p, q);
            default:
                throw new RuntimeException("Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static double[] rgbToString(float r, float g, float b) {
        double rs = r * 256;
        double bs = g * 256;
        double gs = b * 256;
        return new double[]{rs, gs, bs};
    }
}
