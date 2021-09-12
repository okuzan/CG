package HW3;

import Utils.Model;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.util.ArrayList;

public class FillModel extends Model {
    private static final int SIZE = 700;

    private void fillFaces(Mat image) {
        for (int i = 0; i < getF().size(); i++) {
            ArrayList<Integer> faces = getF().get(i);
            Point[] screenCoords = new Point[3];
            for (int j = 0; j < 3; j++) {
                Model.Vertex worldCoords = getV().get(faces.get(j));
                screenCoords[j] = new Point((worldCoords.x + 1) * SIZE / 2,
                        Math.abs(SIZE - (worldCoords.y + 1) * SIZE / 2));
            }
            draw(screenCoords, image,
                    new Color((int) (Math.random() * 255),
                            (int) (Math.random() * 255),
                            (int) (Math.random() * 255)));
        }
    }

    public void draw(Point[] screenCoords, Mat image, Color color) {

        if (screenCoords[0].y > screenCoords[1].y) swapPoints(screenCoords[0], screenCoords[1]);
        if (screenCoords[0].y > screenCoords[2].y) swapPoints(screenCoords[0], screenCoords[2]);
        if (screenCoords[1].y > screenCoords[2].y) swapPoints(screenCoords[1], screenCoords[2]);

        Point A = screenCoords[0];
        Point B = screenCoords[1];
        Point C = screenCoords[2];

        double dx1, dx2, dx3;
        if (B.y - A.y > 0) dx1 = (B.x - A.x) / (B.y - A.y);
        else dx1 = 0;
        if (C.y - A.y > 0) dx2 = (C.x - A.x) / (C.y - A.y);
        else dx2 = 0;
        if (C.y - B.y > 0) dx3 = (C.x - B.x) / (C.y - B.y);
        else dx3 = 0;

        Point E = new Point(A.x, A.y);
        Point S = new Point(A.x, A.y);

        if (dx1 > dx2) {
            for (; S.y <= B.y; S.y++, E.y++, S.x += dx2, E.x += dx1)
                Imgproc.line(image, new Point(S.x, S.y), new Point(E.x, S.y),
                        new Scalar(color.getRed(), color.getGreen(), color.getBlue()), 1);
            E = B;
            for (; S.y <= C.y; S.y++, E.y++, S.x += dx2, E.x += dx3)
                Imgproc.line(image, new Point(S.x, S.y), new Point(E.x, S.y),
                        new Scalar(color.getRed(), color.getGreen(), color.getBlue()), 1);
        } else {
            for (; S.y <= B.y; S.y++, E.y++, S.x += dx1, E.x += dx2)
                Imgproc.line(image, new Point(S.x, S.y), new Point(E.x, S.y),
                        new Scalar(color.getRed(), color.getGreen(), color.getBlue()), 1);
            S = B;
            for (; S.y <= C.y; S.y++, E.y++, S.x += dx3, E.x += dx2)
                Imgproc.line(image, new Point(S.x, S.y), new Point(E.x, S.y),
                        new Scalar(color.getRed(), color.getGreen(), color.getBlue()), 1);
        }
    }

    public void draw2(Point[] screenCoords, Mat image, Color color) {
        ArrayList<MatOfPoint> al = new ArrayList<>();
        MatOfPoint sourceMat = new MatOfPoint();
        sourceMat.fromArray(screenCoords);
        al.add(sourceMat);
        Imgproc.fillPoly(image, al, new Scalar(color.getRed(), color.getGreen(), color.getBlue()));
    }

    public static void swapPoints(Point p1, Point p2) {
        double xTemp = p1.x;
        double yTemp = p1.y;
        p1.x = p2.x;
        p1.y = p2.y;
        p2.x = xTemp;
        p2.y = yTemp;
    }


    public static void main(String[] args) {
        while (HighGui.pressedKey == -1) {
            nu.pattern.OpenCV.loadLocally();
            Mat headImg = new Mat(SIZE, SIZE, CvType.CV_8UC3);
            FillModel model = new FillModel();
            model.fillFaces(headImg);
            HighGui.imshow("Coloured", headImg);
            HighGui.waitKey(100);
        }
        System.exit(0);
    }
}
