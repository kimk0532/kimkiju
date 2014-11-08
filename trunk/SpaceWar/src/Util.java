import java.awt.Color;

public class Util {
	static int rand(int max){
		return (int)(Math.random()*(max + 1));
	}
	static int rand(int min, int max){
		return min + (int)(Math.random()*(max - min + 1));
	}
	static double rand(double min, double max){
		return min + (int)(Math.random()*(max - min + 1));
	}
	static boolean prob100(int r){
		return (int)(Math.random()*100) <= r;
	}
	static Color randColor(){
		return new Color(rand(255), rand(255), rand(255));
	}
	static Color randColor(int min, int max){
		return new Color(rand(min, max), rand(min, max), rand(min, max));
	}
}
