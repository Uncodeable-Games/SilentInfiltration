package de.mih.core.engine.tilemap;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.game.Game;

public class Wall {

	public Wall(TileBorder border){
		this.border = border;
		colliderEntity = Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("wall");
	}
	
	static HashMap<TileBorder, Wall> walls = new HashMap<TileBorder,Wall>();
	
	int colliderEntity = -1;
	
	private TileBorder border;
	
	public TileBorder getTileBorder(){
		return border;
	}
	
	public Vector2 getPos(){
		return border.getPos();
	}
	
	public int getColliderEntity(){
		return colliderEntity;
	}
	
	public void setColliderEntity(int entity){
		colliderEntity = entity;
	}
}