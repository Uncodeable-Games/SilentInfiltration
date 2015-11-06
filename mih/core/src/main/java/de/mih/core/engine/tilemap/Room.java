package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.collision.Segment;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class Room {

	// Polygon form;
	public List<Integer> entitiesInRoom = new ArrayList<Integer>();
	public List<NavPoint> allNavPoints = new ArrayList<NavPoint>();
	Vector3 centerPoint;
	@Deprecated
	List<Tile> tiles = new ArrayList<>();
	public Visual visual;

	public Room() {
		this.visual = new Visual(AdvancedAssetManager.getInstance().getModelByName("center"));
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
	
	public void calculateTileBorderNeighbours(){
		for (Tile tile :tiles){
			for (TileBorder tileborder: tile.borders.values()){
				for (int i=0;i<tileborder.neighbours.length;i++){
					tileborder.neighbours[i] = null;
				}
			}
		}
		
		for (Tile tile : tiles){
			for (Direction dir: tile.borders.keySet()){
				if (!tile.hasBorder(dir)) continue;
				if (dir == Direction.E || dir == Direction.W){
					Tile neigh = tile.getNeighour(dir);
					if (neigh.getRoom() !=this) continue;
					
				} else {
					Tile neigh = tile.getNeighour(dir);
					if (neigh.getRoom() !=this) continue;
				}
			}
		}
	}

	public void addNavPoint(NavPoint nav){
		allNavPoints.add(nav);
	}
	public void removeNavPoint(NavPoint nav){
		if (allNavPoints.contains(nav)) allNavPoints.remove(nav);
	}
	
	private boolean isLeft(Vector3 v1, Vector3 comparand) {
		if (v1.x <= comparand.x)
			return true;
		return false;
	}

	private boolean isRight(Vector3 v1, Vector3 comparand) {
		if (v1.x >= comparand.x)
			return true;
		return false;
	}

	private boolean isOver(Vector3 v1, Vector3 comparand) {
		if (v1.z >= comparand.z)
			return true;
		return false;
	}

	private boolean isUnder(Vector3 v1, Vector3 comparand) {
		if (v1.z <= comparand.z)
			return true;
		return false;
	}

	public void addTile(Tile tile) {
		this.tiles.add(tile);
	}

	public Vector3 getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Vector3 centerPoint) {
		this.centerPoint = centerPoint;
	}

	public boolean render() {
		if (centerPoint == null)
			return false;
		// System.out.println("render room");
		visual.model.transform.setToTranslation(centerPoint.x + visual.pos.x, centerPoint.y + visual.pos.y,
				centerPoint.z + visual.pos.z);
		// visual.model.transform.rotate(0f, 1f, 0f, visual.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
		return true;
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
	
	
	//TODO: COMPLETE
	public void addNavPoints(int entityId){
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityId, ColliderC.class)){
			//if ()
			
			ColliderC col = Game.getCurrentGame().getEntityManager().getComponent(entityId, ColliderC.class);
			for (NavPoint nav:col.navpoints){
				nav.setRoom(this);
			}
		}
	}

	public void removeEntity(int entityId) {
		if (this.entitiesInRoom.contains(entityId)) {
			this.entitiesInRoom.remove(entityId);
		}
	}
}
