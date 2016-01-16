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
	public List<NavPoint> allNavPoints = new ArrayList<NavPoint>();
	public List<TileBorder> allBorders = new ArrayList<TileBorder>();
	public List<TileCorner> allCorners = new ArrayList<TileCorner>();
	public List<TileBorder> allDoors = new ArrayList<TileBorder>();
	List<Tile> tiles = new ArrayList<>();

	public Room() {
	}

	public void calculateVisibility() {
		for (NavPoint nav : allNavPoints) {
			nav.calculateVisibility(this);
		}
	}

	public void routeNavPoints() {
		for (NavPoint nav : allNavPoints) {
			nav.route();
		}
	}
	
	public void addEntityNavPoints() {
		for (int i : entitiesInRoom) {
			ColliderC col = Game.getCurrentGame().getEntityManager().getComponent(i, ColliderC.class);
			PositionC pos = Game.getCurrentGame().getEntityManager().getComponent(i, PositionC.class);
			col.clearNavPoints();

			col.addNavPoint(new NavPoint(
					pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f),
					pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f)));
			col.addNavPoint(new NavPoint(
					pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f),
					pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f)));
			col.addNavPoint(new NavPoint(
					pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f),
					pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f)));
			col.addNavPoint(new NavPoint(
					pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f),
					pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f) + 0.1f)));

			addNavPoints(i);
		}
	}

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

	// TODO: Check for room before adding!
	public void addBorderNavPoints() {

		for (TileCorner corner : allCorners) {
			corner.checked = false;
		}

		// Iterate over all Borders in this room
		for (TileBorder border : allBorders) {

			// skip Border if it doesn't have a Collider
			if (!border.hasColliderEntity())
				continue;

			if (allDoors.contains(border))
				continue;

			// If the border is horizontal...
			if (border.isHorizontal()) {

				// Check east corner...
				TileCorner east = border.corners.get(Direction.E);
				// if the east corner is NOT at the border of the tilemap...
				// (you can ignore the corners at the borders of the tilemap)
				if (!east.checked && east.adjacentBorders.containsKey(Direction.N)
						&& east.adjacentBorders.containsKey(Direction.E)
						&& east.adjacentBorders.containsKey(Direction.S)
						&& east.adjacentBorders.containsKey(Direction.W)) {
					// if the east corner doesn't have another border to the
					// east OR has a border to the north AND to the south...
					// (If the east corner has another east border the wall
					// still goes on. If there's a north and a south corner it's
					// just a T-corner)
					east.checked = true;
					if (!(east.adjacentBorders.get(Direction.E).hasColliderEntity()
							|| (east.adjacentBorders.get(Direction.N).hasColliderEntity()
									&& east.adjacentBorders.get(Direction.S).hasColliderEntity()))) {

						// If there's no north border set a NavPoint
						if (!east.adjacentBorders.get(Direction.N).hasColliderEntity()
								&& east.adjacentTiles.get(Direction.N).getRoom() == this) {
							NavPoint nav = new NavPoint(
									east.getPos().x + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									east.getPos().y - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(east.adjacentBorders.get(Direction.W).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (east.adjacentBorders.get(Direction.S).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(east.adjacentBorders.get(Direction.S).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
						// If there's no south border set a NavPoint
						if (!east.adjacentBorders.get(Direction.S).hasColliderEntity()
								&& east.adjacentTiles.get(Direction.W).getRoom() == this) {
							NavPoint nav = new NavPoint(
									east.getPos().x + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									east.getPos().y + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(east.adjacentBorders.get(Direction.W).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (east.adjacentBorders.get(Direction.N).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(east.adjacentBorders.get(Direction.N).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
					}
				}

				// West
				TileCorner west = border.corners.get(Direction.W);
				if (!west.checked && west.adjacentBorders.containsKey(Direction.N)
						&& west.adjacentBorders.containsKey(Direction.E)
						&& west.adjacentBorders.containsKey(Direction.S)
						&& west.adjacentBorders.containsKey(Direction.W)) {
					west.checked = true;
					if (!(west.adjacentBorders.get(Direction.W).hasColliderEntity()
							|| (west.adjacentBorders.get(Direction.N).hasColliderEntity()
									&& west.adjacentBorders.get(Direction.S).hasColliderEntity()))) {

						if (!west.adjacentBorders.get(Direction.N).hasColliderEntity()
								&& west.adjacentTiles.get(Direction.E).getRoom() == this) {
							NavPoint nav = new NavPoint(
									west.getPos().x - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									west.getPos().y - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(west.adjacentBorders.get(Direction.E).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (west.adjacentBorders.get(Direction.S).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(west.adjacentBorders.get(Direction.S).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
						if (!west.adjacentBorders.get(Direction.S).hasColliderEntity()
								&& west.adjacentTiles.get(Direction.S).getRoom() == this) {
							NavPoint nav = new NavPoint(
									west.getPos().x - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									west.getPos().y + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(west.adjacentBorders.get(Direction.E).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (west.adjacentBorders.get(Direction.N).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(west.adjacentBorders.get(Direction.N).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
					}
				}
				// If the border is vertical
			} else {

				// North
				TileCorner north = border.corners.get(Direction.N);
				if (!north.checked && north.adjacentBorders.containsKey(Direction.N)
						&& north.adjacentBorders.containsKey(Direction.E)
						&& north.adjacentBorders.containsKey(Direction.S)
						&& north.adjacentBorders.containsKey(Direction.W)) {
					north.checked = true;
					if (!(north.adjacentBorders.get(Direction.N).hasColliderEntity()
							|| (north.adjacentBorders.get(Direction.E).hasColliderEntity()
									&& north.adjacentBorders.get(Direction.W).hasColliderEntity()))) {

						if (!north.adjacentBorders.get(Direction.E).hasColliderEntity()
								&& north.adjacentTiles.get(Direction.E).getRoom() == this) {
							NavPoint nav = new NavPoint(
									north.getPos().x + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									north.getPos().y - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(north.adjacentBorders.get(Direction.S).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (north.adjacentBorders.get(Direction.W).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(north.adjacentBorders.get(Direction.W).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
						if (!north.adjacentBorders.get(Direction.W).hasColliderEntity()
								&& north.adjacentTiles.get(Direction.N).getRoom() == this) {
							NavPoint nav = new NavPoint(
									north.getPos().x - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									north.getPos().y - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(north.adjacentBorders.get(Direction.S).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (north.adjacentBorders.get(Direction.E).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(north.adjacentBorders.get(Direction.E).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
					}
				}

				// South
				TileCorner south = border.corners.get(Direction.S);
				if (!south.checked && south.adjacentBorders.containsKey(Direction.N)
						&& south.adjacentBorders.containsKey(Direction.E)
						&& south.adjacentBorders.containsKey(Direction.S)
						&& south.adjacentBorders.containsKey(Direction.W)) {
					south.checked = true;
					if (!(south.adjacentBorders.get(Direction.S).hasColliderEntity()
							|| (south.adjacentBorders.get(Direction.E).hasColliderEntity()
									&& south.adjacentBorders.get(Direction.W).hasColliderEntity()))) {

						if (!south.adjacentBorders.get(Direction.E).hasColliderEntity()
								&& south.adjacentTiles.get(Direction.S).getRoom() == this) {
							NavPoint nav = new NavPoint(
									south.getPos().x + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									south.getPos().y + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(south.adjacentBorders.get(Direction.N).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (south.adjacentBorders.get(Direction.W).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(south.adjacentBorders.get(Direction.W).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
						if (!south.adjacentBorders.get(Direction.W).hasColliderEntity()
								&& south.adjacentTiles.get(Direction.W).getRoom() == this) {
							NavPoint nav = new NavPoint(
									south.getPos().x - 0.1f - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									south.getPos().y + 0.1f + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f);
							allNavPoints.add(nav);
							Game.getCurrentGame().getEntityManager()
									.getComponent(south.adjacentBorders.get(Direction.N).getColliderEntity(),
											ColliderC.class)
									.addNavPoint(nav);
							if (south.adjacentBorders.get(Direction.E).hasColliderEntity()) {
								Game.getCurrentGame().getEntityManager()
										.getComponent(south.adjacentBorders.get(Direction.E).getColliderEntity(),
												ColliderC.class)
										.addNavPoint(nav);
							}
						}
					}
				}
			}
		}

		for (TileBorder door : allDoors) {

			if (Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(), BorderC.class).checked)
				continue;

			Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
					BorderC.class).checked = true;
			Tilemap t = Game.getCurrentGame().getTilemap();

			if (door.isHorizontal()) {
				NavPoint nav1 = new NavPoint(door.getPos().x, door.getPos().y + 2 * ColliderC.COLLIDER_RADIUS);

				Room r = t.getRoomAt(t.coordToIndex(nav1.pos.x), t.coordToIndex(nav1.pos.y));
				if (r != null) {
					r.allNavPoints.add(nav1);
					Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
							BorderC.class).navpoints.add(nav1);
				}

				NavPoint nav2 = new NavPoint(door.getPos().x, door.getPos().y - 2 * ColliderC.COLLIDER_RADIUS);
				r = t.getRoomAt(t.coordToIndex(nav2.pos.x), t.coordToIndex(nav2.pos.y));
				if (r != null){
					r.allNavPoints.add(nav2);
					Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
							BorderC.class).navpoints.add(nav2);
				}

			} else {
				NavPoint nav1 = new NavPoint(door.getPos().x + 2 * ColliderC.COLLIDER_RADIUS, door.getPos().y);

				Room r = t.getRoomAt(t.coordToIndex(nav1.pos.x), t.coordToIndex(nav1.pos.y));
				if (r != null) {
					r.allNavPoints.add(nav1);
					Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
							BorderC.class).navpoints.add(nav1);
				}

				NavPoint nav2 = new NavPoint(door.getPos().x - 2 * ColliderC.COLLIDER_RADIUS, door.getPos().y);
				r = t.getRoomAt(t.coordToIndex(nav2.pos.x), t.coordToIndex(nav2.pos.y));
				if (r != null){
					r.allNavPoints.add(nav2);
					Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
							BorderC.class).navpoints.add(nav2);
				}
			}
		}

	}

	public void addNavPoint(NavPoint nav) {
		allNavPoints.add(nav);
	}

	public void removeNavPoint(NavPoint nav) {
		if (allNavPoints.contains(nav))
			allNavPoints.remove(nav);
	}

	public void addTile(Tile tile) {
		this.tiles.add(tile);
	}

	@Deprecated
	public boolean merge(Room room) {
		if (this == room || room == null)
			return false;
		for (Tile t : room.getTiles()) {
			t.setRoom(this);
		}
		room.getTiles().clear();
		return true;
	}

	public List<Tile> getTiles() {
		return this.tiles;
	}

	public void addEntity(int entityId) {
		this.entitiesInRoom.add(entityId);
	}

	public void addNavPoints(int entityId) {
		ColliderC col = Game.getCurrentGame().getEntityManager().getComponent(entityId, ColliderC.class);
		for (NavPoint nav : col.getAllNavPoints()) {
			nav.setRoom(this);
			addNavPoint(nav);
		}
	}

	public void removeEntity(int entityId) {
		if (this.entitiesInRoom.contains(entityId)) {
			this.entitiesInRoom.remove(entityId);
		}
	}
}
