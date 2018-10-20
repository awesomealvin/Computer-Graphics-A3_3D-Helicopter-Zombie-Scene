package shapes;

import java.io.File;
import java.io.IOException;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import utils.Color;
import utils.Drawable;
import utils.Vector;
import utils.VectorT;

public class Ground implements Drawable {

	private GL2 gl;

	public int size = 200;
	public float gridSize = 1f;
	public int amount;
	public Quad quads[][];
	public int blockSize = 20;

	boolean clampS = false;
	boolean clampT = false;

	Color color = new Color(174, 177, 183);
	// Color color = new Color(1f, 1f, 0f);

	Texture texture;

	public Ground(GL2 gl) {
		this.gl = gl;
		amount = (int) (size / gridSize);
		quads = new Quad[amount][amount];
		
		int repeats = (int) ((size/gridSize)/blockSize);

		// Initialize Quads
		
		float defaultX = (-size / 2f) + (gridSize / 2);
		float defaultZ = (size / 2f) - (gridSize / 2);
		// System.out.println("Start X: " + defaultX);

		Vector currentPos = new Vector(defaultX, 0f, defaultZ);
		
		int count = 0;
		
		for (int z = 0; z < repeats *2; z++) {
			
			for (int x = 0; x < repeats * 2; x++) {
				
				// Per Blocks
				float textureDifference = 1f/(blockSize/2);
//				gl.glTexCoord2d(0, 0); // BOTTOM LEFT
//				gl.glTexCoord2d(1, 0); // BOTTOM RIGHT
//				gl.glTexCoord2d(1, 1); // TOP RIGHT
//				gl.glTexCoord2d(0, 1); // TOP LEFT
				VectorT currentTexPos = new VectorT(0f, 1f-textureDifference);
				
				for (int i = z * (blockSize/2); i < blockSize/2 * (z+1); i++) {
					currentTexPos.x = 0f;
					for (int k = x * (blockSize/2); k < blockSize/2 * (x+1); k++) {
						
						// TEXTURE POSITIONS
						VectorT bottomLeft = new VectorT(currentTexPos.x, currentTexPos.y);
						VectorT bottomRight = new VectorT(currentTexPos.x + textureDifference, currentTexPos.y);
						VectorT topRight = new VectorT(currentTexPos.x + textureDifference, currentTexPos.y + textureDifference);
						VectorT topLeft = new VectorT(currentTexPos.x, currentTexPos.y + textureDifference);
						VectorT texturePos[] = {bottomLeft, bottomRight, topRight, topLeft};
						for (VectorT t : texturePos) {
//							System.out.println(t);
						}
						Vector pos = new Vector(currentPos.x, 0f, currentPos.z);

						quads[i][k] = new Quad(gl, gridSize, pos, texturePos, color);
						
						count++;
						
						currentPos.x += gridSize;
						currentTexPos.x += textureDifference;

					}
					currentPos.x = defaultX + (gridSize * (x*(blockSize/2)));
					currentPos.z -= gridSize;
					currentTexPos.y -= textureDifference;

				}
				
				currentPos.z = defaultZ - (gridSize * (z*(blockSize/2)));
			}
			
			currentPos.x = defaultX;
			
		}
		

		for (int i = 0; i < quads.length; i++) {
			for (int k = 0; k < quads[i].length; k++) {
				Vector pos = new Vector(currentPos.x, 0f, currentPos.z);
//				quads[i][k] = new Quad(gl, gridSize, pos, color);
				currentPos.x += gridSize;

			}
			currentPos.x = defaultX;
			currentPos.z -= gridSize;
		}

		// Load Textures
		try {
			texture = TextureIO.newTexture(new File("./textures/concrete-patch-pattern_low.jpg"), true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void drawTexture() {
		// Draw Texture

		gl.glEnable(GL2.GL_TEXTURE_2D);

		texture.bind(gl);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, clampS ? GL2.GL_CLAMP : GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, clampT ? GL2.GL_CLAMP : GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	@Override
	public void draw() {

		drawTexture();
		
//		quads[99][99].draw();
		gl.glNormal3f(0f, 1f, 0f);

		for (int i = 0; i < quads.length; i++) {
			for (int k = 0; k < quads[i].length; k++) {
				quads[i][k].draw();
			}
		}
	}

}
