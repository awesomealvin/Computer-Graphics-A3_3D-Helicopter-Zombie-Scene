package utils;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import helicopter.Helicopter;

public class Camera {

	private static final float FOV = 60f;

	float windowWidth = 1;
	float windowHeight = 1;

	private Vector up = new Vector(0f, 1f, 0f);
	private Vector velocity;
	private Vector position;
	private Vector forward;
	private Vector right;

	private Vector lookAt;
	private float yaw = 0f;
	private float yawVelocity;
	private final float YAW_SPEED = 40f;
	private final float MAX_YAW = 360f;
	private final float MIN_YAW = 0f;
	private float pitch = -15f;
	private float pitchVelocity;
	private final float PITCH_SPEED = 40f;
	private final float MAX_PITCH = 89f;
	private final float MIN_PITCH = -89f;

	float distance = 200f;

	private final float MOVEMENT_SPEED = 20f;

	// the point to look at

	Helicopter heli;

	Vector offset = new Vector(0f, 3.5f, -8f);

	public Camera(Helicopter heli) {
		this.heli = heli;
		velocity = new Vector();
		position = new Vector(0f, 0f, 0f);
		lookAt = new Vector(0f, 0f, 0f);

		// lookAt = new Vector(-168f, -33f, 86f);
		forward = Vector.subtract(lookAt, position).normalized();
	}

	public void draw(GL2 gl) {
		// set up projection first
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(FOV, windowWidth / windowHeight, 0.1, distance);
		// set up the camera position and orientation
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(position.x, position.y, position.z, // eye
				lookAt.x, lookAt.y, lookAt.z, // looking at
				up.x, up.y, up.z); // up
		//
		// System.out.println(pitch);
		// System.out.println(yaw);
	}

	/**
	 * Sets up the lookAt point - could be a specified object's location
	 * 
	 * @param x
	 *            X coordinate of the lookAt point
	 * @param y
	 *            Y coordinate of the lookAt point
	 * @param z
	 *            Z coordinate of the lookAt point
	 */
	public void setLookAt(float x, float y, float z) {
		lookAt.x = x;
		lookAt.y = y;
		lookAt.z = z;
	}

	public void setEye(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	/**
	 * Passes a new window size to the camera. This method should be called from the
	 * <code>reshape()</code> method of the main program.
	 *
	 * @param width
	 *            the new window width in pixels
	 * @param height
	 *            the new window height in pixels
	 */
	public void newWindowSize(int width, int height) {
		windowWidth = (float) Math.max(1.0, width);
		windowHeight = (float) Math.max(1.0, height);
	}

	public void update(float deltaTime) {
		updateMovement(deltaTime);
		updateRotation(deltaTime);
	}

	public void updateMovement(float deltaTime) {

//		forward = Vector.subtract(heli.lookAt, offset).normalized();

		// right = Vector.crossProduct(forward, up);
		//
		// forward.multiply(heli.velocity.z);
		// right.multiply(heli.velocity.x);
		//
		//
		//
		// this.position.add(forward, deltaTime);
		// this.position.add(right, deltaTime);
		// this.position.add(up.multiplied(heli.velocity.y), deltaTime);

		this.position = heli.position.copy();

		double radians = Math.toRadians((double) heli.yaw);
		double hyp = (double) offset.z;
//
		float z = (float) (Math.cos(radians) * hyp);
		float x = (float) (Math.sin(radians) * hyp);

//		float z = (float)((position.x * Math.cos(radians) * hyp) - (position.z * Math.sin(radians) * hyp));
//		float x = (float)((position.x* Math.sin(radians) * hyp) + (position.z * Math.cos(radians) * hyp));
//		
//		x += heli.position.x;
//		z += position.z;


		Vector newPos = new Vector(x, offset.y, z);
//		System.out.println(newPos);
		this.position.add(newPos);
		
//		System.out.println("Heli: "+ heli.position);
//		System.out.println("Camera: "+ newPos);

	}
//
//	public void moveX(int movement) {
//		velocity.x = movement * MOVEMENT_SPEED;
//	}
//
//	public void moveY(int movement) {
//		velocity.y = movement * MOVEMENT_SPEED;
//	}
//
//	public void moveZ(int movement) {
//		velocity.z = movement * MOVEMENT_SPEED;
//	}

	public void rotateYaw(int rotation) {
		yawVelocity = rotation * YAW_SPEED;
	}

	public void rotatePitch(int rotation) {
		pitchVelocity = rotation * PITCH_SPEED;
	}

	public void updateRotation(float deltaTime) {
		yaw += yawVelocity * deltaTime;
		yaw = (yaw > MAX_YAW) ? MIN_YAW : yaw;
		yaw = (yaw < MIN_YAW) ? MAX_YAW : yaw;
		yaw = heli.yaw;
		// System.out.println("Yaw: " + yaw);

		pitch += pitchVelocity * deltaTime;
		pitch = (pitch > MAX_PITCH) ? MAX_PITCH : pitch;
		pitch = (pitch < MIN_PITCH) ? MIN_PITCH : pitch;
		// System.out.println("Pitch: " + pitch);

		float radians = (float) Math.toRadians(pitch);
		float dxz = (float) Math.cos(radians) * distance;
		float lookAtY = (float) (Math.sin(radians) * distance) + position.y;

		radians = (float) Math.toRadians(yaw);
		float lookAtX = (float) (Math.sin(radians) * dxz) + position.x;
		float lookAtZ = (float) (Math.cos(radians) * dxz) + position.z;

		lookAt.x = lookAtX;
		lookAt.y = lookAtY;
		lookAt.z = lookAtZ;

		// lookAt.multiply(vector);
		//
		// lookAt.x = lookAtX + heli.lookAt.x;
		// lookAt.y = lookAtY + heli.lookAt.y;
		// lookAt.z = lookAtZ + heli.lookAt.z;

		// lookAt.x = heli.lookAt.x;
		// lookAt.y = heli.lookAt.y;
		// lookAt.z = heli.lookAt.z;
	}

}
