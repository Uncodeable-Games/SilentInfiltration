package Widgetclasses;

import java.util.LinkedList;

public class Units extends Moveable {
	
	String name;
	
	static LinkedList<Units> llunits = new LinkedList<Units>();
	
	public Units(String s, String image_path){
		super(image_path);
		llunits.add(this);	
		name = s;
		
	}
	
	static public LinkedList<Units> getList(){
		return llunits;
	}
	
	public void input(){
			
	}
}