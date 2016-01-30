package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;

public class DoorPath extends BasePath<TileBorder> {

	public static DoorPath NOPATH = new DoorPath();

	private static final long serialVersionUID = -2929333627020171326L;

	float dist = Float.MAX_VALUE;
	public NavPoint start;
	public NavPoint end;

	@Override
	public ArrayList<TileBorder> getNeighbours(TileBorder object) {
		return new ArrayList<TileBorder>(
				Game.getCurrentGame().getNavigationManager().getDoorNeighbours(object).keySet());
	}

	@Override
	public float getDistance(TileBorder start, TileBorder end) {
		return Game.getCurrentGame().getNavigationManager().getDoorNeighbours(start).get(end);
	}

	@Override
	public Vector2 getPos(TileBorder object) {
		return object.getPos();
	}

	@Override
	public DoorPath getNoPath() {
		return NOPATH;
	}

	/**
	 * converts this path over doors to a path usable by orders.
	 * 
	 * @param last
	 *            The last NavPoint the unit should walk to.
	 * @return
	 */
	public Path convertToPath(NavPoint last) {
		Path path = new Path();
		path.add(start);
		Room curroom = start.getRoom();
		for (TileBorder door : this) {
			NavPoint nav1 = Game.getCurrentGame().getNavigationManager().getDoorNavPointByRoom(door, curroom);
			NavPoint nav2 = Game.getCurrentGame().getNavigationManager().getDoorNavPointbyPartner(door, nav1);
			path.add(nav1);
			path.add(nav2);
			curroom = nav2.getRoom();
		}
		path.add(end);
		path.add(last);
		return path;
	}
}
