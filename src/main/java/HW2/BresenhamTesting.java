package HW2;

import Utils.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.util.ArrayList;

public class BresenhamTesting {

    private static final int SIZE = 700;
    private static final int CIRCLE_X = SIZE / 2;
    private static final int CIRCLE_Y = SIZE / 2;
    private static final int CIRCLE_RADIUS = SIZE / 4;

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadLocally();
        //4 images
        Mat headSimpleImg = new Mat(SIZE, SIZE, CvType.CV_8UC3);
        Mat headBresImg = new Mat(SIZE, SIZE, CvType.CV_8UC3);
        Mat bresGeomImg = new Mat(SIZE, SIZE, CvType.CV_8UC3);
        Mat simpleGeomImg = new Mat(SIZE, SIZE, CvType.CV_8UC3);

        //triangle and head model loading and writing
        Model triangle = new Model("src/main/resources/head/triangle.obj");
        Model head = new Model("src/main/resources/head/head.obj");
        drawModel(head, headSimpleImg, false);
        drawModel(head, headBresImg, true);

        //bresenham geometry (triangle from file and circle)
        drawModel(triangle, bresGeomImg, true);
        Bresenham.drawCircle(bresGeomImg, CIRCLE_X, CIRCLE_Y, CIRCLE_RADIUS);
        //simple geometry (triangle from file and circle)
        drawModel(triangle, simpleGeomImg, false);
        Bresenham.simpleCircle(simpleGeomImg, CIRCLE_X, CIRCLE_Y, CIRCLE_RADIUS);

        //displaying
        while (HighGui.pressedKey == -1) {
            HighGui.imshow("Circle Bresenham", bresGeomImg);
            HighGui.imshow("Simple Circle", simpleGeomImg);
            HighGui.imshow("Simple Head", headSimpleImg);
            HighGui.imshow("Bresenham Head", headBresImg);
            HighGui.waitKey(50);
        }
        System.exit(0);
    }

    private static void drawModel(Model model, Mat image, boolean bresenham) {
        for (int i = 0; i < model.getF().size(); i++) {
            ArrayList<Integer> face = model.getF().get(i);
            for (int j = 0; j < 3; j++) {
                Model.Vertex v0 = model.getV().get(face.get(j));
                Model.Vertex v1 = model.getV().get(face.get((j + 1) % 3));
                int x0 = (int) ((v0.x + 1) * SIZE / 2);
                int y0 = (int) ((v0.y + 1) * SIZE / 2);
                int x1 = (int) ((v1.x + 1) * SIZE / 2);
                int y1 = (int) ((v1.y + 1) * SIZE / 2);
                if (!bresenham) Bresenham.simpleLine(image, SIZE - y0, x0, SIZE - y1, x1);
                else Bresenham.drawLine(image, SIZE - y0, x0, SIZE - y1, x1);
            }
        }
    }
}