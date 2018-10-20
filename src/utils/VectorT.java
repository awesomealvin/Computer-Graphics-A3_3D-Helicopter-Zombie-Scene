package utils;

public class VectorT {
	public float x;
	public float y;
	
	public VectorT(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addX(float amount) {
		if (this.x + amount > 1f || this.x + amount < 0f) {
			this.x = 0f;
		}
		
		this.x += amount;
	}
	
	public void addY(float amount) {
		if (this.y + amount > 1f || this.y + amount < 0f) {
			this.y = 0f;
		}
		
		this.y += amount;
	}
	
	public String toString() {
		return "(" + x + ", " + y +")";
	}
}
