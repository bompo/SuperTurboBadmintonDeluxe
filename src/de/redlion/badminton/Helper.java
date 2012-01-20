package de.redlion.badminton;

public class Helper {
	
	public static float map(float value, float fromLow, float fromHigh, float toLow, float toHigh) {
		return (value - fromLow) / fromHigh * (toHigh - toLow) + toLow;
	}		 
	
}
