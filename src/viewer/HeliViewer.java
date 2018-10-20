package viewer;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import helicopter.Helicopter;
import shapes.*;
import utils.BuildingManager;
import utils.Camera;
import utils.Color;
import utils.Vector;
import utils.ZombieManager;

public class HeliViewer implements GLEventListener, KeyListener {

	private static int WIN_HEIGHT = 800;
	private static int WIN_WIDTH = 1200;
	public Camera camera;
	private GLUT glut;

	// lighting
	float globalAmbient[] = { 0.1f, 0.1f, 0.1f, 0.2f }; // global light properties
	public float[] lightPosition = { 0.0f, 15.0f, 0.0f, 1.0f };
	public float[] ambientLight = { 0.1f, 0.1f, 0.1f, 0.2f };
	public float[] diffuseLight = { 0.1f, 0.1f, 0.1f, 0.2f };

//	Color fogColor = new Color(72, 99, 142);
	Color fogColor = new Color(70, 70, 70);

	private GLU glu;
	
	Ground ground;
	Helicopter heli;
	
	public static BuildingManager buildings;

	private double previousTime;
	
//	Zombie zombie;
	public static ZombieManager zombies;

	@Override
	public void display(GLAutoDrawable gld) {
		GL2 gl = gld.getGL().getGL2();

		// clear the depth and color buffers
		gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, fogColor.a);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();


		lights(gl);
		
		double currentTime = System.currentTimeMillis() / 1000.0;
		float deltaTime = (float) (currentTime - previousTime);

		if (previousTime > 0.0) {
			// Update Stuff Here
			camera.update(deltaTime);
			camera.draw(gl);
			
			ground.draw();

//			gl.glColor3f(1f, 1f, 0f);
//			gl.glPushMatrix();
//			{	
//				gl.glTranslatef(15f, 0f, 0f);
//				glut.glutSolidCube(10f);
//			}
//			gl.glPopMatrix();
			
			
			heli.update(deltaTime);
			heli.draw();
			
			buildings.draw();
			
			zombies.update(deltaTime);
			zombies.draw();
		
		}
		previousTime = currentTime;

		gl.glFlush();

	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable gld) {
		GL2 gl = gld.getGL().getGL2();
		gl.setSwapInterval(1);

		// enable depth test and set shading mode
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glEnable(GL2.GL_LINE_SMOOTH);

		glut = new GLUT();
		glu = new GLU();

		initializeHelicopter(gl);
		camera = new Camera(heli);
		camera.draw(gl);
		
		gl.glEnable(GL2.GL_FOG);
		gl.glFogfv(GL2.GL_FOG_COLOR, fogColor.getArray(), 0);
		gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_EXP);
//		gl.glFogf(GL2.GL_FOG_START, 10.0f);
//		gl.glFogf(GL2.GL_FOG_END, 50.0f);
		gl.glFogf(GL2.GL_FOG_DENSITY, 0.05f);

		
		
		
		initializeGround(gl);
		initializeBuildings(gl);
		
		zombies = new ZombieManager(gl, glut);

	}
	
	private void initializeGround(GL2 gl) {
		ground = new Ground(gl);
	}
	
	private void initializeBuildings(GL2 gl) {
		buildings = new BuildingManager(gl);
	}
	
	private void initializeHelicopter(GL2 gl) {
		heli = new Helicopter(gl, glut, glu);
	}

	public void lights(GL2 gl) {
		// set the global ambient light level
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmbient, 0);
		// set light 0 properties
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
		// normalise the normal surface vectors
		gl.glEnable(GL2.GL_NORMALIZE);
		// position light 0 at the origin
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
		// enable light 0
		gl.glEnable(GL2.GL_LIGHT0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		camera.newWindowSize(width, height);
	}

	public static void main(String[] args) {
		Frame frame = new Frame("Heli Viewer");
		GLCanvas canvas = new GLCanvas();
		HeliViewer app = new HeliViewer();
		canvas.addGLEventListener(app);
		canvas.addKeyListener(app);
		frame.add(canvas);
		frame.setSize(WIN_WIDTH, WIN_HEIGHT);
		final FPSAnimator animator = new FPSAnimator(canvas, 60);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {

					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		// Center frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		canvas.setFocusable(true);
		canvas.requestFocus();
		animator.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_W:
			heli.moveY(1);
			break;
		case KeyEvent.VK_A:
			heli.moveX(-1);
			break;
		case KeyEvent.VK_S:
			heli.moveY(-1);
			break;
		case KeyEvent.VK_D:
			heli.moveX(1);
			break;
		case KeyEvent.VK_LEFT:
			heli.rotateYaw(1);
			break;
		case KeyEvent.VK_RIGHT:
			heli.rotateYaw(-1);
			break;
		case KeyEvent.VK_UP:
			heli.moveZ(1);
			heli.rotatePitch(1);
			break;
		case KeyEvent.VK_DOWN:
			heli.moveZ(-1);
			heli.rotatePitch(-1);

			break;
		default:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_W:
			heli.moveY(0);
			break;
		case KeyEvent.VK_A:
			heli.moveX(0);
			break;
		case KeyEvent.VK_S:
			heli.moveY(0);
			break;
		case KeyEvent.VK_D:
			heli.moveX(0);
			break;
		case KeyEvent.VK_LEFT:
			heli.rotateYaw(0);
			break;
		case KeyEvent.VK_RIGHT:
			heli.rotateYaw(0);
			break;
		case KeyEvent.VK_UP:
			heli.moveZ(0);
			heli.rotatePitch(0);
			break;
		case KeyEvent.VK_DOWN:
			heli.moveZ(0);
			heli.rotatePitch(0);
			break;
		case KeyEvent.VK_SPACE:
			heli.shoot();
			break;
		default:
			break;

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
