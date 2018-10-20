package shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.Color;
import utils.Drawable;
import utils.RandomRange;
import utils.Vector;
import utils.VectorT;
import utils.ZombieManager;
import viewer.HeliViewer;

public class Zombie implements Drawable {

	GL2 gl;
	GLUT glut;

	Color colorHead = new Color(40, 150, 51);
	Color colorBody = new Color(47, 86, 196);

	public Vector position;

	public Vector velocity;

	public float speed = 1f;

	public BoundingSphere bounds;

	float angle;
	
	Box head;
	String textureLocation ="./textures/overcast-stone-wall_low.jpg";

	public Zombie(GL2 gl, GLUT glut, Vector position) {
		this.gl = gl;
		this.glut = glut;
		this.position = position;
		calculateDirection(RandomRange.randomRange(0f, 360f));
		bounds = new BoundingSphere(this.position, 1f);
		initializeHead();
	}
	
	private void initializeHead() {
		VectorT[] texCoords = new VectorT[4];
		
		texCoords[0] = new VectorT(0, 0.5f);
		texCoords[1] = new VectorT(0.5f, 0f);
		texCoords[2] = new VectorT(0.5f, 1f);
		texCoords[3] = new VectorT(0f, 1f);
//		
		head = new Box(gl, 1f, 1f, 1f, colorHead, textureLocation, true, texCoords);

	}

	public void calculateDirection(float angle) {
		this.angle = angle;
		double radians = Math.toRadians(angle);

		float x = (float) Math.cos(radians);
		float z = (float) Math.sin(radians);

		velocity = new Vector(x, 0f, z);

	}

	// Zombies often getting stuck with this method...
	private float calculateRandomOppositeAngle() {
		float oppositeAngle = (angle + 180) % 360;

		return RandomRange.randomRange((oppositeAngle - 20) % 360, (oppositeAngle + 20) % 360);
	}

	private void checkCollision() {
		for (Building b : HeliViewer.buildings.buildings) {
			if (bounds.contactBox(b.bounds)) {
				calculateDirection((angle + 180) % 360);
			}
		}

		// Outside Collision
		if (((position.x < -100f || position.x > 100f) || (position.z < -100f || position.z > 100f))) {
			calculateDirection((angle + 180) % 360);

		}
	}

	public void update(float deltaTime) {
		this.position.add(velocity.multiplied(speed).multiplied(deltaTime));
		checkCollision();
	}

	@Override
	public void draw() {
		gl.glPushMatrix();
		{
			gl.glTranslatef(position.x, position.y, position.z);
			// Body
			gl.glPushMatrix();
			{
				gl.glColor3f(colorBody.r, colorBody.g, colorBody.b);
				gl.glTranslatef(0f, 0.5f, 0f);
				gl.glScalef(0.4f, 1f, 0.4f);
				glut.glutSolidCube(1f);
			}
			gl.glPopMatrix();

			// Head
			gl.glPushMatrix();
			{
				gl.glColor3f(colorHead.r, colorHead.g, colorHead.b);
				gl.glTranslatef(0f, 1f, 0f);
				gl.glScalef(0.5f, 0.5f, 0.5f);
				head.draw();
//				glut.glutSolidCube(1f);
			}
			gl.glPopMatrix();

		}
		gl.glPopMatrix();
	}

	public void die() {
		boolean validLocation = true;

		Vector pos;
		do {
			validLocation = true;
			float x = RandomRange.randomRange(ZombieManager.min, ZombieManager.max);
			float z = RandomRange.randomRange(ZombieManager.min, ZombieManager.max);

			pos = new Vector(x, 0f, z);

			for (Building b : HeliViewer.buildings.buildings) {
				if (b.bounds.contactPoint(pos)) {
					validLocation = false;
				}
			}

			// if (!validLocation) {
			// System.out.println("Inside Building... Recalculating");
			// }

		} while (!validLocation);
		this.position.x = pos.x;
		this.position.z = pos.z;

		// System.out.println("Respawned at: " +pos);
	}

}
