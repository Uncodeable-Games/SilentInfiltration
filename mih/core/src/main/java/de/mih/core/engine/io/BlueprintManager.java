package de.mih.core.engine.io;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.io.Blueprints.Blueprint;
import de.mih.core.engine.io.Blueprints.EntityBlueprint;
import de.mih.core.engine.io.Blueprints.Tilemap.TileBlueprint;
import de.mih.core.engine.io.Blueprints.Tilemap.TileBorderBlueprint;
import de.mih.core.engine.io.Blueprints.Tilemap.TilemapBlueprint;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.BorderC;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * The BlueprintManager reads enitityBlueprints from XML and stores them internally.
 * With the stored enitityBlueprints the manager creates new entities.
 *
 * @author Tobias
 */
public class BlueprintManager
{
	Map<String,EntityBlueprint> entityBlueprints = new HashMap<>();

	private EntityManager entityManager;
	private boolean noGraphics;

	public BlueprintManager(EntityManager entityManager, boolean noGraphics)
	{
		this.entityManager = entityManager;
		this.noGraphics = noGraphics;
	}

	static BlueprintManager blueprintManager;

	private Json json = new Json();
	
	public boolean isNoGraphics()
	{
		return noGraphics;
	}

	@Deprecated
	public static BlueprintManager getInstance()
	{
		return blueprintManager;
	}

	public Tilemap readTilemapBlueprint(String path){
		File file = Gdx.files.internal(path).file();
		if (!file.exists())
		{
			return null;
		}
		try
		{
			String           content = new String(Files.readAllBytes(file.toPath()), "UTF-8");
			TilemapBlueprint bp      = json.fromJson(TilemapBlueprint.class, content);
			Tilemap tilemap = new Tilemap(bp.getLength(),bp.getWidth(),bp.getTILESIZE(),bp.getName());
			for (TileBorderBlueprint tileBorderBlueprint : bp.getBorders()){

				Tile tile = tilemap.getTileAt(tileBorderBlueprint.getBorders()[0].getX(),tileBorderBlueprint.getBorders()[0].getY());

				int entity = GameLogic.getCurrentGame().getBlueprintManager().createEntityFromBlueprint(tileBorderBlueprint.getCollider());

				BorderC.BorderType borderType = GameLogic.getCurrentGame().getEntityManager().getComponent(entity,BorderC.class).getBorderType();

				switch(borderType)
				{
					case Door:
					{
						tile.getBorder(tileBorderBlueprint.getBorders()[0].getDirection()).setToDoor(entity,tileBorderBlueprint.getCollider());
						break;
					}

					case Wall:
					{
						tile.getBorder(tileBorderBlueprint.getBorders()[0].getDirection()).setToWall(entity,tileBorderBlueprint.getCollider());
						break;
					}
				}
				TileBorder tileBorder = GameLogic.getCurrentGame().getEntityManager().getComponent(entity,BorderC.class).getTileBorder();
				if(!noGraphics)
				{
					tileBorder.setTexture(0,tileBorderBlueprint.getTextures()[0]);
					tileBorder.setTexture(1,tileBorderBlueprint.getTextures()[1]);
				}
			}

			for (TileBlueprint tileBlueprint : bp.getTiles()){
				Tile tile = tilemap.getTileAt(tileBlueprint.getX(),tileBlueprint.getY());
				if(!noGraphics)
				{
					tile.setTexture(tileBlueprint.getTexture());
				}
			}

			return tilemap;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public boolean readEntityBlueprint(String path)
	{
		try
		{
			Files.walk(Paths.get(path)).forEach(filePath ->
			{
				if (Files.isRegularFile(filePath))
				{
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("json"))
					{
						readEntityBlueprintFromPath(handle.path());
					}
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public boolean readEntityBlueprintFromPath(String path)
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
			entityBlueprints.put(file.getName(), bp);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public void writeBlueprintToJson(Blueprint blueprint, String path) throws IOException
	{
		FileWriter writer = new FileWriter(path);
		writer.write(json.prettyPrint(blueprint));
		writer.close();
	}

	public int createEntityFromBlueprint(String name)
	{
		return this.entityBlueprints.get(name).generateEntity();
	}

	public int createEntityFromBlueprint(String name, int entityId)
	{
		return this.entityBlueprints.get(name).generateEntity(entityId);
	}
}
