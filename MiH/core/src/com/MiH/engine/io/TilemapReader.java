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
import com.MiH.engine.exceptions.ComponentNotFoundEx;
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
		System.out.println("t");
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document dom = null;
		try {
			dom = db.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

		try {
			readTiles(doc.getDocumentElement().getElementsByTagName("tiles").item(0), map);
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
		return map;
	}

	private boolean isParsable(Document dom) {
		return true;
	}

	private int readGeneral(Node tilemap) {
		Element dimensions = (Element) tilemap;
		String id = dimensions.getAttribute("id");
		String sheight = dimensions.getElementsByTagName("height").item(0).getTextContent();
		String swidth = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int length = Integer.parseInt(sheight);
		int width = Integer.parseInt(swidth);

		int e = entityM.createEntity();
		entityM.addComponent(e, new PositionC(new Vector3()), new TilemapC(length, width), new Visual(rs.floor, rs));
		try {
			entityM.getComponent(e, Visual.class).pos.y = -.5f;
			entityM.getComponent(e, Visual.class).scale.x = length;
			entityM.getComponent(e, Visual.class).scale.y = 0.1f;
			entityM.getComponent(e, Visual.class).scale.z = width + 1;
		} catch (ComponentNotFoundEx e1) {
			e1.printStackTrace();
		}
		return e;
	}

	int e_temp = -1;
	int x_temp, z_temp;

	private void readTiles(Node tilesNode, int e_tilemap) throws ComponentNotFoundEx {
		NodeList tiles = tilesNode.getChildNodes();
		for (int i = 0; i < tiles.getLength(); i++) {
			if (tiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
				e_temp = entityM.createEntity();
				entityM.addComponent(e_temp, new PositionC(new Vector3()), new NodeC());

				entityM.getComponent(e_temp, NodeC.class).map = entityM.getComponent(e_tilemap, TilemapC.class);
				NodeList childs = tiles.item(i).getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					switch (n.getNodeName()) {
					case "x":
						x_temp = Integer
								.parseInt(n.getTextContent());
						break;
					case "y":
						z_temp = Integer
								.parseInt(n.getTextContent());
						break;
					case "collider":
						entityM.getComponent(e_temp, NodeC.class).blocked = n.getTextContent().equals("full");
						if (entityM.getComponent(e_temp, NodeC.class).blocked) {
							entityM.addComponent(e_temp, new Visual(rs.box, rs));
						}
						break;
					}

					entityM.getComponent(e_tilemap, TilemapC.class).setTileAt((int) entityM.getComponent(e_temp, PositionC.class).position.x,
							(int) entityM.getComponent(e_temp, PositionC.class).position.z, e_temp);
				}
				
				entityM.getComponent(e_temp, PositionC.class).position.x = x_temp-(entityM.getComponent(e_tilemap, TilemapC.class).length/2f)+0.5f;
				entityM.getComponent(e_temp, PositionC.class).position.z = z_temp-(entityM.getComponent(e_tilemap, TilemapC.class).width/2f);	
			}
		}
	}
}
