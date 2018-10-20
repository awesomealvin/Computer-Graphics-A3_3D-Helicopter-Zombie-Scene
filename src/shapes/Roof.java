package shapes;

import com.jogamp.opengl.GL2;

import utils.Color;
import utils.Drawable;
import utils.Vector;

public class Roof implements Drawable{

	Color color = new Color(145, 144, 143);

	GL2 gl;
	
	Box front;
	Box left;
	Box back;
	Box right;
	Box floor;

	private float width;
	private float height;
	private float length;
	
	String textureLocation = "./textures/concrete2_col_low.jpg";

	public Roof(GL2 gl, float width, float height, float length) {
		this.gl = gl;
		this.width = width;
		this.height = height;
		this.length = length;
		
		front = new Box(gl, width+1, 1f, 1f, color, textureLocation);
		left = new Box(gl, 1f, 1f, length+1, color,textureLocation);
		right = new Box(gl, 1f, 1f, length+1, color,textureLocation);
		back = new Box(gl, width +1, 1f, 1f, color,textureLocation);
		floor = new Box(gl, width, 1f, height, color,textureLocation);
	}

	
	@Override
	public void draw() {
		gl.glPushMatrix();
		{
			gl.glTranslatef(0f, height, 0f);
			
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, -0.9f, 0f);
				floor.draw();
			}
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, 0f, -length/2);
				front.draw();
			}
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			{
				gl.glTranslatef(width/2, 0f, 0f);
				left.draw();

			}
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			{
				gl.glTranslatef(0f, 0f, length/2);
				back.draw();

			}
			gl.glPopMatrix();
			
			gl.glPushMatrix();
			{	
				gl.glTranslatef(-width/2, 0f, 0f);
				right.draw();

			}
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
		
	}
}
