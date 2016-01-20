package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;

public class Tilemap {

	float TILESIZE;

	String name;
	public List<Room> rooms = new ArrayList<>();
	Tile[][] tilemap;
	private List<TileBorder> borders = new ArrayList<>();
	private int length;

	private int width;

	private EntityManager entityManager;

	public Tilemap(int length, int width, float tilesize, EntityManager entityManager) {
		this.setLength(length);
		this.setWidth(width);
		this.TILESIZE = tilesize;

		this.tilemap = new Tile[width][length];
		this.createTilemap();
		this.entityManager = entityManager;
	}

	public Tile getTileAt(float x, float y) {
		if (x >= 0 && x < width && y >= 0 && y < length) {
			return tilemap[(int) x][(int) y];
		}
		return null;
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

	public int coordToIndex(float x) {
		return (int) (x / TILESIZE);
	}

	// TODO: Check Directions!
	private void createTilemap() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getLength(); y++) {
				Tile tmp = new Tile(TILESIZE * (float) x + TILESIZE / 2f, 0, TILESIZE * (float) y + TILESIZE / 2f,
						this);
				tmp.setX(x);
				tmp.setY(y);
				tilemap[x][y] = tmp;
			}
		}
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getLength(); y++) {

				TileBorder newtb;
				Tile temp;

				// North Border
				if (y == 0) {
					newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(0, 0, -TILESIZE / 2f));
					tilemap[x][y].setBorder(Direction.N, newtb);
					borders.add(newtb);
				} else {
					temp = tilemap[x][y - 1];
					if (temp.borders.containsKey(Direction.S)) {
						tilemap[x][y].setBorder(Direction.N, temp.getBorder(Direction.S));
					} else {
						newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(0, 0, -TILESIZE / 2f));
						tilemap[x][y].setBorder(Direction.N, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.S, newtb);
					}
				}

				// West Border
				if (x == 0) {
					newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(-TILESIZE / 2f, 0, 0));
					newtb.angle = 90f;
					tilemap[x][y].setBorder(Direction.W, newtb);
					borders.add(newtb);
				} else {
					temp = tilemap[x - 1][y];
					if (temp.borders.containsKey(Direction.E)) {
						tilemap[x][y].setBorder(Direction.W, temp.getBorder(Direction.E));
					} else {
						newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(-TILESIZE / 2f, 0, 0));
						newtb.angle = 90f;
						tilemap[x][y].setBorder(Direction.W, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.E, newtb);
					}
				}

				// South Border
				if (y == tilemap[0].length - 1) {
					newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(0, 0, TILESIZE / 2f));
					tilemap[x][y].setBorder(Direction.S, newtb);
					borders.add(newtb);
				} else {
					temp = tilemap[x][y + 1];
					if (temp.borders.containsKey(Direction.N)) {
						tilemap[x][y].setBorder(Direction.S, temp.getBorder(Direction.N));
					} else {
						newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(0, 0, TILESIZE / 2f));
						tilemap[x][y].setBorder(Direction.S, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.N, newtb);
					}
				}

				// East Border
				if (x == tilemap.length - 1) {
					newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(TILESIZE / 2f, 0, 0));
					newtb.angle = 90f;
					tilemap[x][y].setBorder(Direction.E, newtb);
					borders.add(newtb);
				} else {
					temp = tilemap[x + 1][y];
					if (temp.borders.containsKey(Direction.W)) {
						tilemap[x][y].setBorder(Direction.E, temp.getBorder(Direction.W));
					} else {
						newtb = new TileBorder(new Vector3(tilemap[x][y].center).add(TILESIZE / 2f, 0, 0));
						newtb.angle = 90f;
						tilemap[x][y].setBorder(Direction.E, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.W, newtb);
					}
				}
			}
		}

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getLength(); y++) {

				TileCorner tmp = new TileCorner();

				tilemap[x][y].setCorner(Direction.N, tmp);
				tmp.adjacentTiles.put(Direction.S, tilemap[x][y]);

				tmp.adjacentBorders.put(Direction.E, tilemap[x][y].getBorder(Direction.N));
				tilemap[x][y].getBorder(Direction.N).corners.put(Direction.W, tmp);
				tmp.adjacentBorders.put(Direction.S, tilemap[x][y].getBorder(Direction.W));
				tilemap[x][y].getBorder(Direction.W).corners.put(Direction.N, tmp);

				if (y != 0) {
					tilemap[x][y - 1].setCorner(Direction.W, tmp);
					tmp.adjacentTiles.put(Direction.E, tilemap[x][y - 1]);

					tmp.adjacentBorders.put(Direction.N, tilemap[x][y - 1].getBorder(Direction.W));
					tilemap[x][y - 1].getBorder(Direction.W).corners.put(Direction.S, tmp);
				}

				if (x != 0) {
					tilemap[x - 1][y].setCorner(Direction.E, tmp);
					tmp.adjacentTiles.put(Direction.W, tilemap[x - 1][y]);

					tmp.adjacentBorders.put(Direction.W, tilemap[x - 1][y].getBorder(Direction.N));
					tilemap[x - 1][y].getBorder(Direction.N).corners.put(Direction.E, tmp);
				}
				if (x != 0 && y != 0) {
					tilemap[x - 1][y - 1].setCorner(Direction.S, tmp);
					tmp.adjacentTiles.put(Direction.N, tilemap[x - 1][y - 1]);
				}
			}
		}

		for (int x = 0; x < getWidth(); x++) {
			TileCorner tmp = new TileCorner();

			tilemap[x][getLength() - 1].setCorner(Direction.W, tmp);
			tmp.adjacentTiles.put(Direction.E, tilemap[x][getLength() - 1]);

			tmp.adjacentBorders.put(Direction.E, tilemap[x][getLength() - 1].getBorder(Direction.S));
			tilemap[x][getLength() - 1].getBorder(Direction.S).corners.put(Direction.W, tmp);
			tmp.adjacentBorders.put(Direction.N, tilemap[x][getLength() - 1].getBorder(Direction.W));
			tilemap[x][getLength() - 1].getBorder(Direction.W).corners.put(Direction.S, tmp);

			if (x != 0) {
				tilemap[x - 1][getLength() - 1].setCorner(Direction.S, tmp);
				tmp.adjacentTiles.put(Direction.N, tilemap[x - 1][getLength() - 1]);
				tmp.adjacentBorders.put(Direction.W, tilemap[x - 1][getLength() - 1].getBorder(Direction.S));
				tilemap[x - 1][getLength() - 1].getBorder(Direction.S).corners.put(Direction.E, tmp);
			}
		}

		for (int y = 0; y < getLength(); y++) {
			TileCorner tmp = new TileCorner();

			tilemap[getWidth() - 1][y].setCorner(Direction.E, tmp);
			tmp.adjacentTiles.put(Direction.W, tilemap[getWidth() - 1][y]);

			tmp.adjacentBorders.put(Direction.W, tilemap[getWidth() - 1][y].getBorder(Direction.N));
			tilemap[getWidth() - 1][y].getBorder(Direction.N).corners.put(Direction.E, tmp);
			tmp.adjacentBorders.put(Direction.S, tilemap[getWidth() - 1][y].getBorder(Direction.E));
			tilemap[getWidth() - 1][y].getBorder(Direction.E).corners.put(Direction.N, tmp);

			if (y != 0) {
				tilemap[getWidth() - 1][y - 1].setCorner(Direction.S, tmp);
				tmp.adjacentTiles.put(Direction.N, tilemap[getWidth() - 1][y - 1]);

				tmp.adjacentBorders.put(Direction.N, tilemap[getWidth() - 1][y - 1].getBorder(Direction.E));
				tilemap[getWidth() - 1][y - 1].getBorder(Direction.E).corners.put(Direction.S, tmp);
			}
		}

		TileCorner tmp = new TileCorner();

		tilemap[getWidth() - 1][getLength() - 1].setCorner(Direction.S, tmp);
		tmp.adjacentTiles.put(Direction.N, tilemap[getWidth() - 1][getLength() - 1]);

		tmp.adjacentBorders.put(Direction.N, tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.E));
		tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.E).corners.put(Direction.S, tmp);
		tmp.adjacentBorders.put(Direction.W, tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.S));
		tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.S).corners.put(Direction.E, tmp);
	}

	public void setRoomforTile(Room r, Tile t) {
		t.setRoom(r);
		r.addTile(t);
		r.addBordersAndCornersfromTile(t);
		if (t.hasNeighbour(Direction.E) && !t.getBorder(Direction.E).hasColliderEntity()
				&& !t.getNeighour(Direction.E).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.E));
		}
		if (t.hasNeighbour(Direction.N) && !t.getBorder(Direction.N).hasColliderEntity()
				&& !t.getNeighour(Direction.N).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.N));
		}
		if (t.hasNeighbour(Direction.W) && !t.getBorder(Direction.W).hasColliderEntity()
				&& !t.getNeighour(Direction.W).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.W));
		}
		if (t.hasNeighbour(Direction.S) && !t.getBorder(Direction.S).hasColliderEntity()
				&& !t.getNeighour(Direction.S).hasRoom()) {
			setRoomforTile(r, t.getNeighour(Direction.S));
		}
	}

	public void calculateRooms() {
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

		for (int i = 0; i < entityManager.entityCount; i++) {
			if (entityManager.hasComponent(i, PositionC.class) && entityManager.hasComponent(i, ColliderC.class)
					&& !entityManager.hasComponent(i, VelocityC.class)
					&& !entityManager.hasComponent(i, BorderC.class)) {
				Room r = getTileAt(coordToIndex(entityManager.getComponent(i, PositionC.class).getX()),
						coordToIndex(entityManager.getComponent(i, PositionC.class).getZ())).getRoom();
				r.addEntity(i);
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

	public List<Room> getRooms() {
		return rooms;
	}
}
