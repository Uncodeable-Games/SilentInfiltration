package com.MiH.engine.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.MiH.engine.ecs.EntityManager;
import com.MiH.game.components.NodeC;
import com.MiH.game.components.PositionC;
import com.MiH.game.components.TilemapC;
import com.MiH.game.components.Visual;
import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.math.Vector3;

public class TilemapReader {

	final static String TILE_TAG = "tile";
	final static String DIMENSIONS_TAG = "tilemap";

	RenderSystem rs;
	EntityManager entityM;

	public TilemapReader(RenderSystem rs, EntityManager em) {
		this.rs = rs;
		this.entityM = em;
	}

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public int readMap(String path) {

		File file = new File(path);

		if (!file.exists()) {
			return -1;
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
			return -1;
		return parseMap(dom);
	}

	private int parseMap(Document doc) {
		if (!isParsable(doc)) {
			return -1;
		}
		doc.getDocumentElement().normalize();

		int map = readGeneral(doc.getDocumentElement());

		readTiles(doc.getDocumentElement().getElementsByTagName("tiles").item(0), map);
		return map;
	}

	private boolean isParsable(Document dom) {
		return true;
	}

	int e_temp;

	@SuppressWarnings("unused")
	private int readGeneral(Node tilemap) {
		Element dimensions = (Element) tilemap;
		String id = dimensions.getAttribute("id");
		String slength = dimensions.getElementsByTagName("length").item(0).getTextContent();
		String swidth = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int length = Integer.parseInt(slength);
		int width = Integer.parseInt(swidth);

		int map = entityM.createEntity();
		entityM.addComponent(map, new PositionC(new Vector3()), new Visual("floor", rs), new TilemapC(length, width));
		entityM.getComponent(map, Visual.class).pos.y = -.5f;
		entityM.getComponent(map, Visual.class).setScale(length * NodeC.TILE_SIZE, 1f, width * NodeC.TILE_SIZE);

		for (int i = 0; i < length; i++) {
			for (int k = 0; k < width; k++) {
				e_temp = entityM.createEntity();
				entityM.addComponent(e_temp, new PositionC(new Vector3()), new NodeC());

				entityM.getComponent(e_temp, NodeC.class).map = entityM.getComponent(map, TilemapC.class);

				entityM.getComponent(e_temp,
						PositionC.class).position.x = (2 * i - entityM.getComponent(map, TilemapC.class).length + 1)
								* NodeC.TILE_SIZE / 2f;
				entityM.getComponent(e_temp,
						PositionC.class).position.z = (2 * k - entityM.getComponent(map, TilemapC.class).width + 1)
								* NodeC.TILE_SIZE / 2f;

				entityM.getComponent(map, TilemapC.class).setTileAt(i, k, e_temp);

			}
		}
		return map;
	}

	int x_temp, z_temp;
	String model;
	TilemapC tilemap;
	NodeC temp_node;

	private void readTiles(Node tilesNode, int map) {
		tilemap = entityM.getComponent(map, TilemapC.class);
		NodeList tiles = tilesNode.getChildNodes();
		for (int i = 0; i < tiles.getLength(); i++) {
			if (tiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
				NodeList childs = tiles.item(i).getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					switch (n.getNodeName()) {
					case "x":
						x_temp = Integer.parseInt(n.getTextContent());
						break;
					case "y":
						z_temp = Integer.parseInt(n.getTextContent());
						break;
					case "model":
						model = n.getTextContent();
						break;
					}
				}
				e_temp = tilemap.getTileAt(x_temp, z_temp);
				entityM.getComponent(e_temp, NodeC.class).blocked = true;
				entityM.addComponent(e_temp, new Visual(model, rs));
				entityM.getComponent(e_temp, Visual.class).pos.y = -NodeC.TILE_SIZE /2f;
				entityM.getComponent(e_temp, Visual.class).setScale(NodeC.TILE_SIZE, NodeC.TILE_SIZE, NodeC.TILE_SIZE);
			}

		}
		for (int i = 0; i < tilemap.length; i++) {
			for (int j = 0; j < tilemap.width; j++) {
				temp_node = entityM.getComponent(tilemap.getTileAt(i, j), NodeC.class);
				if (i > 0)
					temp_node.neighbours.add(tilemap.getTileAt(i - 1, j));
				if (j > 0)
					temp_node.neighbours.add(tilemap.getTileAt(i, j - 1));
				if (i < tilemap.length - 1)
					temp_node.neighbours.add(tilemap.getTileAt(i + 1, j));
				if (j < tilemap.width - 1)
					temp_node.neighbours.add(tilemap.getTileAt(i, j + 1));

				// Diagonal Neighbours
				// if (i > 0 && j > 0)
				// temp_node.neighbours.add(tilemap.getTileAt(i - 1, j - 1));
				// if (i > 0 && j < tilemap.width - 1)
				// temp_node.neighbours.add(tilemap.getTileAt(i - 1, j + 1));
				// if (j > 0 && i < tilemap.length - 1)
				// temp_node.neighbours.add(tilemap.getTileAt(i + 1, j - 1));
				// if (i < tilemap.length - 1 && j < tilemap.width - 1)
				// temp_node.neighbours.add(tilemap.getTileAt(i + 1, j + 1));
			}
		}
	}

}
