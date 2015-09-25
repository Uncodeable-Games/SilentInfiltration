package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class Tilemap {

	float TILESIZE;

	// Vector2[][] tileCorners;
	String name;
	public List<Room> rooms = new ArrayList<>();
	Tile[][] tilemap;
	// TileBorder[][] borders;
	private List<TileBorder> borders = new ArrayList<>();
	private int length;

	private int width;

	public Tilemap(int length, int width, float tilesize) {
		this.setLength(length);
		this.setWidth(width);
		this.TILESIZE = tilesize;

		this.tilemap = new Tile[width][length];
		this.createTilemap();
		// this.calculateRooms();
	}

	public Tile getTileAt(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < length) {
			return tilemap[x][y];
		}
		return null;
	}

	public Room getRoomAt(int x, int y) {
		return getTileAt(x, y).getRoom();
	}

	public int coordToIndex_x(float x) {
		return Math.round(x / TILESIZE);
	}

	public int coordToIndex_z(float z) {
		return Math.round(z / TILESIZE);
	}

	private void createTilemap() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getLength(); y++) {
				Tile tmp = new Tile(TILESIZE * (float) x + TILESIZE / 2f, 0, TILESIZE * (float) y + TILESIZE / 2f,
						this);
				tmp.setX(x);
				tmp.setY(y);
				for (Direction direction : new Direction[] { Direction.E, Direction.N }) {
					Tile neighbour = null;
					Vector3 borderCenterOffset = new Vector3();
					float angle = 0f;
					if (direction == Direction.E) {
						borderCenterOffset.x += TILESIZE / 2f;
						angle = 90f;
						if (x > 0)
							neighbour = tilemap[x - 1][y];
					}
					/*
					 * else if (direction == Direction.W) { borderCenterOffset.x
					 * += TILESIZE/2f; if(x < width) neighbour =
					 * tilemap[x+1][y]; }
					 */
					else if (direction == Direction.N) {
						borderCenterOffset.z += TILESIZE / 2f;
						if (y > 0)
							neighbour = tilemap[x][y - 1];
					}
					/*
					 * else if (direction == Direction.S) { borderCenterOffset.y
					 * += TILESIZE/2f; if(y < length) neighbour =
					 * tilemap[x][y+1]; }
					 */

					// if(!tmp.hasBorder(direction))
					// {
					Vector3 borderCenter = tmp.center.cpy();
					borderCenter.sub(borderCenterOffset);

					TileBorder border = new TileBorder(borderCenter);
					border.angle = angle;
					border.setAdjacent(tmp);
					if (neighbour != null) {
						border.setAdjacent(neighbour);
						neighbour.setBorder(direction.getOppositeDirection(), border);
					}
					this.borders.add(border);
					tmp.setBorder(direction, border);
					// }
				}
				tilemap[x][y] = tmp;
			}
		}
	}

	public void setRoomforTile(Room r, Tile t) {
		t.setRoom(r);
		if (t.hasNeighbour(Direction.E) && !t.getBorder(Direction.E).hasColliderEntity() && !t.getNeighour(Direction.E).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.E));
		}
		if (t.hasNeighbour(Direction.N) && !t.getBorder(Direction.N).hasColliderEntity() && !t.getNeighour(Direction.N).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.N));
		}
		if (t.hasNeighbour(Direction.W) && !t.getBorder(Direction.W).hasColliderEntity() && !t.getNeighour(Direction.W).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.W));
		}
		if (t.hasNeighbour(Direction.S) && !t.getBorder(Direction.S).hasColliderEntity() && !t.getNeighour(Direction.S).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.S));
		}
	}

	public void calculateRooms() {
		for (Room r : rooms) {
			for (Tile t : r.getTiles())
				t.setRoom(null);
			r.tiles.clear();
		}
		this.rooms.clear();

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getLength(); y++) {
				if (!getTileAt(x, y).hasRoom()) {
					Room r = new Room();
					rooms.add(r);
					setRoomforTile(r, getTileAt(x, y));
				}
			}
		}

		for (Room room : this.rooms) {
			room.calculateCenter();
		}

		EntityManager entityM = EntityManager.getInstance();
		for (int i = 0; i < EntityManager.getInstance().entityCount; i++) {
			if (entityM.hasComponent(i, PositionC.class) && entityM.hasComponent(i, ColliderC.class)
					&& !entityM.hasComponent(i, VelocityC.class) && !entityM.hasComponent(i, BorderC.class)) {
				Room r = getTileAt(coordToIndex_x(entityM.getComponent(i, PositionC.class).getX()),
						coordToIndex_x(entityM.getComponent(i, PositionC.class).getZ())).getRoom();
				System.out.println(i+"("+(int)entityM.getComponent(i, PositionC.class).getX()+","+(int)entityM.getComponent(i, PositionC.class).getZ()+") added to "+r);
				r.entitiesInRoom.add(i);
			}
		}
	}

	public float getTILESIZE() {
		return TILESIZE;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<TileBorder> getBorders() {
		return borders;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
