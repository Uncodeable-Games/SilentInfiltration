package de.mih.core.engine.ecs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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

	private Json json = new Json();

	@Deprecated
	public static BlueprintManager getInstance()
	{
		return blueprintManager;
	}

	public boolean readBlueprintFromJson(String path)
	{
		File file = Gdx.files.internal(path).file();
		if (!file.exists())
		{
			return false;
		}
		try
		{
			String          content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
			EntityBlueprint bp      = json.fromJson(EntityBlueprint.class, content);
			blueprints.put(file.getName(), bp);
			return true;
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void writeBlueprintToJson(EntityBlueprint blueprint, String path) throws IOException
	{
		FileWriter writer = new FileWriter(path);
		writer.write(json.prettyPrint(blueprint));
		writer.close();
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
