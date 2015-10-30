package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.Game;

import com.badlogic.gdx.math.Vector3;

public class PositionC extends Component {

	public Vector3 position;
	public Vector3 facing = new Vector3();
	float angle;
	
	public PositionC()
	{
		this.position = new Vector3();
	}
	public PositionC(Vector3 position){
		this.position = new Vector3();
		setPos(position);
	}
	
	public void setPos(Vector3 vec){
		setPos(vec.x,vec.y,vec.z);
	}
	
	public void setPos(float x, float y, float z){
		position.x = x;
		position.y = y;
		position.z = z;
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityID, ColliderC.class) && !Game.getCurrentGame().getEntityManager().hasComponent(entityID, VelocityC.class)){
			Game.getCurrentGame().getEntityManager().getComponent(entityID, ColliderC.class).setPos(getX(), getZ());
		}
	}
	
	public float getX(){
		return position.x;
	}
	
	public float getY(){
		return position.y;
	}
	
	public float getZ(){
		return position.z;
	}
	
	public void setX(float x){
		setPos(x,getY(),getZ());
	}
	public void setY(float y){
		setPos(getX(),y,getZ());
	}
	public void setZ(float z){
		setPos(getX(),getY(),z);
	}

	public Vector3 getPos(){
		return position;
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public float getAngle(){
		return this.angle;
	}
	
}
