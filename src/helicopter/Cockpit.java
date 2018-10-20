package helicopter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import utils.Drawable;

public class Cockpit implements Drawable{
	
	GLUquadric quad;
	
	GLU glu;
	GL2 gl;
	
	float radius = 1f;
	int stacks = 30;
	int slices = 25;
	
	public Cockpit(GLU glu, GL2 gl) {
		this.glu = glu;
		this.gl = gl;
		quad = glu.gluNewQuadric();
	}

	@Override
	public void draw() {
		gl.glPushMatrix();
		gl.glScalef(1f, 0.8f, 1.4f);
		glu.gluSphere(quad, radius, slices, stacks);
		gl.glPopMatrix();
	}

}
