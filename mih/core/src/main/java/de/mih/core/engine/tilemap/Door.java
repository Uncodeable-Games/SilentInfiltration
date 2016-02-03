package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Node;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;

public class Door implements Node {

	static HashMap<TileBorder, Door> doors = new HashMap<TileBorder, Door>();

	public Door(TileBorder border) {
		this.border = border;
		setColliderEntity(Game.getCurrentGame().getBlueprintManager().createEntityFromBlueprint("door"));
	}

	private TileBorder border;

	int colliderEntity = -1;

	@Override
	public float getDistance(Node target) {
		if (target instanceof Door) {
			return Game.getCurrentGame().getNavigationManager().getDoorNeighbours(this).get((Door)target);
		} else if (target instanceof NavPoint) {
			return ((NavPoint) target).getDistance(this);
		}
		return Float.MAX_VALUE;
	}

	public TileBorder getTileBorder() {
		return border;
	}

	public Vector2 getPos() {
		return border.getPos();
	}

	public int getColliderEntity() {
		return colliderEntity;
	}

	public void setColliderEntity(int entity) {
		colliderEntity = entity;
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class)
				.setPos(new Vector3(this.border.getPos().x, 0, this.border.getPos().y));
		Game.getCurrentGame().getEntityManager().getComponent(entity, PositionC.class).setAngle(this.border.angle);
	}

	public boolean hasSameRoom(Door door) {
		for (Room room : Game.getCurrentGame().getTilemap().getRooms()) {
			if (room.allDoors.contains(this) && room.allDoors.contains(door))
				return true;
		}
		return false;
	}

	public Room getSameRoom(Door door) {
		for (Room room : Game.getCurrentGame().getTilemap().getRooms()) {
			if (room.allDoors.contains(this) && room.allDoors.contains(door))
				return room;
		}
		return null;
	}

	
	//TODO: Check if last is in next room!
	@Override
	public ArrayList<Node> getNeighbours(NavPoint last) {
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (Node node:Game.getCurrentGame().getNavigationManager().getDoorNeighbours(this).keySet()){
			neighbours.add(node);
		}
		if (last.getRoom().allDoors.contains(this)) neighbours.add(last);
		return new ArrayList<Node>();
	}
}
