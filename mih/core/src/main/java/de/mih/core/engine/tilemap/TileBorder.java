package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

public class TileBorder {

	Tile adjacentTile1, adjacentTile2;
	public float angle;

	int colliderEntity = -1;
	Vector3 center;

	public HashMap<Direction, TileCorner> corners = new HashMap<>();

	public TileBorder(float x, float y, float z) {
		this(new Vector3(x, y, z));
	}

	public TileBorder(Vector3 center) {
		this.center = center;
	}

	public Vector3 getCenter() {
		return center;
	}

	public void setAdjacent(Tile tile) {
		if (adjacentTile1 == null || tile == adjacentTile1)
			adjacentTile1 = tile;
		else if (adjacentTile2 == null || tile == adjacentTile2)
			adjacentTile2 = tile;
		else {
			System.out.println("ERROR");
		}
	}

	public Tile getAdjacentTile(Tile tile) {
		return tile == adjacentTile1 ? adjacentTile2 : adjacentTile1;
	}

	public void removeColliderEntity() {
		// TODO: resolve to outside maybe? why should the tileborder be deleting
		// entities?
		Game.getCurrentGame().getEntityManager().removeEntity(this.colliderEntity);
		this.colliderEntity = -1;
	}

	public void setColliderEntity(int entityID) {
		this.colliderEntity = entityID;

		Game.getCurrentGame().getEntityManager().getComponent(entityID, PositionC.class).setPos(this.center);
		Game.getCurrentGame().getEntityManager().getComponent(entityID, PositionC.class).setAngle(this.angle);
	}

	public int getColliderEntity() {
		return this.colliderEntity;
	}

	public boolean hasColliderEntity() {
		return this.colliderEntity > -1;
	}

	public boolean isHorizontal() {
		return corners.containsKey(Direction.E);
	}

	public boolean isVertical() {
		return !isHorizontal();
	}

	public Vector2 getPos() {
		Tile tile = adjacentTile1;
		Direction dir = adjacentTile1.getBorderDirection(this);
		Vector2 pos = new Vector2();
		switch (dir) {
		case N: {
			pos.x = tile.center.x;
			pos.y = tile.center.z - tile.getTilemap().getTILESIZE() / 2f;
			break;
		}
		case E: {
			pos.x = tile.center.x + tile.getTilemap().getTILESIZE() / 2f;
			pos.y = tile.center.z;
			break;
		}
		case S: {
			pos.x = tile.center.x;
			pos.y = tile.center.z + tile.getTilemap().getTILESIZE() / 2f;
			break;
		}
		case W: {
			pos.x = tile.center.x - tile.getTilemap().getTILESIZE() / 2f;
			pos.y = tile.center.z;
			break;
		}
		}
		return pos;
	}

	public List<Tile> getTiles() {
		List<Tile> adjacentTiles = new ArrayList<>();
		if (adjacentTile1 != null) {
			adjacentTiles.add(adjacentTile1);
		}
		if (adjacentTile2 != null) {
			adjacentTiles.add(adjacentTile2);
		}
		return adjacentTiles;
	}

}