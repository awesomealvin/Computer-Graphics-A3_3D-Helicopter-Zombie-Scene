package shapes;

import com.jogamp.opengl.GL2;

import utils.Drawable;
import utils.Vector;

public class BoundingBox implements Drawable{

	public Vector min;
	public Vector max;
	
	GL2 gl;
	
	public BoundingBox(float width, float height, float length, Vector position) {
		
		min = new Vector(-width/2f, 0f, -length/2f);
		max = new Vector(width/2f, height, length/2f);
		
		min.add(position);
		max.add(position);
	}

	public boolean contactBox(BoundingBox a, BoundingBox b) {
		if ((a.min.x <= b.max.x && a.max.x >= b.min.x) &&
				(a.min.y <= b.max.y && a.max.y >= b.min.y)&&
				(a.min.z <= b.max.z && a.max.z >= b.min.z)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean contactPoint(Vector point) {
		return ((point.x >= min.x && point.x <= max.x) &&
				(point.y >= min.y && point.y <= max.y) &&
				(point.z >= min.z && point.z <= max.z)); 
			
		
	}

	@Override
	public void draw() {
		/*
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1f, 0f, 0f, 0.3f);
		gl.glBegin(GL2.GL_QUADS);
		
		// FACE
		gl.glVertex3f(min.x, min.y, min.z);
		gl.glVertex3f(min.x, max.y, min.z);
		gl.glVertex3f(max.x, max.y, min.z);
		gl.glVertex3f(max.x, min.y, min.z);
		
		// LEFT
		
		gl.glVertex3f(min.x, min.y, max.z);
		gl.glVertex3f(min.x, min.y, max.z);
		gl.glVertex3f(min.x, min.y, max.z);
		gl.glVertex3f(min.x, min.y, max.z);
		
		
		gl.glEnd();
		
		
		gl.glDisable(GL2.GL_BLEND);
		*/

	}
	
	
}
