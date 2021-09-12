package HW5;

import Utils.Model;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RotateModel extends Model {
    private double[][] rotationMatrix;

    private static final int SIZE = 700;
    private static final double dz = Math.toRadians(0);
    private static final double dy = Math.toRadians(0);
    private static final double dx = Math.toRadians(1);

    void rotate(Mat mat) {
        rotationMatrix = finalMatrix();
        while (HighGui.pressedKey == -1) {
            calcVertices();
            draw(mat);
            HighGui.imshow("Rotating Head", mat);
            HighGui.waitKey(30);
            mat = new Mat(mat.cols(), mat.rows(), CvType.CV_8UC3);
        }
        System.exit(0);
    }

    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix) {
        int r1 = firstMatrix[0].length;
        int c1 = firstMatrix.length;
        int c2 = secondMatrix.length;
        double[][] product = new double[r1][c2];
        for (int i = 0; i < r1; i++)
            for (int j = 0; j < c2; j++)
                for (int k = 0; k < c1; k++)
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
        return product;
    }

    public void calcVertices() {
        int i = 0;
        for (Vertex v : getV()) {
            double[] vector = v.getVector();
            double[] updPts = matrixByVector(rotationMatrix, vector);
            getV().set(i++, new Vertex(updPts[0], updPts[1], updPts[2]));
        }
    }

    public double[][] finalMatrix() {
        double[][] mediante = multiplyMatrices(xRotateMatrix(dx), yRotateMatrix(dy));
        return multiplyMatrices(mediante, zRotateMatrix(dz));
    }

    public static double[] matrixByVector(double[][] matrix, double[] vector) {
        return Arrays.stream(matrix)
                .mapToDouble(row -> IntStream.range(0, row.length)
                        .mapToDouble(col -> row[col] * vector[col])
                        .sum()).toArray();
    }


    public double[][] xRotateMatrix(double angle) {
        return new double[][]{
                {1, 0, 0},
                {0, Math.cos(angle), -Math.sin(angle)},
                {0, Math.sin(angle), Math.cos(angle)}};
    }

    public double[][] yRotateMatrix(double angle) {
        return new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)}};
    }

    public double[][] zRotateMatrix(double angle) {
        return new double[][]{
                {Math.cos(angle), -Math.sin(angle), 0},
                {Math.sin(angle), Math.cos(angle), 0},
                {0, 0, 1}};
    }

    public static void main(String[] args) {
        nu.pattern.OpenCV.loadLocally();
        Mat image = new Mat(SIZE, SIZE, CvType.CV_8UC3);
        RotateModel model = new RotateModel();
        model.rotate(image);
    }
}
