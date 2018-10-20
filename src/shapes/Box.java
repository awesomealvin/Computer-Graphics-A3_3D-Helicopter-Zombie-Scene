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

public class Box implements Drawable {

	Color color = new Color(165, 96, 36);

	GL2 gl;

	private float width;
	private float height;
	private float length;

	String textureLocation;
	Texture texture;
	boolean hasTexture;
	
	boolean useColor = false;
	
	VectorT[] texCoords;
	
	
	public Box(GL2 gl, float width, float height, float length, Color color, String textureLocation, boolean useColor, VectorT[] texCoords) {
		initializeBasics(gl, width, height, length, color, textureLocation);
		this.useColor = useColor;
		this.texCoords = texCoords;
		initializeTexture();

		buildDisplayList();
	}

	public Box(GL2 gl, float width, float height, float length, Color color, String textureLocation) {
		initializeBasics(gl, width, height, length, color, textureLocation);
		
		texCoords = new VectorT[4];
		texCoords[0] = new VectorT(0, 0);
		texCoords[1] = new VectorT(1, 0);
		texCoords[2] = new VectorT(1, 1);
		texCoords[3] = new VectorT(0, 1);


		initializeTexture();

		buildDisplayList();
	}

	private void initializeBasics(GL2 gl, float width, float height, float length, Color color, String textureLocation) {
		this.gl = gl;
		this.width = width;
		this.height = height;
		this.length = length;
		this.color = color;
		this.textureLocation = textureLocation;
	}
	private void drawTexture() {
		// Draw Texture

		gl.glEnable(GL2.GL_TEXTURE_2D);

		texture.bind(gl);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glLoadIdentity();

		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	private void initializeTexture() {
		try {
			texture = TextureIO.newTexture(new File(textureLocation), true);
			hasTexture = true;
		} catch (IOException e) {
			hasTexture = false;
		}
	}

	int displayListIndex;

	private void buildDisplayList() {

		displayListIndex = gl.glGenLists(1);
		float quadSizeWidth = 1;
		int amountWidth = (int) width;

		float quadSizeHeight = 1;
		int amountHeight = (int) height;

		float quadSizeLength = 1;
		int amountLength = (int) length;

		gl.glNewList(displayListIndex, GL2.GL_COMPILE);

		if (!hasTexture) {
			gl.glColor3f(color.r, color.g, color.b);
		} else {
			gl.glColor3f(1f, 1f, 1f);
		}
		
		if (useColor) {
			gl.glColor3f(color.r, color.g, color.b);
		}

		gl.glBegin(GL2.GL_QUADS);

		// FRONT WALL
		Vector start = new Vector(-width / 2, height, -length / 2);
		Vector quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(0f, 0f, -1f);
		for (int i = 0; i < amountHeight; i++) {
			for (int k = 0; k < amountWidth; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y - quadSizeHeight, quadPos.z);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z);
				quadPos.x += quadSizeWidth;

			}
			quadPos.x = start.x;
			quadPos.y -= quadSizeHeight;
		}

		// LEFT WALL
		start = new Vector(width / 2, height, -length / 2);
		quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(1f, 0f, 0f);
		for (int i = 0; i < amountHeight; i++) {
			for (int k = 0; k < amountLength; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT

				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z + quadSizeLength);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z + quadSizeLength);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z);
				quadPos.z += quadSizeWidth;

			}
			quadPos.z = start.z;
			quadPos.y -= quadSizeHeight;
		}

		// BACK WALL
		start = new Vector(-width / 2, height, length / 2);
		quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(0f, 0f, 1f);
		for (int i = 0; i < amountHeight; i++) {
			for (int k = 0; k < amountWidth; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y - quadSizeHeight, quadPos.z);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z);
				quadPos.x += quadSizeWidth;

			}
			quadPos.x = start.x;
			quadPos.y -= quadSizeHeight;
		}

		// RIGHT WALL
		start = new Vector(-width / 2, height, -length / 2);
		quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(-1f, 0f, 0f);
		for (int i = 0; i < amountHeight; i++) {
			for (int k = 0; k < amountLength; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT

				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z + quadSizeLength);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z + quadSizeLength);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT

				gl.glVertex3f(quadPos.x, quadPos.y - quadSizeHeight, quadPos.z);
				quadPos.z += quadSizeWidth;

			}
			quadPos.z = start.z;
			quadPos.y -= quadSizeHeight;
		}

		// ROOF
		start = new Vector(-width / 2, height, length / 2);
		quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(0f, 1f, 0f);
		for (int i = 0; i < amountLength; i++) {
			for (int k = 0; k < amountWidth; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT

				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z - quadSizeLength);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT

				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z - quadSizeLength);
				quadPos.x += quadSizeWidth;

			}
			quadPos.x = start.x;
			quadPos.z -= quadSizeHeight;
		}

		// FLOOR
		start = new Vector(-width / 2, 0f, length / 2);
		quadPos = new Vector(start.x, start.y, start.z);
		gl.glNormal3f(0f, 1f, 0f);
		for (int i = 0; i < amountLength; i++) {
			for (int k = 0; k < amountWidth; k++) {
				gl.glTexCoord2d(texCoords[0].x, texCoords[0].y); // BOTTOM LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[1].x, texCoords[1].y); // BOTTOM RIGHT
				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z);
				gl.glTexCoord2d(texCoords[2].x, texCoords[2].y); // TOP RIGHT
				gl.glVertex3f(quadPos.x + quadSizeWidth, quadPos.y, quadPos.z - quadSizeLength);
				gl.glTexCoord2d(texCoords[3].x, texCoords[3].y); // TOP LEFT
				gl.glVertex3f(quadPos.x, quadPos.y, quadPos.z - quadSizeLength);
				quadPos.x += quadSizeWidth;

			}
			quadPos.x = start.x;
			quadPos.z -= quadSizeHeight;
		}

		gl.glEnd();
		gl.glEndList();
	}

	@Override
	public void draw() {
		drawTexture();

		gl.glCallList(displayListIndex);
	}
}
