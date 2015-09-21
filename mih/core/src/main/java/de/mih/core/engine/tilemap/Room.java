package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;

public class Room {
	Vector3 centerPoint;
	Tile centerTile, leftTop, rightBottom, leftBottom, rightTop;
	List<Tile> tiles = new ArrayList<>();
	public Visual visual;
	
	public Room()
	{
		this.visual = new Visual(AdvancedAssetManager.getInstance().getModelByName("center"));
	}
	
	public void calculateCenter()
	{
		for(Tile tile : tiles)
		{

			if(leftTop == null || (isLeft(tile.getCenter(), leftTop.getCenter()) && isOver(tile.getCenter(), leftTop.getCenter())))
				leftTop = tile;
			if(rightTop == null || (isRight(tile.getCenter(), rightTop.getCenter()) && isOver(tile.getCenter(), rightTop.getCenter())))
				rightTop = tile;				
			if(leftBottom == null || (isLeft(tile.getCenter(), leftBottom.getCenter()) && isUnder(tile.getCenter(), leftBottom.getCenter())))
				leftBottom = tile;
			if(rightBottom == null || (isRight(tile.getCenter(), rightBottom.getCenter()) && isUnder(tile.getCenter(), rightBottom.getCenter())))
				rightBottom = tile;
		}

		Vector2 p1 = new Vector2(leftTop.ltop.x, leftTop.ltop.z);
		Vector2 p2 = new Vector2(rightBottom.rbot.x, rightBottom.rbot.z);
		
		Vector2 p3 = new Vector2(rightTop.rtop.x, rightTop.rtop.z);
		Vector2 p4 = new Vector2(leftBottom.lbot.x, leftBottom.lbot.z);
		
		Vector2 intersection = new Vector2();
		
		Intersector.intersectLines(p1, p2, p3, p4, intersection);
		
		this.centerPoint = new Vector3(intersection.x,0,intersection.y);
		System.out.println(centerPoint);
	}
	
	private boolean isLeft(Vector3 v1, Vector3 comparand)
	{
		if(v1.x <= comparand.x)
			return true;
		return false;
	}
	
	private boolean isRight(Vector3 v1, Vector3 comparand)
	{
		if(v1.x >= comparand.x)
			return true;
		return false;
	}
	private boolean isOver(Vector3 v1, Vector3 comparand)
	{
		if(v1.z >= comparand.z)
			return true;
		return false;
	}
	
	private boolean isUnder(Vector3 v1, Vector3 comparand)
	{
		if(v1.z <= comparand.z)
			return true;
		return false;
	}
	
	
	public void addTile(Tile tile)
	{
		this.tiles.add(tile);
	}

	public Vector3 getCenterPoint() {
		return centerPoint;
	}

	public void setCenterPoint(Vector3 centerPoint) {
		this.centerPoint = centerPoint;
	}

	public boolean render()
	{
		if(centerPoint == null)
			return false;
		//System.out.println("render room");
		visual.model.transform.setToTranslation(centerPoint.x + visual.pos.x, centerPoint.y + visual.pos.y,
				centerPoint.z + visual.pos.z);
		//visual.model.transform.rotate(0f, 1f, 0f, visual.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
		return true;
	}
	
	public boolean merge(Room room)
	{
		if(this == room || room == null)
			return false;
		for(Tile t : room.getTiles())
		{
			t.setRoom(this);
		}
		room.getTiles().clear();
		return true;
	}

	public List<Tile> getTiles() {
		return this.tiles;
	}
}
