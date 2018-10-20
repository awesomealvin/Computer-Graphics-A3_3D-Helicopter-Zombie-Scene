package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import shapes.BoundingSphere;
import shapes.Building;
import utils.Color;
import utils.Drawable;
import utils.Vector;
import viewer.HeliViewer;

public class Helicopter implements Drawable {

	private GL2 gl;
	private GLUT glut;
	private GLU glu;

	public Vector position;

	private Vector up = new Vector(0f, 1f, 0f);
	public Vector velocity;
	public Vector forward;
	public Vector right;
	private final float MOVEMENT_SPEED = 2.5f;

	public Vector lookAt;
	public float yaw = 0f;
	private float yawVelocity;
	private final float YAW_SPEED = 20f;
	private final float MAX_YAW = 360f;
	private final float MIN_YAW = 0f;
	public float pitch = 0f;
	public final float PITCH_SPEED_MULTIPLIER = 0.25f;
	private float pitchVelocity;
	private final float PITCH_SPEED = 30f;
	private final float MAX_PITCH = 30f;
	private final float MIN_PITCH = -30f;

	Cockpit cockpit;
	LandingSkid landingSkid1;
	LandingSkid landingSkid2;
	Tail tail;
	Rotorblade rotorblade;
	
	public BoundingSphere bounds;
	Vector nextPosition;

	float[] lightPosition = {0f, 0f, 0f, 1f};
	float[] lightDirection = {0f, 0f, 1f, 1f};
	public float[] ambientLight = { 0.2f, 0.2f, 0.2f, 1f };
	public float[] diffuseLight = {1f, 1f, 1f, 1f };
	public float[] specularLight = { 0.8f, 0.8f, 0.8f, 1f, };
	
	public Color color = new Color(56, 56,56);
	
	public Rocket[] rockets;

	public Helicopter(GL2 gl, GLUT glut, GLU glu) {
		this.glut = glut;
		this.glu = glu;
		this.gl = gl;
		cockpit = new Cockpit(glu, gl);
		landingSkid1 = new LandingSkid(gl, glut);
		landingSkid2 = new LandingSkid(gl, glut);
		tail = new Tail(gl, glut);
		rotorblade = new Rotorblade(gl, glut, glu);

		position = new Vector(0f, 7f, 0f);
		nextPosition = position.copy();
		
		bounds = new BoundingSphere(nextPosition, 1f);

		lookAt = new Vector();
		velocity = new Vector();
		
		
		initializeRockets();
		
		printHelicopterControls();
		
	}
	
	private void printHelicopterControls() {
		System.out.println("--HELICOPTER CONTROLS--");
		System.out.println("W: Elevate Up");
		System.out.println("S: Elevate Down");
		System.out.println("A: Strafe Left");
		System.out.println("D: Strafe Right");
		System.out.println();
		System.out.println("Up Arrow: Pitch Down / Move Forward");
		System.out.println("Down Arrow: Pitch Up / Move Backwards");
		System.out.println("-NOTE: Pitching controls increase/decreases the pitch amount-");
		System.out.println("Left Arrow: Yaw / Rotate Left");
		System.out.println("Right Arrow: Yaw / Rotate Right");
		System.out.println();
		System.out.println("Spacebar: SHOOT DEM ROCKETS!");
		System.out.println();
	}
	
	private void initializeRockets() {
		rockets = new Rocket[5];
		for (int i = 0; i < rockets.length; i++) {
			rockets[i] = new Rocket(gl, glu);
		}
	}
	
	
	public void shoot() {
		for (Rocket r : rockets) {
			if (!r.enabled) {
				// SHOOT
				double radiansPitch = Math.toRadians(-pitch);
				double radiansYaw = Math.toRadians(yaw);
				
				double dxz = Math.cos(radiansPitch);
				float y = (float)(Math.sin(radiansPitch));
				
				float x = (float)(Math.sin(radiansYaw)*dxz);	
				float z = (float)(Math.cos(radiansYaw)*dxz);
				
				Vector velocity = new Vector(x, y, z);
//				System.out.println(velocity);
				
				r.shoot(position.copy(), velocity.normalized(),yaw , pitch);
				
				return;
			}
		}
	}

	private void lights() {
		// set the global ambient light level
		// gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmbient, 0);
		// set light 0 properties
//		 gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambientLight, 0);
		gl.glEnable(GL2.GL_LIGHT1);

		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuseLight, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specularLight, 0);

	
		// position light 0 at the origin
//		float[] lightPosition = {position.x, position.y, position.z, 1f};
		

		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition, 0);
		
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, lightDirection, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 20f);
		gl.glLightf(GL2.GL_LIGHT1,GL2.GL_SPOT_EXPONENT , 0.001f);

		
		// normalise the normal surface vectors
		gl.glEnable(GL2.GL_NORMALIZE);
		
		// enable light 1
	}

	public void update(float deltaTime) {
		tail.update(deltaTime);
		rotorblade.update(deltaTime);

		updateMovement(deltaTime);
		updateRotation(deltaTime);	
		updateRockets(deltaTime);
		checkRocketCollision();
		
	}

	private void updateRockets(float deltaTime) {
		for (Rocket r : rockets) {
			r.update(deltaTime);
		}
	}
	
	private void drawRockets() {
		for (Rocket r : rockets) {
			r.draw();
		}
	}

	public void updateMovement(float deltaTime) {
		forward = Vector.subtract(lookAt, position).normalized();
		right = Vector.crossProduct(forward, up);
		
//		forward.multiply(velocity.z);
		forward.multiply(pitch * PITCH_SPEED_MULTIPLIER);
		right.multiply(velocity.x);

		
//		nextPosition.add(forward, deltaTime);
//		nextPosition.add(right, deltaTime);
//		nextPosition.add(up.multiplied(velocity.y), deltaTime);
		
		this.position.add(forward, deltaTime);
		this.position.add(right, deltaTime);
		this.position.add(up.multiplied(velocity.y), deltaTime);
		
//		if (bounds.contactBox(HeliViewer.building.bounds)) {
//			// CONTACT
//		}


	}
	
	private void checkRocketCollision() {
		for (Building b : HeliViewer.buildings.buildings) {
			for (Rocket r : rockets) {
				
				if (r.bounds.contactBox(b.bounds)) {
					if (r.enabled) {
						r.explode();

					}
				}
				
				// Ground Collision
				if (r.position.y <= 0f) {
					if (r.enabled) {
						r.explode();

					}
				}
				// Outside Collision
				if (((r.position.x < -100f || r.position.x > 100f) ||
				(r.position.z < -100f || r.position.z > 100f))){
					if (r.enabled) {
						r.explode();
					}
				}
				
			}
		}
	}

	public void moveX(int movement) {
		velocity.x = movement * MOVEMENT_SPEED;
	}

	public void moveY(int movement) {
		velocity.y = movement * MOVEMENT_SPEED;
	}

	public void moveZ(int movement) {
		velocity.z = movement * MOVEMENT_SPEED;
	}

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
//		 System.out.println("Yaw: " + yaw);

		pitch += pitchVelocity * deltaTime;
		pitch = (pitch > MAX_PITCH) ? MAX_PITCH : pitch;
		pitch = (pitch < MIN_PITCH) ? MIN_PITCH : pitch;
//		pitch = 0f;
//		 System.out.println("Pitch: " + pitch);

		yaw += yawVelocity * deltaTime;
		yaw = (yaw > MAX_YAW) ? MIN_YAW : yaw;
		yaw = (yaw < MIN_YAW) ? MAX_YAW : yaw;
//		 System.out.println("Yaw: " + yaw);



		float radians = (float) Math.toRadians(0);
		// float dxz = (float) Math.cos(radians) * distance;
		// float lookAtY = (float)(Math.sin(radians) * distance) + position.y;

		float dxz = (float) Math.cos(radians);
		float lookAtY = (float) (Math.sin(radians)) + position.y;

		radians = (float) Math.toRadians(yaw);
		float lookAtX = (float) (Math.sin(radians) * dxz) + position.x;
		float lookAtZ = (float) (Math.cos(radians) * dxz) + position.z;

		lookAt.x = lookAtX;
		lookAt.y = lookAtY;
		lookAt.z = lookAtZ;
	}

	@Override
	public void draw() {
		drawRockets();
		

		gl.glPushMatrix();
		{	
			float shininess = 100;
			float[] ambient = {1f, 1f, 1f, 1f};
			float[] diffuse = {1f, 1f, 1f, 1f};
			float[] specular = {1f, 1f, 1f, 1f};
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
//
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);

			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);

			gl.glColor3f(color.r, color.g, color.b);

			gl.glTranslatef(position.x, position.y, position.z);
			gl.glRotatef(yaw, 0f, 1f, 0f);
			gl.glRotatef(pitch, 1f, 0f, 0f);
			

			lights();

			cockpit.draw();

			// LANDING SKIDS
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, -1f, 0f);
				gl.glScalef(0.2f, 0.2f, 0.2f);

				gl.glPushMatrix();
				{
					gl.glTranslatef(2f, 0f, 0f);
					landingSkid1.draw();
				}
				gl.glPopMatrix();

				gl.glPushMatrix();
				{
					gl.glTranslatef(-2f, 0f, 0f);
					landingSkid2.draw();
				}
				gl.glPopMatrix();

			}
			gl.glPopMatrix();

			// TAIL
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, 0.3f, -1f);
				tail.draw();
			}
			gl.glPopMatrix();

			// ROTORBLADE
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, 1f, 0f);
				
				rotorblade.draw();
			}
			gl.glPopMatrix();

			gl.glPopMatrix();
		}
	}

}
