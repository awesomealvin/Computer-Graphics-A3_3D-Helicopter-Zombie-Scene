package shapes;

import com.jogamp.opengl.GL2;

import utils.Color;
import utils.Drawable;
import utils.Vector;

public class Building implements Drawable {

	GL2 gl;

	Color color = new Color(165, 96, 36);

	private float width = 10f;
	private float height = 5f;
	private float length = 5f;
	public Vector position;
	
	Box walls;
	
	Roof roof;
	
	public BoundingBox bounds;
	

	public Building(GL2 gl, Vector position, float width, float height, float length) {
		this.gl = gl;
		this.position = position;
		this.width = width;
		this.height = height;
		this.length = length;
		walls = new Box(gl, width, height, length, color, "./textures/bricks01_col_low.jpg");
		roof = new Roof(gl, width, height, length);
				
		initializeBoundingBox();
	}

	private void initializeBoundingBox() {
		bounds = new BoundingBox(width, height, length, position);
	}

	@Override
	public void draw() {
		gl.glPushMatrix();
		gl.glTranslatef(position.x, position.y, position.z);
		walls.draw();
		roof.draw();
		
		gl.glPopMatrix();
	}
}
