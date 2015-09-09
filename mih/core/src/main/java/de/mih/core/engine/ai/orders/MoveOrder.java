package de.mih.core.engine.ai.orders;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.game.components.TilemapC;

public class MoveOrder extends BaseOrder {
	
	public Map<Integer,Integer> path;
	public TilemapC tilemap;
	
	public int end;
	
	public MoveOrder(Map<Integer,Integer> p, int e, TilemapC map) {
		path = p;
		end = e;
		tilemap = map;
	}
}
