package de.mih.core.engine.ecs;

import java.io.File;
import java.io.IOException;
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

import de.mih.core.engine.io.Blueprints.EntityBlueprint;

/**
 * The BlueprintManager reads blueprints from XML and stores them internally.
 * With the stored blueprints the manager creates new entities.
 * 
 * @author Tobias
 */
public class BlueprintManager
{

	Map<String, EntityBlueprint> blueprints = new HashMap<>();

	private EntityManager entityManager;

	public BlueprintManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	static BlueprintManager blueprintManager;

	@Deprecated
	public static BlueprintManager getInstance()
	{
		return blueprintManager;
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
