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
import de.mih.core.game.components.Visual;
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

		for (int i = 0; i < comps.getLength(); i++) {
			if (comps.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node n = comps.item(i);
				switch (n.getNodeName()) {

				case "Collider":
					// Collider has to be initialized last!
					hascollider = true;
					break;

				case "Control":
					initControl(n);
					break;

				case "Position":
					initPosition(n);
					break;

				case "Selectable":
					initSelectable(n);
					break;
	
				case "Velocity":
					initVelocity(n);
					break;

				case "Visual":
					initVisual(n);
					break;
					
				}
			}
		}

		if (hascollider)
			entityM.addComponent(new_unit, new ColliderC(entityM.getComponent(new_unit, Visual.class)));

		return new_unit;
	}
	
	void initControl(Node parent) {
		entityM.addComponent(new_unit, new Control());
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "withmouse":
				entityM.getComponent(new_unit, Control.class).withmouse = (a.getTextContent().equals("true"));
				break;

			case "withwasd":
				entityM.getComponent(new_unit, Control.class).withwasd = (a.getTextContent().equals("true"));
				break;

			case "withkeys":
				entityM.getComponent(new_unit, Control.class).withkeys = (a.getTextContent().equals("true"));
				break;

			}
		}
	}

	void initPosition(Node parent) {
		entityM.addComponent(new_unit, new PositionC(new Vector3()));
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "position":
				tokenizer = new StringTokenizer(a.getTextContent(), ",");
				entityM.getComponent(new_unit, PositionC.class).position.x = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, PositionC.class).position.y = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, PositionC.class).position.z = Float.parseFloat(tokenizer.nextToken());
				break;

			case "angle":
				entityM.getComponent(new_unit, PositionC.class).angle = Integer.parseInt(a.getTextContent());
				break;

			}
		}
	}
	
	void initSelectable(Node parent){
		entityM.addComponent(new_unit, new SelectableC());
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "selected":
				entityM.getComponent(new_unit,SelectableC.class).selected = (a.getTextContent().equals("true"));
				break;
			}
		}
	}

	void initVelocity(Node parent) {
		entityM.addComponent(new_unit, new VelocityC(new Vector3()));
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "drag":
				entityM.getComponent(new_unit, VelocityC.class).drag = Float.parseFloat(a.getTextContent());
				break;

			case "maxspeed":
				entityM.getComponent(new_unit, VelocityC.class).maxspeed = Float.parseFloat(a.getTextContent());
				break;

			}
		}
	}

	void initVisual(Node parent) {
		NodeList attr = parent.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			if (a.getNodeName().equals("model")) {
				entityM.addComponent(new_unit, new Visual(a.getTextContent(), rs));
			}
		}

		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "position":
				tokenizer = new StringTokenizer(a.getTextContent(), ",");
				entityM.getComponent(new_unit, Visual.class).pos.x = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, Visual.class).pos.y = Float.parseFloat(tokenizer.nextToken());
				entityM.getComponent(new_unit, Visual.class).pos.z = Float.parseFloat(tokenizer.nextToken());
				
				break;

			case "scale":
				tokenizer = new StringTokenizer(a.getTextContent(), ",");
				entityM.getComponent(new_unit, Visual.class).setScale(Float.parseFloat(tokenizer.nextToken()),
						Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()));
				break;

			case "angle":
				entityM.getComponent(new_unit, Visual.class).angle = Integer.parseInt(a.getTextContent());
				break;

			}
		}
	}
}
