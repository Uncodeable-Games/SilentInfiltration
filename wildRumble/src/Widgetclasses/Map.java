package Widgetclasses;

public class Map {

	final double  COL_SCALE = 2.0;
	
	int[][] map;
	int size_x;
	int size_y;
	
	public Map(int x , int y){
		size_x = x;
		size_y = y;

	}
	
	public int getSizeX(){
		return size_x;
	}
	
	public int getSizeY(){
		return size_y;
	}
}
