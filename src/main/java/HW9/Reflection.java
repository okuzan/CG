package HW9;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static java.awt.event.KeyEvent.*;

public class Reflection implements GLEventListener, KeyListener {

    private static final int CANVAS_WIDTH = 500;
    private static final int CANVAS_HEIGHT = 500;
    private static final int FPS = 60;
    private float rotateAngleX = 0.0f;
    private float rotateAngleY = 0.0f;
    private float rotateSpeedX = 0.0f;
    private float rotateSpeedY = 0.0f;
    private float height = 2.0f;
    private float z = -7.0f, x = 0;
    private GLUT glut = new GLUT();
    private GLU glu = new GLU();

    private Texture[] textures = new Texture[3];
    private static final String[] TEXTURE_FILE_NAMES = {
            "src/main/resources/images/glass.jpg",
            "src/main/resources/images/pattern3.jpg",
            "src/main/resources/images/envroll.jpg",
    };

    private float[] textureTops = new float[3];
    private float[] textureBottoms = new float[3];
    private float[] textureLefts = new float[3];
    private float[] textureRights = new float[3];

    private float[] lightAmbientValue = {0.7f, 0.7f, 0.7f, 1.0f};
    private float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightDiffusePosition = {4.0f, 4.0f, 6.0f, 1.0f};
    private double R = 0.4f;

    public static void main(String[] args) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setStencilBits(8);
        final GLCanvas canvas = new GLCanvas(capabilities);
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        Reflection renderer = new Reflection();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer);
        canvas.setFocusable(true);
        canvas.requestFocus();
        final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
        final JFrame frame = new JFrame();
        frame.getContentPane().add(canvas);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        animator.stop(); // stop the animator loop
                        System.exit(0);
                    }
                }.start();
            }
        });
        frame.setTitle("Reflections");
        frame.pack();
        frame.setVisible(true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);

        gl.glClearStencil(0);

        try {
            for (int i = 0; i < textures.length; i++) {
                textures[i] = TextureIO.newTexture(new FileInputStream(TEXTURE_FILE_NAMES[i]), false, ".png");
                gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                // Use linear filter for texture if image is smaller than the original texture
                gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

                TextureCoords textureCoords = textures[i].getImageTexCoords();
                textureTops[i] = textureCoords.top();
                textureBottoms[i] = textureCoords.bottom();
                textureLefts[i] = textureCoords.left();
                textureRights[i] = textureCoords.right();
            }
        } catch (GLException | IOException e) {
            e.printStackTrace();
        }

        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, lightAmbientValue, 0);
        gl.glLightfv(GL_LIGHT0, GL_DIFFUSE, lightDiffuseValue, 0);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0);
        gl.glEnable(GL_LIGHT0);
        gl.glEnable(GL_LIGHTING);

        // Set up the Quadric for drawing sphere
//        quadric = glu.gluNewQuadric();             // Create a new quadric
//        glu.gluQuadricNormals(quadric, GL_SMOOTH); // Generate smooth normals
//        glu.gluQuadricTexture(quadric, true);      // Enable texture
        // set up sphere mapping for both the s- and t-axes
        gl.glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);
        gl.glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_SPHERE_MAP);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float) width / height;

//        gl.glViewport(0, 0, width, height);
        glu.gluPerspective(120.0, aspect, 0.1, 100.0);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        // Clip plane equations for clipping the reflected image
        double[] eqr = {0.0f, -1.0f, 0.0f, 0.0f};

        // Translate downward to see the floor, and z
        gl.glTranslatef(0, -0.6f, z);

        // Set the color mask RGBA to false, i.e., no color gets thru
        gl.glColorMask(false, false, false, false);

        // Setting up the stencil buffer and stencil testing
        gl.glEnable(GL_STENCIL_TEST);      // Enable stencil buffer for "marking" the floor
        gl.glStencilFunc(GL_ALWAYS, 1, 1); // Always passes, 1 Bit Plane, 1 As Mask
        gl.glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE); // Set the stencil buffer to 1 where we draw any polygon
        // Keep if test fails, keep if test passes but buffer test fails
        // replace if test passes
        gl.glDisable(GL_DEPTH_TEST); // Disable depth testing

        drawFloor(gl);

        gl.glColorMask(true, true, true, true); // Set color mask to let RGBA thru
        gl.glEnable(GL_DEPTH_TEST);  // Enable depth testing
        gl.glStencilFunc(GL_EQUAL, 1, 1);       // We draw only where the stencil is 1
        // (i.e. where the floor was drawn)
        gl.glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // Don't change the stencil buffer

        gl.glEnable(GL_CLIP_PLANE0); // Enable clip plane for removing artifacts
        // (when the object crosses the floor)
        gl.glClipPlane(GL_CLIP_PLANE0, eqr, 0); // Equation for reflected objects
        gl.glPushMatrix();                      // Push the matrix onto the stack
        gl.glScalef(1.0f, -1.0f, 1.0f);         // Mirror y-axis

//        gl.glEnable(GL_CULL_FACE);
//        gl.glCullFace(GL_BACK);
        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0); // Set up LIGHT0
        gl.glTranslatef(x, height, 0.0f); // Position the ball
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f);
        drawBall(gl);                  // Draw the sphere (reflection)
        gl.glPopMatrix();              // Pop the matrix off the stack
        gl.glDisable(GL_CLIP_PLANE0);  // Disable clip plane for drawing the floor
        gl.glDisable(GL_STENCIL_TEST); // We don't need the stencil buffer any more

        gl.glLightfv(GL_LIGHT0, GL_POSITION, lightDiffusePosition, 0); // Set up LIGHT0 position
        gl.glEnable(GL_BLEND);     // Enable blending (otherwise the reflected object won't show)
        gl.glDisable(GL_LIGHTING); // Since we use blending, we disable Lighting
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.8f); // Set color to white with 80% alpha
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // Blending based on source alpha and 1 minus dest alpha
        drawFloor(gl); // Draw the floor

        gl.glEnable(GL_LIGHTING);  // Enable lighting
        gl.glDisable(GL_BLEND);    // Disable blending
        gl.glTranslatef(x, height, 0.0f); // Position the ball at proper height
        gl.glRotatef(rotateAngleX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotateAngleY, 0.0f, 1.0f, 0.0f);
        drawBall(gl); // Draw the ball

        // Update rotational angle
        rotateAngleX += rotateSpeedX;
        rotateAngleY += rotateSpeedY;
        gl.glFlush();
    }

    // Render the beach ball by drawing two fully overlapped spheres
    private void drawBall(GL2 gl) {

        // draw the first sphere with texture "ball"
        gl.glColor3f(1.0f, 1.0f, 1.0f); // Set color to white
        textures[1].enable(gl);
        textures[1].bind(gl);
//        glu.gluSphere(quadric, R, 32, 16); // Draw first sphere
        glut.glutSolidTeapot(R, true); // Draw first sphere
        textures[1].disable(gl);

        textures[2].enable(gl);
        textures[2].bind(gl);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);  // Set color to white with 40% alpha
        gl.glEnable(GL_BLEND);                 // Enable blending
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);  // Set blending mode to mix based on source alpha
        gl.glEnable(GL_TEXTURE_GEN_S);   // Enable sphere mapping on s-axis
        gl.glEnable(GL_TEXTURE_GEN_T);   // Enable sphere mapping on t-axis
//        glu.gluSphere(quadric, R, 32, 16); // Draw another sphere using new texture
        glut.glutSolidTeapot(R, true); // Draw first sphere
        // Textures will mix creating a multi-texture effect (Reflection)
        gl.glDisable(GL_TEXTURE_GEN_S);  // Disable sphere mapping
        gl.glDisable(GL_TEXTURE_GEN_T);  // Disable sphere mapping
        gl.glDisable(GL_BLEND);
        textures[2].disable(gl);
    }

    private void drawFloor(GL2 gl) {
        textures[0].enable(gl);  // "evnwall"
        textures[0].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);   // Normal pointing up
        gl.glTexCoord2f(textureLefts[0], textureBottoms[0]);  // Bottom-left of texture
        gl.glVertex3f(-5.0f, 0.0f, 5.0f);  // Bottom-Left corner of the floor
        gl.glTexCoord2f(textureLefts[0], textureTops[0]);     // Top-left of texture
        gl.glVertex3f(-5.0f, 0.0f, -5.0f); // Top-left corner of the floor
        gl.glTexCoord2f(textureRights[0], textureTops[0]);    // Top-right of texture
        gl.glVertex3f(5.0f, 0.0f, -5.0f);  // Top-right corner of the floor
        gl.glTexCoord2f(textureRights[0], textureBottoms[0]); // Bottom-right of texture
        gl.glVertex3f(5.0f, 0.0f, 5.0f);  // Bottom-right corner of the floor
        gl.glEnd();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case VK_RIGHT:  // increase rotational speed y
                rotateSpeedY += 0.1f;
                break;
            case VK_LEFT:   // decrease rotational speed y
                rotateSpeedY -= 0.1f;
                break;
            case VK_DOWN:   // increase rotational speed x
                rotateSpeedX += 0.1f;
                break;
            case VK_UP:     // decrease rotational speed x
                rotateSpeedX -= 0.1f;
                break;
            case VK_A:      // zoom in
                z += 0.03f;
                break;
            case VK_X:      // move by x
                if (e.isShiftDown()) x += .03f;
                else x -= .03f;
                break;
            case VK_Z:      // zoom out
                z -= 0.03f;
                break;
            case VK_PAGE_UP:    // move ball up
                height += 0.03f;
                break;
            case VK_PAGE_DOWN:  // move ball down
                if (height >= R + 0.03) height -= 0.03f;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}