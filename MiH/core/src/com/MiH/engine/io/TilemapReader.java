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

import com.MiH.engine.tilemap.Tile;
import com.MiH.engine.tilemap.Tilemap;
import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class TilemapReader {

	final static String TILE_TAG = "tile";
	final static String DIMENSIONS_TAG = "tilemap";

	RenderSystem rs;

	public TilemapReader(RenderSystem rs) {
		this.rs = rs;
	}

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public Tilemap readMap(String path) {

		File file = new File(path);

		if (!file.exists()) {
			return null;
		}
		System.out.println("t");
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
			return null;
		return parseMap(dom);
	}

	private Tilemap parseMap(Document doc) {
		if (!isParsable(doc)) {
			return null;
		}
		doc.getDocumentElement().normalize();

		Tilemap map = readGeneral(doc.getDocumentElement());

		readTiles(doc.getDocumentElement().getElementsByTagName("tiles").item(0), map);
		return map;
	}

	private boolean isParsable(Document dom) {
		return true;
	}

	private Tilemap readGeneral(Node tilemap) {
		Element dimensions = (Element) tilemap;
		String id = dimensions.getAttribute("id");
		String sheight = dimensions.getElementsByTagName("height").item(0).getTextContent();
		String swidth = dimensions.getElementsByTagName("width").item(0).getTextContent();
		int length = Integer.parseInt(sheight);
		int width = Integer.parseInt(swidth);

		Tilemap map = new Tilemap(width, length, rs);
		return map;
	}

	private void readTiles(Node tilesNode, Tilemap map) {
		NodeList tiles = tilesNode.getChildNodes();
		for (int i = 0; i < tiles.getLength(); i++) {
			if (tiles.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Tile tmp = new Tile();
				tmp.map = map;
				NodeList childs = tiles.item(i).getChildNodes();
				printNote(childs);
				for (int j = 0; j < childs.getLength(); j++) {
					Node n = childs.item(j);
					switch (n.getNodeName()) {
					case "x":
						tmp.x = Integer.parseInt(n.getTextContent());
						break;
					case "y":
						tmp.y = Integer.parseInt(n.getTextContent());
						break;
					case "collider":
						tmp.blocked = n.getTextContent().equals("full");
						if (tmp.blocked) {
							tmp.model = new ModelInstance(rs.box);
							rs.allmodels.add(tmp.model);
							System.out.println(tmp.x + " " + tmp.y);
						}
						break;
					}

					// tmp.image_size = new Vector2f(32, 32);
				}
				if (tmp.x == 7 && tmp.y == 0) {
					System.out.println(tmp);
				}

				map.setTileAt(tmp.x, tmp.y, tmp);
				System.out.println(map.getTileAt(tmp.x, tmp.y) == tmp);
				// printNote(tiles.item(i).getChildNodes());
			}
		}
	}

	private static void printNote(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				System.out.println("Node Value =" + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue());

					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					// printNote(tempNode.getChildNodes());

				}

				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		}
	}
}
