package de.mih.core.engine.ecs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;

import de.mih.core.engine.io.ComponentParser;
import de.mih.core.game.components.UnittypeC;

public class BlueprintManager {
	
	Map<String, EntityBlueprint> blueprints = new HashMap<>();
	Map<String, Class<? extends Component>> componentTypes = new HashMap<>();
	private ComponentFactory componentFactory;
	
	static BlueprintManager blueprintManager;
	
	public static BlueprintManager getInstance()
	{
		if(blueprintManager == null)
		{
			blueprintManager = new BlueprintManager();
		}
		return blueprintManager;
	}
	
	public void registerComponentType(String name, Class<? extends Component> componentType)
	{
		componentTypes.put(name, componentType);
	}
	
	
	public boolean readBlueprintFromXML(String path)
	{
		
		File file = Gdx.files.internal(path).file();

		if (!file.exists()) {
			return false;
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
			return false;
		
		dom.getDocumentElement().normalize();
		try {
			readBlueprint(dom.getDocumentElement());
			return true;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Component readComponent(Node node) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		String componentTypeName = node.getNodeName().toLowerCase();
		Class<? extends Component>  componentType = componentTypes.get(componentTypeName);
		Component component = componentType.newInstance();
		
		System.out.println(componentTypeName);
		Map<String,String> fields = new HashMap<>();
		NodeList attr = node.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			if(a.getNodeType() != Node.ELEMENT_NODE)
				continue;
			component.setField(a.getNodeName(), a.getTextContent());
			
			//TODO: maybe use the following:
			fields.put(a.getNodeName(), a.getTextContent());	
		}
		//TODO: maybe use the following:
		//componentFactory.componentFromType(componentTypeName, fields);

		return component;
	}
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	
	
	private void readBlueprint(Node node) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		//System.out.println(node.getChildNodes().item(1).getNodeName());
		String name = node.getAttributes().getNamedItem("name").getNodeValue();
		System.out.println("read name: "  + name);
		EntityBlueprint blueprint = new EntityBlueprint(name);
		NodeList comps = node.getChildNodes();
		
		for (int i = 0; i < comps.getLength(); i++) {
			System.out.println(comps.item(i).getNodeName());
			if (comps.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Node n = comps.item(i);
				String componentType = n.getNodeName().toLowerCase();
				blueprint.addComponent(readComponent(n));
				
			}
		}
		blueprint.addComponent(new UnittypeC(name));
		this.blueprints.put(name, blueprint);
	}
	
	public int createEntityFromBlueprint(String name)
	{
		return this.blueprints.get(name).generateEntity();
	}
	
	public int createEntityFromBlueprint(String name, int entityId)
	{
		return this.blueprints.get(name).generateEntity(entityId);
	}
}
