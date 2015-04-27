package Widgetclasses;

import java.util.LinkedList;

public class Obstracle extends Widget {

	public int col_x;
	public int col_y;
	
	static LinkedList<Obstracle> llobstracle = new LinkedList<Obstracle>();
	
	public Obstracle(String s){
		super(s);
		llobstracle.add(this);
	}
	
	public static LinkedList<Obstracle> getLinkedList(){
		return llobstracle;
	}
}
