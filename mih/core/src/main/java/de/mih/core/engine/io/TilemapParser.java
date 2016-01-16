package de.mih.core.engine.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.UnittypeC;

import com.badlogic.gdx.Gdx;

public class TilemapParser {

	final static String TILE_TAG = "tile";
	final static String DIMENSIONS_TAG = "tilemap";
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	

	EntityManager entityManager;
	BlueprintManager blueprintManager;

	public TilemapParser(BlueprintManager blueprintManager, EntityManager entityManager) 
	{
		this.entityManager = entityManager;
		this.blueprintManager  = blueprintManager;
	}
	


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
		String name = dimensions.getAttribute("name");
		String slength = dimensions.getElementsByTagName("length").item(0).getTextContent();
		String swidth = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int length = Integer.parseInt(slength);
		int width = Integer.parseInt(swidth);
		float TILE_SIZE = Float.parseFloat(dimensions.getElementsByTagName("tilesize").item(0).getTextContent());
		
		Tilemap map = new Tilemap(length, width, TILE_SIZE, entityManager);
		map.setName(name);
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

				String colliderType = null;
				int entityCollider = -1;
				if(child.hasAttributes())
				{
					colliderType = child.getAttributes().getNamedItem("collider").getNodeValue();
					entityCollider = this.blueprintManager.createEntityFromBlueprint(colliderType);
					
				}
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

						Tile tmp = map.getTileAt(x, y);
						if(first == null)
							first = tmp;
						else
							second = tmp;
						
						if(entityCollider > -1)
						{
							tmp.getBorder(direction).setColliderEntity(entityCollider);
						}
					}
					
				}
				if(entityCollider > -1 && first != null && second != null)
				{
					for(Direction d : Direction.values())
					{
						if(first.getBorder(d).getAdjacentTile(first) == second)
						{
							first.getBorder(d).setColliderEntity(entityCollider);
							break;
							
						}
					}
				}
			}
		}
	}
	
	
	public boolean writeTilemap(String path, Tilemap map) throws ParserConfigurationException, TransformerException
	{
		//File file = Gdx.files.internal(path).file();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document doc = builder.newDocument();
		Element root = doc.createElement("tilemap");
		root.setAttribute("name", map.getName());
		doc.appendChild(root);
		
		Element length = doc.createElement("length");
		length.appendChild(doc.createTextNode(Integer.toString(map.getLength())));
		root.appendChild(length);
		
		Element width = doc.createElement("width");
		width.appendChild(doc.createTextNode(Integer.toString(map.getWidth())));
		root.appendChild(width);
		
		Element tilesize = doc.createElement("tilesize");
		tilesize.appendChild(doc.createTextNode(Float.toString(map.getTILESIZE())));
		root.appendChild(tilesize);
		
		Element borders = doc.createElement("borders");
		root.appendChild(borders);
		
		List<TileBorder> tileBorders = map.getBorders();
		for(TileBorder tileBorder : tileBorders)
		{
			if(!tileBorder.hasColliderEntity())
			{
				continue;
			}
			Element currentBorder = doc.createElement("border");
			String collider = "";
			if(entityManager.hasComponent(tileBorder.getColliderEntity(), UnittypeC.class))
			{
				collider = entityManager.getComponent(tileBorder.getColliderEntity(), UnittypeC.class).unitType;
			}
			currentBorder.setAttribute("collider", collider);
			
			List<Tile> adjacentTiles = tileBorder.getTiles();
			for(Tile tmp : adjacentTiles)
			{
				Element tile = doc.createElement("tile");
				tile.setAttribute("x", Integer.toString(tmp.getX()));
				tile.setAttribute("y", Integer.toString(tmp.getY()));
				tile.setAttribute("direction", tmp.getDirection(tileBorder).toString());

				currentBorder.appendChild(tile);
			}
			borders.appendChild(currentBorder);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));

		// Output to console for testing
		 //StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		
		return true;
	}

}
