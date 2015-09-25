package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.ComponentParser;

import java.util.StringTokenizer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PositionC extends Component {
	public final static String name = "position";

	Vector3 position;
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
		if (EntityManager.getInstance().hasComponent(entityID, ColliderC.class) && !EntityManager.getInstance().hasComponent(entityID, VelocityC.class)){
			EntityManager.getInstance().getComponent(entityID, ColliderC.class).setPos(getX(), getZ());
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
	
	@Override
	public Component cpy() {
		PositionC tmp = new PositionC(new Vector3(position.x,position.y,position.z));
		tmp.angle = angle;
		return tmp;
	}
	@Override
	public void setField(String fieldName, String fieldValue) {
		if(fieldName.equals("angle"))
		{
			angle = Integer.parseInt(fieldValue);
		}
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
