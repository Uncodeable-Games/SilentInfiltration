package de.mih.core.engine.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.BorderColliderFactory;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.tilemap.borders.Door;
import de.mih.core.game.tilemap.borders.Wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class TilemapReader {

	final static String TILE_TAG = "tile";
	final static String DIMENSIONS_TAG = "tilemap";
	
	BorderColliderFactory borderColliderFactory;
	RenderSystem rs;
	EntityManager entityM;
	//Tilemap tilemap;

	public TilemapReader(RenderSystem rs, EntityManager em, BorderColliderFactory borderColliderFactory) {
		this.rs = rs;
		this.entityM = em;
		this.borderColliderFactory = borderColliderFactory;
	}

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public Tilemap readMap(String path) {

		File file = Gdx.files.internal(path).file();

		if (!file.exists()) {
			return null;
		}
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document dom = null;
		try {
			dom = db.parse(file);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		if (dom == null)
			return null;
		return parseMap(dom);
	}

	private Tilemap parseMap(Document doc) {
		if (!isParsable(doc)) {
			return null;
		}
		doc.getDocumentElement().normalize();

		Tilemap map = readGeneral(doc.getDocumentElement());

		//readTiles(doc.getDocumentElement().getElementsByTagName("tiles").item(0), map);
		//NodeList borders = doc.getDocumentElement().getElementsByTagName("borders");
		
		readTileBorders(map, doc.getDocumentElement().getElementsByTagName("borders"));
		return map;
	}

	private boolean isParsable(Document dom) {
		return true;
	}

	int e_temp;

	@SuppressWarnings("unused")
	private Tilemap readGeneral(Node tilemap) {
		Element dimensions = (Element) tilemap;
		String id = dimensions.getAttribute("id");
		String slength = dimensions.getElementsByTagName("length").item(0).getTextContent();
		String swidth = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int length = Integer.parseInt(slength);
		int width = Integer.parseInt(swidth);
		float TILE_SIZE = Float.parseFloat(dimensions.getElementsByTagName("tilesize").item(0).getTextContent());
		
		Tilemap map = new Tilemap(length, width, TILE_SIZE);
		//int map = entityM.createEntity();
		
		/*entityM.addComponent(map, new PositionC(new Vector3()), new VisualC("floor", rs), new TilemapC(length, width));
		
		entityM.getComponent(map, TilemapC.class).TILE_SIZE = Float.parseFloat(dimensions.getElementsByTagName("tilesize").item(0).getTextContent());
		
		entityM.getComponent(map, VisualC.class).setScale(length * entityM.getComponent(map, TilemapC.class).TILE_SIZE,
				1f, width * entityM.getComponent(map, TilemapC.class).TILE_SIZE);
		

		for (int i = 0; i < length; i++) {
			for (int k = 0; k < width; k++) {
				e_temp = entityM.createEntity();
				entityM.addComponent(e_temp, new PositionC(new Vector3()), new NodeC());

				entityM.getComponent(e_temp, NodeC.class).map = entityM.getComponent(map, TilemapC.class);

				entityM.getComponent(e_temp,
						PositionC.class).position.x = (2 * i - entityM.getComponent(map, TilemapC.class).length + 1)
								* entityM.getComponent(map, TilemapC.class).TILE_SIZE / 2f;
				entityM.getComponent(e_temp,
						PositionC.class).position.z = (2 * k - entityM.getComponent(map, TilemapC.class).width + 1)
								* entityM.getComponent(map, TilemapC.class).TILE_SIZE / 2f;

				entityM.getComponent(map, TilemapC.class).setTileAt(i, k, e_temp);

			}
		}*/
		return map;
	}

	
	private void readTileBorders(Tilemap map, NodeList borders)
	{
		NodeList childs = borders.item(0).getChildNodes();

		for(int i = 0; i < childs.getLength(); i++)
		{
			Node child = childs.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE)
			{
				System.out.println(child.getNodeName());
				BorderCollider collider = null;
				if(child.hasAttributes())
				{
					String colliderType = child.getAttributes().getNamedItem("collider").getNodeValue();
					collider = borderColliderFactory.colliderForName(colliderType);
				}
				System.out.println(collider);
				//PARSE collider to class! maybe with an register
				
				NodeList adjacentTiles = child.getChildNodes();
				Tile first = null, second = null;

				for(int j = 0; j< adjacentTiles.getLength(); j++)
				{
					Node tile = adjacentTiles.item(j);
					
					
					if(tile.getNodeType() == Node.ELEMENT_NODE && tile.hasAttributes())
					{
						int x = Integer.parseInt(tile.getAttributes().getNamedItem("x").getNodeValue());
						int y = Integer.parseInt(tile.getAttributes().getNamedItem("y").getNodeValue());
						
						Direction direction = Direction.parseDirection(tile.getAttributes().getNamedItem("direction").getNodeValue());
						System.out.println("adding border: " + x + ", " + y);
						Tile tmp = map.getTileAt(x, y);
						if(first == null)
							first = tmp;
						else
							second = tmp;
						if(collider != null)
						{
							collider.setPosition(tmp.getBorder(direction));
							tmp.addBorderCollider(collider, direction);
						}
					}
					
				}
				if(collider != null && first != null && second != null)
				{
					for(Direction d : Direction.values())
					{
						if(first.getBorder(d).getAdjacentTile(first) == second)
						{
							first.getBorder(d).setBorderCollider(collider);
							collider.setPosition(first.getBorder(d));
							break;
							
						}
					}
				}
			}
		}
		
	}
	private void readTiles(Node tilesNode, int map) {	
//		int x_temp = 0, z_temp  = 0;
//		String model = null;
//		TilemapC tilemap = null;
//		NodeC temp_node = null;
//		int angle = 0;
//		
//		tilemap = entityM.getComponent(map, TilemapC.class);
//		NodeList tiles = tilesNode.getChildNodes();
//		for (int i = 0; i < tiles.getLength(); i++) {
//			if (tiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
//				NodeList childs = tiles.item(i).getChildNodes();
//				for (int j = 0; j < childs.getLength(); j++) {
//					Node n = childs.item(j);
//					switch (n.getNodeName()) {
//					case "x":
//						x_temp = Integer.parseInt(n.getTextContent());
//						break;
//					case "y":
//						z_temp = Integer.parseInt(n.getTextContent());
//						break;
//					case "angle":
//						angle = Integer.parseInt(n.getTextContent());
//						break;
//					case "model":
//						model = n.getTextContent();
//						break;
//					}
//				}
//				e_temp = tilemap.getTileAt(x_temp, z_temp);
//				entityM.getComponent(e_temp, PositionC.class).angle = angle;
//				//entityM.getComponent(e_temp, PositionC.class).position.z = 1f;
//
//				entityM.getComponent(e_temp, NodeC.class).blocked = true;
//				entityM.addComponent(e_temp, new VisualC(model, rs));
//				VisualC vis = entityM.getComponent(e_temp, VisualC.class);
//				vis.visual.pos.y = 0f;//tilemap.TILE_SIZE / 2f;
//				vis.setScale(0.25f,0.5f,0.5f);//tilemap.TILE_SIZE, tilemap.TILE_SIZE, tilemap.TILE_SIZE);
//				
//			}
//
//		}
//		for (int i = 0; i < tilemap.length; i++) {
//			for (int j = 0; j < tilemap.width; j++) {
//				temp_node = entityM.getComponent(tilemap.getTileAt(i, j), NodeC.class);
//				if (i > 0)
//					temp_node.neighbours.add(tilemap.getTileAt(i - 1, j));
//				if (j > 0)
//					temp_node.neighbours.add(tilemap.getTileAt(i, j - 1));
//				if (i < tilemap.length - 1)
//					temp_node.neighbours.add(tilemap.getTileAt(i + 1, j));
//				if (j < tilemap.width - 1)
//					temp_node.neighbours.add(tilemap.getTileAt(i, j + 1));
//
//				// Diagonal Neighbours
//				// if (i > 0 && j > 0)
//				// temp_node.neighbours.add(tilemap.getTileAt(i - 1, j - 1));
//				// if (i > 0 && j < tilemap.width - 1)
//				// temp_node.neighbours.add(tilemap.getTileAt(i - 1, j + 1));
//				// if (j > 0 && i < tilemap.length - 1)
//				// temp_node.neighbours.add(tilemap.getTileAt(i + 1, j - 1));
//				// if (i < tilemap.length - 1 && j < tilemap.width - 1)
//				// temp_node.neighbours.add(tilemap.getTileAt(i + 1, j + 1));
//			}
//		}
	}

}
