package com.MiH.game.components;

import java.util.Vector;

import com.MiH.engine.ecs.Component;


public class Collision extends Component {

	public boolean isCircle = true;
	public float ccol=0;
	public Vector v = new Vector(0,0);
	public boolean collidesWithWorld;
	
	public Collision(boolean isCircle, boolean collidesWithWorld) {
		this.isCircle = isCircle;
		this.collidesWithWorld = collidesWithWorld;
	}
	
}
