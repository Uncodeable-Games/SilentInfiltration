package com.MiH.engine.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DomainCombiner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document dom = null;
		try {
			dom = db.parse(file);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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

		int map = entityM.createEntity();
		entityM.addComponent(map, new PositionC(new Vector3()), new Visual(rs.floor, rs), new TilemapC(length,width));
		try {
			entityM.getComponent(map, Visual.class).pos.y = -.5f;
			entityM.getComponent(map, Visual.class).scale.x = length;
			entityM.getComponent(map, Visual.class).scale.z = width+1;
		} catch (ComponentNotFoundEx e) {e.printStackTrace();}
		
		return map;
	}

	int e_temp,x_temp,z_temp;
	
	private void readTiles(Node tilesNode, int map) throws ComponentNotFoundEx {
		NodeList tiles = tilesNode.getChildNodes();
		for (int i = 0; i < tiles.getLength(); i++) {
			if (tiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
				e_temp = entityM.createEntity();
				entityM.addComponent(e_temp, new PositionC(new Vector3()), new NodeC());
				entityM.getComponent(e_temp, NodeC.class).map = entityM.getComponent(map, TilemapC.class);
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
					case "collider":
						entityM.getComponent(e_temp, NodeC.class).blocked = n.getTextContent().equals("full");
						if (entityM.getComponent(e_temp, NodeC.class).blocked) {
							entityM.addComponent(e_temp, new Visual(rs.box, rs));
						}
						break;
					}

				}
				entityM.getComponent(e_temp, PositionC.class).position.x = x_temp-(entityM.getComponent(map, TilemapC.class).length / 2f)+.5f;
				entityM.getComponent(e_temp, PositionC.class).position.z = z_temp-(entityM.getComponent(map, TilemapC.class).length / 2f);
				
				entityM.getComponent(map, TilemapC.class).setTileAt(x_temp, z_temp, e_temp);
			}
		}
	}

}
