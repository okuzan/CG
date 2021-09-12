package HW7;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.FloatBuffer;

import static com.jogamp.newt.event.KeyEvent.*;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.*;

public class FlyingHead extends JFrame implements GLEventListener, KeyListener {
    private static float[] light_ambient = {0.1f, 0.0f, 0.0f, 1.0f};
    private static float[] light_diffuse = {3f, 2f, 0f, 0f};
    private static float[] light_specular = {1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] light_position = {1.0f, 0.0f, 0.0f, 0.0f};
    private static float[] mat_ambient = {0.7f, 0.7f, 0.7f, 1.0f};
    private static float[] mat_diffuse = {0.8f, 0.8f, 0.8f, 1.0f};
    private static float[] mat_specular = {1.0f, 1.0f, 1.0f, 1.0f};
    private static float[] fogColor = {0.5f, 0.5f, 0.5f, 1.0f};
    private static float[] high_shininess = {100.0f};
    private static int[] fogModes = {GL_EXP, GL_EXP2, GL_LINEAR};
    private float angleX = 0.0f, angleY = 0.0f, angleZ = 0.0f;
    private float rotStepX = 0.2f, rotStepY = 0.2f, rotStepZ = 0.2f;
    private boolean steady, reClr, makeFog, rotate = true;
    private float density = 0.35f;
    private int currFogFilter = 1;
    private static FLyingModel model;
    private static GLU glu = new GLU();

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -1.5f);

        if (!steady) {
            gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
            gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);
        }

        //getting model matrices
        gl.glPushMatrix();
        model.visual(gl);
        gl.glPopMatrix();

        if (rotate) {
            angleX += rotStepX;
            angleY += rotStepY;
            angleZ += rotStepZ;
        }

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
        gl.glColor3d(2, 0.3, 0.8);
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        gl.glClearDepth(1.0f);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        gl.glEnable(GL_FOG);
        gl.glFogfv(GL_FOG_COLOR, fogColor, 0);
        gl.glFogf(GL_FOG_DENSITY, density);
        gl.glHint(GL_FOG_HINT, GL_NICEST);
        gl.glFogf(GL_FOG_START, 1.0f);
        gl.glFogf(GL_FOG_END, 5.0f);

        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glOrtho(-200.0, 200.0, -200.0, 200.0, -5.0, 5.0);

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
        final float h = (float) width / height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(85.0, h, 0.1, 100.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {
        model = new FLyingModel();
        setCanvas();
    }

    private static void setCanvas() {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        FlyingHead lib = new FlyingHead();
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
            case VK_S:
                if (e.isAltDown()) {
                    angleX = angleY = angleZ = 0;
                    rotStepX = rotStepY = rotStepZ = 0;
                } else steady = !steady;
                break;
            case VK_D:
                if (e.isShiftDown()) density -= 0.02f;
                else density += 0.02f;
                break;
            case VK_F:
                currFogFilter = (currFogFilter + 1) % fogModes.length;
                break;
            case VK_SPACE:
                rotate = !rotate;
                break;
            case VK_C:
                reClr = !reClr;
                break;
            case VK_R:
                makeFog = !makeFog;
                break;
            case VK_X:
                if (e.isShiftDown()) rotStepX += 0.1f;
                else rotStepX -= 0.1f;
                break;
            case VK_Y:
                if (e.isShiftDown()) rotStepY += 0.1f;
                else rotStepY -= 0.1f;
                break;
            case VK_Z:
                if (e.isShiftDown()) rotStepZ += 0.1f;
                else rotStepZ -= 0.1f;
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