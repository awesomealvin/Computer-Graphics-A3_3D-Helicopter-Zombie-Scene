package utils;

/**
 * 
 * @author Alvin Tang 15909204
 *
 */
import java.util.Random;

public class RandomRange {
	
	static Random rand = new Random();
	
	public static float randomRange(float min, float max) {
		float value = min + rand.nextFloat() * (max - min);
		return value;
	}
	
	public static int randomRange(int min, int max) {
		int value = rand.nextInt((max-min)+1) + min;
		return value;
	}
}
