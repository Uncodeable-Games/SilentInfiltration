package de.mih.core.engine.ai.navigation.pathfinder;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.AStar;
import de.mih.core.engine.tilemap.Door;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import java.util.ArrayList;

public class Pathfinder
{

	private AStar aStar = new AStar();

	public Path getPath(Vector3 v_start, Vector3 v_end)
	{
		Room startroom = Game.getCurrentGame().getTilemap().getRoomAt(v_start.x, v_start.z);
		Room endroom   = Game.getCurrentGame().getTilemap().getRoomAt(v_end.x, v_end.z);

		if (startroom == null || endroom == null)
		{
			System.out.println("startroom or endroom are null");
			return Path.NOPATH;
		}

		// Create NavPoints where the Unit starts and where it wants to go and
		// calculate the Visibility (They need to be added to the Room NavPoints
		// so they can see each other, but needed to be removed after 'cause
		// they should be ignored by other checks)
		NavPoint first = new NavPoint(v_start.x, v_start.z);
		NavPoint last  = new NavPoint(v_end.x, v_end.z);
		Game.getCurrentGame().getNavigationManager().getNavPoints(startroom).add(first);
		Game.getCurrentGame().getNavigationManager().getNavPoints(endroom).add(last);
		initNavPoint(first);
		initNavPoint(last);
		Game.getCurrentGame().getNavigationManager().getNavPoints(startroom).remove(first);
		Game.getCurrentGame().getNavigationManager().getNavPoints(endroom).remove(last);
		
		// If target is in line of sight return direct Path;

		Path tmp = new Path();
		if (first.isVisibleBy(last))
		{
			tmp.add(first);
			tmp.add(last);
			return tmp;
		}
		tmp.addAll(aStar.generatePath(first, last));
		return tmp;
	}

	private void initNavPoint(NavPoint navPoint){
		Room room = Game.getCurrentGame().getTilemap().getRoomAt(navPoint.getPos().x, navPoint.getPos().y);

		ArrayList<ColliderC> cArrayList = new ArrayList<>();

		//TODO: Only add doors "standing" in
		//Add Doors to exclude from checking. So the PF will find a Path even when the entity is standing inside a door
		for (Door door : room.allDoors){
			cArrayList.add(Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),ColliderC.class));
		}

		navPoint.calculateVisibility(cArrayList);
	}
}
