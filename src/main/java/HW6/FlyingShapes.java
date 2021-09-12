package HW6;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.FloatBuffer;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.*;

public class FlyingShapes extends JFrame implements GLEventListener, KeyListener {
    private static float[] light_ambient = {0.1f, 0.0f, 0.0f, 1.0f};
    private static float[] light_diffuse = {3f, 2f, 0f, 0f};
    private static float[] light_specular = {1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] light_position = {0.0f, 1.0f, 0.0f, 0.0f};
    private static float[] mat_ambient = {0.7f, 0.7f, 0.7f, 1.0f};
    private static float[] mat_diffuse = {0.8f, 0.8f, 0.8f, 1.0f};
    private static float[] mat_specular = {1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] high_shininess = {100.0f};
    private static float[] fogColor = {0.5f, 0.5f, 0.5f, 1.0f};
    private static int[] fogModes = {GL_EXP, GL_EXP2, GL_LINEAR};
    private boolean effects = true;
    private GLUT glut = new GLUT();
    private GLU glu = new GLU();
    private int currFogFilter = 1;
    private float angle = 0.0f;
    private float rotStep = 0.2f;
    private float density = 0.35f;
    private boolean reClr, makeFog, steady, rotate = true;

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Render Sphere
        gl.glLoadIdentity();
        gl.glTranslatef(1.5f, 0.0f, -7.0f);
        if (!steady) gl.glRotatef(angle, 1.0f, 1.0f, 1.0f);

        gl.glPushMatrix();
        glut.glutSolidSphere(1.5, 100, 100);
        gl.glPopMatrix();

        // Render Torus
        gl.glLoadIdentity();
        gl.glTranslatef(-3f, 0.0f, -6.0f);
        if (!steady) gl.glRotatef(angle, -2f, 0.0f, 0.0f);

        gl.glPushMatrix();
        glut.glutSolidTorus(0.9, 1.5, 100, 100);
        gl.glPopMatrix();

        // Render Сube
        gl.glLoadIdentity();
        gl.glTranslatef(4f, 0.0f, -5.0f);
        if (!steady) gl.glRotatef(angle, 0.5f, 0.5f, 0.5f);

        gl.glPushMatrix();
        glut.glutSolidCube(1.5f);
        gl.glPopMatrix();

        if (rotate) angle += rotStep;

        if (reClr) {
            gl.glColor3d(Math.random(), Math.random(), Math.random());
            reClr = false;
        }

        if (makeFog) gl.glDisable(GL_FOG);
        else {
            gl.glEnable(GL_FOG);
            gl.glFogf(GL_FOG_DENSITY, density);
            gl.glFogi(GL_FOG_MODE, fogModes[currFogFilter]);
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glColor3d(2, 1, 0.8);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        gl.glClearDepth(1.0f);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        gl.glFogfv(GL_FOG_COLOR, fogColor, 0);
        gl.glFogf(GL_FOG_DENSITY, density);
        gl.glHint(GL_FOG_HINT, GL_NICEST);
        gl.glFogf(GL_FOG_START, 1.0f);
        gl.glFogf(GL_FOG_END, 5.0f);
        gl.glEnable(GL_FOG);

        gl.glEnable(GL_CULL_FACE);
        gl.glCullFace(GL_BACK);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, FloatBuffer.wrap((light_ambient)));
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, FloatBuffer.wrap(light_diffuse));
        gl.glLightfv(GL_LIGHT0, GL_SPECULAR, FloatBuffer.wrap(light_specular));
        gl.glLightfv(GL_LIGHT0, GL_POSITION, FloatBuffer.wrap(light_position));
        gl.glMaterialfv(GL_FRONT, GL_AMBIENT, FloatBuffer.wrap(mat_ambient));
        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, FloatBuffer.wrap(mat_diffuse));
        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, FloatBuffer.wrap(mat_specular));
        gl.glMaterialfv(GL_FRONT, GL_SHININESS, FloatBuffer.wrap(high_shininess));
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0)
            height = 1;

        final float h = (float) width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(100.0f, h, 1.0, 40.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        FlyingShapes lib = new FlyingShapes();
        glcanvas.addGLEventListener(lib);
        glcanvas.setSize(700, 700);
        glcanvas.addKeyListener(lib);
        glcanvas.setFocusable(true);
        glcanvas.requestFocus();
        final JFrame frame = new JFrame("Visualisation");
        frame.getContentPane().add(glcanvas);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);
        animator.start();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread(() -> {
                    animator.stop();
                    System.exit(0);
                }
                ).start();
            }
        });
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_T:
                if (e.isAltDown()) {
                    angle = 0;
                    rotStep = 0;
                } else steady = !steady;
                break;

            case VK_S:
                if (e.isShiftDown()) rotStep += 0.1f;
                else rotStep -= 0.1f;
                break;
            case VK_E:
                System.out.println("!");
                effects = !effects;
                break;
            case VK_C:
                reClr = !reClr;
                break;
            case VK_R:
                makeFog = !makeFog;
                break;
            case VK_D:
                if (e.isShiftDown()) density += 0.02f;
                else density -= 0.02f;
                break;
            case VK_F:
                currFogFilter = (currFogFilter + 1) % fogModes.length;
                break;
            case VK_SPACE:
                rotate = !rotate;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }
}