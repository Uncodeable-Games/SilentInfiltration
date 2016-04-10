package de.mih.core.engine.io;

import com.badlogic.gdx.Gdx;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.UnittypeC;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TilemapParser
{

	final static String TILE_TAG       = "tile";
	final static String DIMENSIONS_TAG = "tilemap";
	DocumentBuilderFactory factory            = DocumentBuilderFactory.newInstance();
	TransformerFactory     transformerFactory = TransformerFactory.newInstance();

	EntityManager    entityManager;
	BlueprintManager blueprintManager;

	public TilemapParser(BlueprintManager blueprintManager, EntityManager entityManager)
	{
		this.entityManager = entityManager;
		this.blueprintManager = blueprintManager;
	}

	public Tilemap readMap(String path)
	{

		File file = Gdx.files.internal(path).file();

		if (!file.exists())
		{
			return null;
		}
		DocumentBuilder db = null;
		try
		{
			db = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		Document dom = null;
		try
		{
			dom = db.parse(file);
		} catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}
		if (dom == null)
			return null;
		return parseMap(dom);
	}

	private Tilemap parseMap(Document doc)
	{
		if (!isParsable(doc))
		{
			return null;
		}
		doc.getDocumentElement().normalize();

		Tilemap map = readMapInfo(doc.getDocumentElement());

		readTileBorders(map, doc.getDocumentElement().getElementsByTagName("borders"));
		return map;
	}

	private boolean isParsable(Document dom)
	{
		return true;
	}

	private Tilemap readMapInfo(Node tilemap)
	{
		Element dimensions = (Element) tilemap;
		String  name       = dimensions.getAttribute("name");
		String  slength    = dimensions.getElementsByTagName("length").item(0).getTextContent();
		String  swidth     = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int     length     = Integer.parseInt(slength);
		int     width      = Integer.parseInt(swidth);
		float   TILE_SIZE  = Float.parseFloat(dimensions.getElementsByTagName("tilesize").item(0).getTextContent());

		Tilemap map = new Tilemap(length, width, TILE_SIZE, entityManager);
		map.setName(name);
		return map;
	}

	private void readTileBorders(Tilemap map, NodeList borders)
	{
		NodeList childs = borders.item(0).getChildNodes();

		for (int i = 0; i < childs.getLength(); i++)
		{
			Node child = childs.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
			{

				String colliderType = null;
				if (child.hasAttributes())
				{
					colliderType = child.getAttributes().getNamedItem("collider").getNodeValue();
				}
				// PARSE collider to class! maybe with an register

				NodeList adjacentTiles = child.getChildNodes();
				Tile     first         = null, second = null;

				int entityCollider = -1;
				for (int j = 0; j < adjacentTiles.getLength(); j++)
				{
					Node tile = adjacentTiles.item(j);

					if (tile.getNodeType() == Node.ELEMENT_NODE && tile.hasAttributes())
					{
						int x = Integer.parseInt(tile.getAttributes().getNamedItem("x").getNodeValue());
						int y = Integer.parseInt(tile.getAttributes().getNamedItem("y").getNodeValue());
						Direction direction = Direction
								.parseDirection(tile.getAttributes().getNamedItem("direction").getNodeValue());

						Tile tmp = map.getTileAt(x, y);
						if (first == null)
							first = tmp;
						else
							second = tmp;

						switch (colliderType)
						{
							case "wall":
							{
								tmp.getBorder(direction).setToWall();
								break;
							}
							case "door":
							{
								tmp.getBorder(direction).setToDoor();
								break;
							}
						}
						entityCollider = tmp.getBorder(direction).getColliderEntity();
					}
				}

//				if (entityCollider > -1 && first != null && second != null) {
//					for (Direction d : Direction.values()) {
//						if (first.getBorder(d).getAdjacentTile(first) == second) {
//							switch (colliderType) {
//							case "wall": {
//								first.getBorder(d).setToWall();
//								break;
//							}
//							case "door": {
//								first.getBorder(d).setToDoor();
//								break;
//							}
//							}
//							first.getBorder(d).setColliderEntity(entityCollider);
//							break;
//						}
//					}
//				}

			}
		}
	}

	public void writeTilemap(String path, Tilemap map) throws ParserConfigurationException, TransformerException
	{
		Document doc = factory.newDocumentBuilder().newDocument();

		Element root = createElement(doc, "tilemap", map.getName());
		doc.appendChild(root);

		root.appendChild(createElement(doc, "length", Integer.toString(map.getLength())));

		root.appendChild(createElement(doc, "width", Integer.toString(map.getWidth())));

		root.appendChild(createElement(doc, "tilesize", Float.toString(map.getTILESIZE())));

		Element borders = doc.createElement("borders");
		root.appendChild(borders);

		List<TileBorder> tileBorders = map.getBorders();
		for (TileBorder tileBorder : tileBorders)
		{
			if (!tileBorder.hasCollider())
			{
				continue;
			}
			Element currentBorder = doc.createElement("border");
			String  collider      = "";
			if (entityManager.hasComponent(tileBorder.getColliderEntity(), UnittypeC.class))
			{
				collider = entityManager.getComponent(tileBorder.getColliderEntity(), UnittypeC.class).unitType;
			}
			currentBorder.setAttribute("collider", collider);

			List<Tile> adjacentTiles = tileBorder.getTiles();
			for (Tile tmp : adjacentTiles)
			{
				Element tile = doc.createElement("tile");
				tile.setAttribute("x", Integer.toString(tmp.getX()));
				tile.setAttribute("y", Integer.toString(tmp.getY()));
				tile.setAttribute("direction", tmp.getDirection(tileBorder).toString());

				currentBorder.appendChild(tile);
			}
			borders.appendChild(currentBorder);
		}

		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		DOMSource    source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));

		transformer.transform(source, result);
	}

	private Element createElement(Document doc, String elementName, String value)
	{
		Element element = doc.createElement(elementName);
		element.appendChild(doc.createTextNode(value));
		return element;
	}
}
