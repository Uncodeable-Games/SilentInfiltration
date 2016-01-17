package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

public class Room {

	// Polygon form;
	public List<Integer> entitiesInRoom = new ArrayList<Integer>();
	public List<TileBorder> allBorders = new ArrayList<TileBorder>();
	public List<TileCorner> allCorners = new ArrayList<TileCorner>();
	public List<TileBorder> allDoors = new ArrayList<TileBorder>();
	List<Tile> tiles = new ArrayList<>();	

	public void addBordersAndCornersfromTile(Tile tile) {
		for (Direction dir : new Direction[] { Direction.N, Direction.S, Direction.W, Direction.E }) {
			if (!allBorders.contains(tile.getBorder(dir))) {
				TileBorder b = tile.getBorder(dir);
				allBorders.add(b);
				if (b.hasColliderEntity()) {
					if (Game.getCurrentGame().getEntityManager().getComponent(b.getColliderEntity(),
							BorderC.class).isDoor) {
						allDoors.add(b);
					}
				}
			}
			if (!allCorners.contains(tile.getCorner(dir))) {
				allCorners.add(tile.getCorner(dir));
			}
		}
	}

	public void addTile(Tile tile) {
		this.tiles.add(tile);
	}

	public List<Tile> getTiles() {
		return this.tiles;
	}

	public void addEntity(int entityId) {
		this.entitiesInRoom.add(entityId);
	}

	public void removeEntity(int entityId) {
		if (this.entitiesInRoom.contains(entityId)) {
			this.entitiesInRoom.remove(entityId);
		}
	}
}
