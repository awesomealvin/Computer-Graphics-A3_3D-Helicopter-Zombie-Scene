package shapes;

import com.jogamp.opengl.GL2;

import utils.Color;
import utils.Drawable;
import utils.Vector;
import utils.VectorT;

public class Quad implements Drawable {

	private GL2 gl;
	
	public Vector position;
	public float size;
	Color color;
	Vector[] points;
	VectorT[] texturePoints;
	
	
	int displayListIndex;
	
	public Quad(GL2 gl, float size, Vector position, VectorT[] texturePoints, Color color) {
		this.gl = gl;
		this.size = size;
		this.color = color;
		this.points = new Vector[4];
		this.position = position;
		this.texturePoints = texturePoints;
//		System.out.println(position);
		
		
		
		points[0] = new Vector((size/2) + position.x, position.y, (size/2) + position.z); // Top Right
		points[1] = new Vector((-size/2) + position.x, position.y, (size/2) + position.z); // Top Left
		points[2] = new Vector((-size/2) + position.x, position.y, (-size/2) + position.z); // Bottom Left
		points[3] = new Vector((size/2) + position.x, position.y, (-size/2) + position.z); // Bottom Right
				
		buildDisplayList();
	}
	
	@Override
	public void draw() {
		gl.glCallList(displayListIndex);
		
	

//		gl.glBegin(GL2.GL_QUADS);
//		gl.glVertex3f((-size/2) + position.x, (0f)+position.y, (size/2) + position.z);
//		gl.glVertex3f((size/2) + position.x, (0f)+position.y, (size/2) + position.z);
//		gl.glVertex3f((size/2) + position.x, (0f)+position.y, (-size/2) + position.z);
//		gl.glVertex3f((-size/2) + position.x, (0f)+position.y, (-size/2) + position.z);
//		gl.glEnd();
	}
	
	public void buildDisplayList() {
		displayListIndex = gl.glGenLists(1);
		
		
		gl.glNewList(displayListIndex, GL2.GL_COMPILE);
		

		gl.glColor3f(color.r, color.g, color.b);
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(color.r, color.g, color.b);
		
		gl.glTexCoord2d(texturePoints[0].x, texturePoints[0].y);
		gl.glVertex3f(points[0].x, points[0].y, points[0].z);
		
		gl.glTexCoord2d(texturePoints[1].x, texturePoints[1].y);
		gl.glVertex3f(points[1].x, points[1].y, points[1].z);

		
		gl.glTexCoord2d(texturePoints[2].x, texturePoints[2].y);
		gl.glVertex3f(points[2].x, points[2].y, points[2].z);

		
		gl.glTexCoord2d(texturePoints[3].x, texturePoints[3].y);
		gl.glVertex3f(points[3].x, points[3].y, points[3].z);

		
		gl.glEnd();
		gl.glEndList();
		//System.out.println(displayListIndex);
	}

}
