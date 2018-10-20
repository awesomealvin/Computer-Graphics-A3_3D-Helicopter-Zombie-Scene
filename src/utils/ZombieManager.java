package utils;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

import shapes.Zombie;

public class ZombieManager implements Drawable{
	
	GLUT glut;
	GL2 gl;
	
	public ArrayList<Zombie> zombies;
	
	public int amount = 300;
	
	public static float min = -80f;
	public static float max = 80f;
	
	public ZombieManager(GL2 gl, GLUT glut) {
		this.gl = gl;
		this.glut = glut;
		
		zombies = new ArrayList<Zombie>();
		
		spawnZombies();
	}

	@Override
	public void draw() {
		for (Zombie z : zombies) {
			z.draw();
		}
	}
	
	public void update(float deltaTime) {
		for (Zombie z : zombies) {
			z.update(deltaTime);
		}
	}
	
	public void spawnZombies() {
		
		for (int i = 0; i < amount; i++) {
		
			Vector pos = new Vector(0f, 0f, 0f);
			Zombie zombie = new Zombie(gl, glut, pos);
			zombie.die();
			zombies.add(zombie);

		}
		
	}

}
