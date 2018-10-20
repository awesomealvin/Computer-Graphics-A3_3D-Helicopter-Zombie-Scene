package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.Drawable;

public class Rotorblade implements Drawable {

	GL2 gl;
	GLUT glut;
	GLU glu;

	GLUquadric quad;

	float rotorAngle = 0f;
	
	public static float bladeSpeed = 600f;

	public Rotorblade(GL2 gl, GLUT glut, GLU glu) {
		this.gl = gl;
		this.glut = glut;
		this.glu = glu;
		quad = glu.gluNewQuadric();
	}

	public void update(float deltaTime) {
		rotorAngle -= (bladeSpeed * deltaTime);
		if (rotorAngle < 0f) {
			rotorAngle = 360f;
		}
	}

	@Override
	public void draw() {

		// Holder
		gl.glPushMatrix();
		{
			gl.glRotatef(90f, 1f, 0f, 0f);
			gl.glScalef(0.09f, 0.09f, 0.5f);
			glu.gluCylinder(quad, 1f, 1f, 1f, 15, 5);
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, 0f, 0.09f);
				glu.gluDisk(quad, 0f, 1f, 15, 5);
			}
			gl.glPopMatrix();

			glu.gluDeleteQuadric(quad);

			// glut.glutSolidCube(1f);
		}
		gl.glPopMatrix();

		// Blades
		gl.glPushMatrix();
		{
			gl.glRotatef(rotorAngle, 0f,1f, 0f);
			gl.glTranslatef(0f, -0.1f, 0f);
			gl.glScalef(0.3f, 0.05f, 4f);
			glut.glutSolidCube(1f);
		}
		gl.glPopMatrix();
	}

}
