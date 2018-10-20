package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import shapes.BoundingSphere;
import shapes.Zombie;
import utils.Color;
import utils.Drawable;
import utils.MyMath;
import utils.Vector;
import viewer.HeliViewer;

public class Explosion implements Drawable{
	
	Color color = new Color(255, 178, 45, 0.6f);
	
	public boolean exploding = false;
	
	private float currentTime = 0f;
	private float t;
	private float duration = 0.2f;
	
	private float maxRadius = 4f;
	private float minRadius = 0.8f;
	private float currentRadius = minRadius;
	private float minScale = 0.2f;
	
	private boolean scaleUp = false;
	
	private Vector position;
	
	GL2 gl;
	GLU glu;
	
	GLUquadric quad;
	
	Rocket source;
	
	BoundingSphere bounds;
	
	public Explosion (GL2 gl, GLU glu, Rocket source) {
		this.gl = gl;
		this.glu = glu;
		quad = glu.gluNewQuadric();
		position = source.position.copy();
		this.source = source;
		bounds = new BoundingSphere(position, currentRadius);
	}
	

	@Override
	public void draw() {
		if (!exploding) {
			return;
		}
		gl.glPushMatrix();
		{
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			
			float shininess = 100;
			float[] ambient = {1f, 1f, 1f, 1f};
			float[] diffuse = {1f, 1f, 1f, 1f};
			float[] specular = {1f, 1f, 1f, 1f};
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
//
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);

			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);
			
			
			gl.glColor4f(color.r, color.g, color.b, color.a);
			
			gl.glTranslatef(position.x, position.y, position.z);
//			gl.glScalef(currentScale, currentScale, currentScale);
			glu.gluSphere(quad, currentRadius, 10, 10);
			gl.glDisable(GL2.GL_BLEND);

		}
		gl.glPopMatrix();
	}
	
	public void update(float deltaTime) {
		if (exploding) {
			if (scaleUp) {
				currentTime += deltaTime;
				t = currentTime / duration;
				if (currentTime > duration) {
					scaleUp = false;
				}
			} else {
				currentTime -= deltaTime;
				t = currentTime / duration;
				if (currentTime < 0f) {
					exploding = false;
					source.exploded = false;
				}
			}
			currentRadius = MyMath.lerp(minRadius, maxRadius, t);
			bounds.radius = currentRadius;
			
			checkForZombies();
		}
	}
	
	public void explode(Vector position) {
		this.position = position;
		bounds.position = this.position;
		
		currentTime = 0f;
		exploding = true;
		scaleUp = true;
		
//		System.out.println("EXPLODE: " + this.position);
	}
	
	public void checkForZombies() {
		for (Zombie z : HeliViewer.zombies.zombies) {
			if (this.bounds.contactSphere(z.bounds)) {
				z.die();
			}
		}
	}

}
