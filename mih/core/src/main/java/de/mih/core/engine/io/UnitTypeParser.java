package de.mih.core.engine.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.systems.RenderSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/*	TODO: Still add UnitType- and Unit-Class?
 * 	newUnit()
 * 		Pros:	
 * 				- UnitTypes-xmls can be easily changed at runtime (new Units have updated UnitType)
 * 				
 * 		Cons:	
 * 				- actual UnitType can't be changed at runtime (old Units still have old UnitType)
 * */

public class UnitTypeParser {

	RenderSystem rs;
	EntityManager entityM;

	public UnitTypeParser(RenderSystem rs, EntityManager em) {
		this.rs = rs;
		this.entityM = em;
	}

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	public int newUnit(String unittype) {

		File file = Gdx.files.internal("assets/unittypes/" + unittype + ".xml").file();

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
		
		dom.getDocumentElement().normalize();
		
		return readUnitType(dom.getDocumentElement());
	}

	int new_unit;
	StringTokenizer tokenizer;

	private int readUnitType(Node unittype) {

		new_unit = entityM.createEntity();
		boolean hascollider = false;

		NodeList comps = unittype.getChildNodes();
		ComponentParser.print();
		for (int i = 0; i < comps.getLength(); i++) {
			if (comps.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node n = comps.item(i);
				String componentType = n.getNodeName().toLowerCase();
				if(ComponentParser.hasParser(n.getNodeName()))
				{
					Component c =  ComponentParser.getComponentParser(componentType).parseXML(n);
					System.out.println(c);
					entityM.addComponent(new_unit,c);
				}
				else
				{
					System.out.println(componentType);
					if(componentType.equals("visual"))
					{
						initVisual(n);
					}
				}
			}
		}

		if (hascollider)
			entityM.addComponent(new_unit, new ColliderC(entityM.getComponent(new_unit, VisualC.class)));

		return new_unit;
	}
	


	void initVisual(Node parent) {
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			if (a.getNodeName().equals("model")) {
				entityM.addComponent(new_unit, new VisualC(a.getTextContent(), rs));
			}
		}

		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "position":
				tokenizer = new StringTokenizer(a.getTextContent(), ",");
				entityM.getComponent(new_unit, VisualC.class).visual.pos.x = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, VisualC.class).visual.pos.y = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, VisualC.class).visual.pos.z = Float.parseFloat(tokenizer.nextToken());
				
				break;

			case "scale":
				tokenizer = new StringTokenizer(a.getTextContent(), ",");
				entityM.getComponent(new_unit, VisualC.class).setScale(Float.parseFloat(tokenizer.nextToken()),
						Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()));
				break;

			case "angle":
				entityM.getComponent(new_unit, VisualC.class).visual.angle = Integer.parseInt(a.getTextContent());
				break;

			}
		}
	}
}
