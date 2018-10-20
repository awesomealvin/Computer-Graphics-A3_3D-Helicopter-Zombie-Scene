package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import shapes.BoundingSphere;
import utils.Color;
import utils.Drawable;
import utils.Vector;
import viewer.HeliViewer;

public class Rocket implements Drawable {

	public float speed = 20f;

	public Vector position;

	public Vector velocity;

	public boolean enabled = false;
	
	public boolean exploded = false;

	public float radius = 0.3f;
	
	float yaw;
	float pitch;

	GLUquadric quad;

	GLU glu;
	GL2 gl;
	
	BoundingSphere bounds;
	
	Explosion explosion;
	
	Color color = new Color(88, 153, 96);

	public Rocket(GL2 gl, GLU glu) {
		this.glu = glu;
		this.gl = gl;
		
		position = new Vector(0f, 0f, 0f);
		bounds = new BoundingSphere(position, radius);
		explosion = new Explosion(gl, glu, this);
		quad = glu.gluNewQuadric();
	}

	public void update(float deltaTime) {
		if (enabled) {
			this.position.add(velocity.multiplied(deltaTime).multiplied(speed));
			bounds.position = position;
		}
		
		explosion.update(deltaTime);
	}

	public void shoot(Vector position, Vector velocity, float yaw, float pitch) {
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
		this.velocity = velocity;
		this.yaw = yaw;
		this.pitch = pitch;
		enabled = true;
	}

	@Override
	public void draw() {
		if (enabled) {
			gl.glPushMatrix();
			{
				float shininess = 10;
				float[] ambient = {1f, 1f, 1f, 1f};
				float[] diffuse = {1f, 1f, 1f, 1f};
				float[] specular = {0.1f, 0.1f, 0.1f, 1f};
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	//
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
				gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);

				gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);
				gl.glColor3f(color.r, color.g, color.b);
				
				gl.glTranslatef(position.x, position.y, position.z);
				gl.glRotatef(yaw, 0f, 1f, 0f);
				gl.glRotatef(pitch, 1f, 0f, 0f);
				gl.glScalef(0.6f, 0.6f, 2.5f);
				glu.gluSphere(quad, radius, 15, 15);
			}
			gl.glPopMatrix();
		}
		
		explosion.draw();
	}
	
	public void explode() {
		if (!exploded) {
			enabled = false;
			explosion.explode(position.copy());
			exploded = true;
		}
		
	}

}
