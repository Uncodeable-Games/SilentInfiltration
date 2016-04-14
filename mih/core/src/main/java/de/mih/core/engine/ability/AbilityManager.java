package de.mih.core.engine.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import de.mih.core.game.Game;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobias on 12.04.2016.
 */
public class AbilityManager
{
	private Map<Integer, Ability> idMapping = new HashMap<>();

	public Ability getAbilityById(int id)
	{
		return idMapping.get(id);
	}

	public void registerAbilities(String path)
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
						String content = null;
						try
						{
							content = new String(Files.readAllBytes(handle.file().toPath()), "UTF-8");
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}

						Ability ability = new Json().fromJson(Ability.class, content);
						ability.setScript(Game.getCurrentGame().getLuaScriptManager().loadScript(ability.getScriptPath()));
						ability.getScript().getGlobals().set("Ability", CoerceJavaToLua.coerce(ability));
						idMapping.put(ability.getId(), ability);
					}
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
