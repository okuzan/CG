package Utils;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {

    private ArrayList<Vertex> vertices;
    private ArrayList<Vertex> normals;
    private ArrayList<ArrayList<Integer>> faces;
    private static final String HEAD_PATH = "src/main/resources/head/head.obj";
    private static final String V_MASK = "v ";
    private static final String F_MASK = "f ";
    private static final String N_MASK = "n ";

    public Model() {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        loadModel(HEAD_PATH);
    }

    public Model(String filename) {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
        loadModel(filename);
    }

    private void loadModel(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) continue;
                if (line.substring(0, 2).equals(V_MASK))
                    addVertex(line);
                if (line.substring(0, 2).equals(F_MASK))
                    addFace(line);
                if (line.substring(0, 2).equals(N_MASK))
                    addNormal(line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFace(String line) {
        ArrayList<Integer> indices = new ArrayList<>();
        String faceLine = line.substring(2);
        String[] faceArray = faceLine.split(" ");
        for (String s : faceArray) {
            String[] indicesArray = s.split("/");
            int index = Integer.parseInt(indicesArray[0]);
            index--;
            indices.add(index);
        }
        faces.add(indices);
    }

    private void addVertex(String line) {
        String[] vertexArray = line.split(" ");
        Vertex vertex = new Vertex();
        vertex.x = Double.parseDouble(vertexArray[1]);
        vertex.y = Double.parseDouble(vertexArray[2]);
        vertex.z = Double.parseDouble(vertexArray[3]);
        vertices.add(vertex);
    }

    private void addNormal(String line) {
        String[] normalsArr = line.split(" ");
        Vertex vertex = new Vertex();
        vertex.x = Float.parseFloat(normalsArr[1]);
        vertex.y = Float.parseFloat(normalsArr[2]);
        vertex.z = Float.parseFloat(normalsArr[3]);
        normals.add(vertex);
    }

    public void draw(Mat mat) {
        int SIZE = mat.cols();
        for (ArrayList<Integer> face : faces)
            for (int j = 0; j < 3; j++) {
                Vertex v0 = vertices.get(face.get(j));
                Vertex v1 = vertices.get(face.get((j + 1) % 3));
                int x0 = (int) ((v0.x + 1) * SIZE / 2);
                int y0 = (int) ((v0.y + 1) * SIZE / 2);
                int x1 = (int) ((v1.x + 1) * SIZE / 2);
                int y1 = (int) ((v1.y + 1) * SIZE / 2);
                Imgproc.line(mat, new Point(x0, SIZE - y0), new Point(x1, SIZE - y1),
                        new Scalar(125, 125, 125), 2);
            }
    }

    public static class Vertex {
        public double x;
        public double y;
        public double z;

        public Vertex() {
        }

        public Vertex(double a, double b, double c) {
            x = a;
            y = b;
            z = c;
        }

        public double[] getVector() {
            double[] vector = new double[3];
            vector[0] = x;
            vector[1] = y;
            vector[2] = z;
            return vector;
        }
    }

    public ArrayList<Vertex> getV() {
        return vertices;
    }

    public ArrayList<ArrayList<Integer>> getF() {
        return faces;
    }
}
