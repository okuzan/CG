package HW7;

import Utils.Model;
import com.jogamp.opengl.GL2;

import java.util.ArrayList;

import static com.jogamp.opengl.GL2.GL_POLYGON;

public class FLyingModel extends Model {

    void visual(GL2 gl) {
        for (ArrayList<Integer> face : getF()) {
            gl.glBegin(GL_POLYGON);
            Vertex v1 = getV().get(face.get(0));
            Vertex v2 = getV().get(face.get(1));
            Vertex v3 = getV().get(face.get(2));

            Vertex polyVector1 = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
            Vertex polyVector2 = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);

            Vertex cross = crossProduct(polyVector1, polyVector2);
            cross = normalize(cross);

            gl.glNormal3d(cross.x, cross.y, cross.z);
            gl.glVertex3d(v2.x, v2.y, v2.z);
            gl.glVertex3d(v3.x, v3.y, v3.z);
            gl.glVertex3d(v1.x, v1.y, v1.z);
            gl.glEnd();
        }
    }

    private Vertex normalize(Vertex p) {
        float length = (float) Math.sqrt((p.x * p.x) + (p.y * p.y) + (p.z * p.z));
        return new Vertex(p.x / length, p.y / length, p.z / length);
    }

    private Vertex crossProduct(Vertex v1, Vertex v2) {
        return new Vertex(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }
}
