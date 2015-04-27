package Widgetclasses;

import java.util.LinkedList;

public class Moveable extends Widget {

	public int col;
	
	static LinkedList<Moveable> llmoveable = new LinkedList<Moveable>();
	
	public Moveable(String image_path){
		super(image_path);
		llmoveable.add(this);
	}
	
	public static LinkedList<Moveable> getLinkedList(){
		return llmoveable;
	}
	
	public void setPos(int x , int y){
		super.setPos(x, y, true);
	}
	
	public void setPosNoCol(int x, int y){
		super.setPos(x, y, false);
	}
	
}
