package de.mih.core.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;

public class ColliderC extends Component {

	public final static float COLLIDER_RADIUS = 0.3f;

	float width,length = 0;
	ArrayList<NavPoint> navpoints = new ArrayList<NavPoint>();

	public ColliderC() {
	}
	
	public ColliderC(float width, float length){
		this.width = width;
		this.length = length;
	}

	public void setScale(float x, float y) {
		this.width *= x;
		this.length *= y;
	}
	
	public float getWidth(){
		return width;
	}
	
	public void setWidth(float width){
		this.width = width;
	}

	public float getLength(){
		return length;
	}
	
	public void setLength(float length){
		this.length = length;
	}
	
	public void addNavPoint(NavPoint nav){
		navpoints.add(nav);
	}
	
	public void removeNavPoint(NavPoint nav){
		navpoints.remove(nav);
	}
	
	public boolean hasNavPoint(NavPoint nav){
		return navpoints.contains(nav);
	}
	
	public ArrayList<NavPoint> getAllNavPoints(){
		return navpoints;
	}
	
	public void clearNavPoints(){
		navpoints.clear();
	}
}
