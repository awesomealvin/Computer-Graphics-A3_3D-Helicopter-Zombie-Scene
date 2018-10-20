package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.Drawable;

public class Tail implements Drawable {

	GL2 gl;
	GLUT glut;
	
	float rotorAngle = 0f;

	public Tail(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}
	
	public void update(float deltaTime) {
		rotorAngle-=(Rotorblade.bladeSpeed *deltaTime);
		if (rotorAngle < 0f) {
			rotorAngle = 360f;
		}
	}

	@Override
	public void draw() {
		// Main
		gl.glPushMatrix();
		{
			gl.glScalef(0.3f, 0.3f, 3.5f);
			glut.glutSolidCube(1f);
		}
		gl.glPopMatrix();

		// Tip
		gl.glPushMatrix();
		{
			gl.glTranslatef(0f, 0.1f, -1.8f);
			
			// Rotor
			gl.glPushMatrix();
			{
				
				gl.glRotatef(rotorAngle, 1f, 0f, 0f);

				gl.glScalef(0.05f, 0.05f, 1f);
				gl.glTranslatef(3.5f, 0f, 0f);
				
				glut.glutSolidCube(1f);
			}
			gl.glPopMatrix();
			
			gl.glRotatef(-45f, 1f, 0f, 0f);
			gl.glScalef(0.3f, 0.4f, 0.3f);

			glut.glutSolidCube(1f);
		}
		gl.glPopMatrix();

	}

}
