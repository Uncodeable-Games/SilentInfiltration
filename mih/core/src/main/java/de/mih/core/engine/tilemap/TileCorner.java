package de.mih.core.engine.tilemap;

import java.util.HashMap;

import javax.swing.text.Position;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

public class TileCorner {

	public HashMap<Direction, Tile> adjacentTiles = new HashMap<Direction, Tile>();
	public HashMap<Direction, TileBorder> adjacentBorders = new HashMap<Direction, TileBorder>();
	
	public boolean checked = false;

	public HashMap<Direction, Tile> getAdjacentTiles() {
		return adjacentTiles;
	}

	public HashMap<Direction, TileBorder> getAdjacentBorders() {
		return adjacentBorders;
	}

	public void addTile(Tile tile, Direction dir) {
		adjacentTiles.put(dir, tile);
	}

	public void addBorder(TileBorder border, Direction dir) {
		adjacentBorders.put(dir, border);
	}
	
	public Vector2 getPos(){
		Direction dir = (Direction) adjacentTiles.keySet().toArray()[0];
		Tile tile = adjacentTiles.get(dir);
		Vector2 pos = new Vector2();
		switch(dir){
		case N:{
			pos.x = tile.center.x+tile.getTilemap().getTILESIZE()/2f;
			pos.y = tile.center.y+tile.getTilemap().getTILESIZE()/2f;
			break;
		}
		case E:{
			pos.x = tile.center.x-tile.getTilemap().getTILESIZE()/2f;
			pos.y = tile.center.y+tile.getTilemap().getTILESIZE()/2f;
			break;
		}
		case S:{
			pos.x = tile.center.x-tile.getTilemap().getTILESIZE()/2f;
			pos.y = tile.center.y-tile.getTilemap().getTILESIZE()/2f;
			break;
		}
		case W:{
			pos.x = tile.center.x+tile.getTilemap().getTILESIZE()/2f;
			pos.y = tile.center.y-tile.getTilemap().getTILESIZE()/2f;
			break;
		}
		}
		
		return pos;
		
	}

	public void removeTile(Tile tile) {
		for (Direction dir : adjacentTiles.keySet()) {
			if (adjacentTiles.get(dir) == tile) {
				adjacentTiles.remove(dir);
			}
		}
	}

	public void removeBorder(TileBorder border) {
		for (Direction dir : adjacentBorders.keySet()) {
			if (adjacentBorders.get(dir) == border) {
				adjacentBorders.remove(dir);
			}
		}
	}
	
}
