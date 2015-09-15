package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.tilemap.Tile.Direction;

public class Tilemap {
	
	float TILESIZE;
	

	//Vector2[][] tileCorners;
	String name;
	public List<Room> rooms = new ArrayList<>();
	Tile[][] tilemap;
	//TileBorder[][] borders;
	private List<TileBorder> borders = new ArrayList<>();
	private int length;

	private int width;
	
	public Tilemap(int length, int width, float tilesize)
	{
		this.setLength(length);
		this.setWidth(width);
		this.TILESIZE = tilesize;
		
		this.tilemap = new Tile[width][length];
		this.createTilemap();
		//this.calculateRooms();
	}
	
	public Tile getTileAt(int x, int y)
	{
		if(x >= 0 && x < width && y >= 0 && y < length)
		{
			return tilemap[x][y];
		}
		return null;
	}
	
	public int coordToIndex_x(float x)
	{
		return Math.round(x/TILESIZE );
	}
	
	public int coordToIndex_z(float z)
	{
		return Math.round(z/TILESIZE );
	}
	
	
	private void createTilemap()
	{
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getLength(); y++)
			{
				Tile tmp = new Tile(TILESIZE * (float)x + TILESIZE/2f, 0, TILESIZE * (float)y + TILESIZE/2f)  ;
				tmp.setX(x);
				tmp.setY(y);
				for(Direction direction : new Direction[]{ Direction.E, Direction.N})
				{
					Tile neighbour = null;
					Vector3 borderCenterOffset = new Vector3();
					float angle = 0f;
					if (direction == Direction.E)
					{
						borderCenterOffset.x += TILESIZE/2f;
						angle = 90f;
						if(x > 0)
							neighbour = tilemap[x-1][y];
					}
					/*else if (direction == Direction.W)
					{
						borderCenterOffset.x += TILESIZE/2f;
						if(x < width)
							neighbour = tilemap[x+1][y];
					}*/
					else if (direction == Direction.N)
					{
						borderCenterOffset.z += TILESIZE/2f;
						if(y > 0)
							neighbour = tilemap[x][y-1];
					}
					/*else if (direction == Direction.S)
					{
						borderCenterOffset.y += TILESIZE/2f;
						if(y < length)
							neighbour = tilemap[x][y+1];
					}*/
					
//					if(!tmp.hasBorder(direction))
//					{
						Vector3 borderCenter = tmp.center.cpy();
						borderCenter.sub(borderCenterOffset);
						
						TileBorder border = new TileBorder(borderCenter);
						border.angle = angle;
						border.setAdjacent(tmp);
						if(neighbour != null)
						{
							border.setAdjacent(neighbour);
							neighbour.setBorder(direction.getOppositeDirection(),border);
						}
						this.borders.add(border);
						tmp.setBorder(direction, border);
//					}
				}
				tilemap[x][y] = tmp;
			}
		}
	}
	
	public void calculateRooms()
	{
		for(Room r : rooms)
		{
			for(Tile t : r.getTiles())
				t.setRoom(null);
			r.tiles.clear();
		}
		this.rooms.clear();
//		Room test = new Room();
//		for(int x = 0; x < 2; x++)
//		{
//			for(int y = 0; y < 3; y++)
//			{
//				test.addTile(tilemap[x][y]);
//				tilemap[x][y].setRoom(test);
//			}
//		}
//		this.rooms.add(test);
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getLength(); y++)
			{
				Tile tmp = tilemap[x][y];
				for(Direction direction : Direction.values())
				{						
					Room room = null;
					if(tmp.hasNeighbour(direction) && !tmp.getBorder(direction).hasColliderEntity())
					{
						Tile neighbour = tmp.getNeighour(direction);
						if(tmp.hasRoom() && neighbour.hasRoom())
						{
							room = neighbour.getRoom();
							if(tmp.getRoom().merge(neighbour.getRoom()))
							{
								this.rooms.remove(room);
								room = null;
								room = tmp.getRoom();
							}
						}
						else if(tmp.hasRoom())
						{
							room = tmp.getRoom();
							room.addTile(neighbour);
							neighbour.setRoom(room);
						}
						else if(neighbour.hasRoom())
						{
							room = neighbour.getRoom();
							room.addTile(tmp);
							tmp.setRoom(room);
						}
						else
						{
							room = new Room();
							room.addTile(neighbour);
							neighbour.setRoom(room);
							room.addTile(tmp);
							tmp.setRoom(room);
							if(!this.rooms.contains(room))
								this.rooms.add(room);
						}
					}
					else if(!tmp.hasRoom())
					{
						room = new Room();
						room.addTile(tmp);
						tmp.setRoom(room);
					
					}
					if(room != null  && !this.rooms.contains(room))
					{
						this.rooms.add(room);
					}
				}
			}
		}
		System.out.println(rooms.size());
		for(Room room : this.rooms)
		{
			//System.out.println(room.tiles.size());
			
			room.calculateCenter();
			System.out.println(room.getCenterPoint());
			
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
