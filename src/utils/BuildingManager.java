package utils;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import shapes.Building;

public class BuildingManager implements Drawable{
	
	public ArrayList<Building> buildings;
	
	private int minHeight = 4;
	private int maxHeight;
	
	private int minWidth = 4;
	private int maxWidth;
	
	private int minLength = 4;
	private int maxLength;
	
	private int gridSize = 40;
	private int terrainSize = 200;
	private int amount;
	private int amountWidth;
	private int amountLength;
	
	GL2 gl;
	
	public BuildingManager(GL2 gl) {
		this.gl = gl;
		buildings = new ArrayList<Building>();
		maxHeight = 10;
		maxWidth = 10;
		maxLength = 10;
		
		amount = terrainSize/gridSize;
		
		generateBuildings();
	}

	@Override
	public void draw() {
		for (Building b : buildings) {
			b.draw();
		}
	}
	

	
	
	public void generateBuildings() {
		Vector defaultPos = new Vector(-terrainSize/2, 0f, terrainSize/2);
		Vector currentPos = defaultPos.copy();
		
		for (int i = 0; i < amount; i++) {
			for (int k = 0; k < amount; k++) {
				Vector min = new Vector(currentPos.x, 0f, currentPos.z - gridSize);
				Vector max = new Vector(currentPos.x + gridSize, 0f, currentPos.z);
				
				float x = RandomRange.randomRange(min.x, max.x);
				float z = RandomRange.randomRange(min.z, max.z);
				
				int width = RandomRange.randomRange(minWidth,maxWidth);
				int height = RandomRange.randomRange(minHeight,maxHeight);
				int length = RandomRange.randomRange(minLength,maxLength);
				
				generateBuilding(width, height, length, new Vector(x, 0f, z));
				
				currentPos.x += gridSize;
			}
			currentPos.x = defaultPos.x;
			currentPos.z -= gridSize;
		}
	}
	
	public void generateBuilding(int width, int height, int length, Vector position) {
		Building building = new Building(gl,position,width, height, length);
		buildings.add(building);
	}
	
}
