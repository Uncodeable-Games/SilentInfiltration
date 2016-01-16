package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;

import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

public class NavigationManager {
	
	Tilemap tilemap;
	
	HashMap<Room,ArrayList<NavPoint>> allNavPoints = new HashMap<Room,ArrayList<NavPoint>>();
	
	
	
	
	public NavigationManager(Tilemap map){
		this.tilemap = map;
	}
	
	public void calculateVisibility(Room room){
		for (NavPoint nav : allNavPoints.get(room)){
			nav.calculateVisibility(room);
		}
	}
	
	public void routeNavPoints(Room room){
		for (NavPoint nav : allNavPoints.get(room)){
			nav.route();
		}
	}
	
	public void addEntityNavPointsForRoom(Room room){
		for (int i : room.entitiesInRoom) {
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

			//addNavPoints(i);
		}
	}
}
