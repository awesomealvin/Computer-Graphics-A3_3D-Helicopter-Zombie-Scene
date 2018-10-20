package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import utils.Drawable;

public class LandingSkid implements Drawable{
	
	GL2 gl;
	GLUT glut;
	float scale = 1f;
	public LandingSkid(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
	}

	@Override
	public void draw() {
		// Skid
		gl.glPushMatrix();
		gl.glScalef(1f, 1f, 12f);
		glut.glutSolidCube(scale);
		gl.glPopMatrix();
		
		// Skid arm 1
		gl.glPushMatrix();
		gl.glTranslatef(0f, 1.5f, 1.7f);
		gl.glScalef(1f, 3f, 1f);
		glut.glutSolidCube(scale);
		gl.glPopMatrix();
		
		// Skid arm 2
		gl.glPushMatrix();
		gl.glTranslatef(0f, 1.5f, -1.7f);
		gl.glScalef(1f, 3f, 1f);
		glut.glutSolidCube(scale);
		gl.glPopMatrix();
	}
}
