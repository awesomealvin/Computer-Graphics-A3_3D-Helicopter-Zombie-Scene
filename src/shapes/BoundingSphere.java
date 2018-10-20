package shapes;

import utils.Vector;

public class BoundingSphere {
	
	public Vector position;
	public float radius;
	
	public BoundingSphere(Vector position, float radius) {
		this.position = position;
		this.radius = radius;
	}
	
	public boolean contactBox(BoundingBox box) {
		if (box == null) {
			return false;
		}
		float x = Math.max(box.min.x, Math.min(position.x, box.max.x));
		float y = Math.max(box.min.y, Math.min(position.y, box.max.y));
		float z = Math.max(box.min.z, Math.min(position.z, box.max.z));
		
		float distance = (float) Math.sqrt(	(x - position.x) * (x-position.x) +
											(y - position.y) * (y-position.y) +
											(z - position.z) * (z-position.z));
		
		return (distance < radius);
	}
	
	public boolean contactSphere(BoundingSphere other) {

		float distance = (float) Math.sqrt((position.x - other.position.x) * (position.x - other.position.x) +
				(position.y - other.position.y) * (position.y - other.position.y) +
				(position.z - other.position.z) * (position.z - other.position.z));
		
//		System.out.println("thisRadius: " + radius);
//		System.out.println("otherRadius: " + other.radius);
//		System.out.println("thisPosition: "+position);
//		System.out.println("otherPosition: "+other.position);
//		System.out.println();

		return distance < (radius + other.radius);
	}
}
