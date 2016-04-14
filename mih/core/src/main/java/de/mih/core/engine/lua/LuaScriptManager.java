package de.mih.core.engine.lua;

import de.mih.core.game.Game;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Created by Cataract on 13.04.2016.
 */
public class LuaScriptManager
{

	public LuaScript loadScript(String path)
	{
		LuaScript script = new LuaScript();
		script.setLuaValue(script.getGlobals().loadFile(path));

		script.getGlobals().set("currentGame", CoerceJavaToLua.coerce(Game.getCurrentGame()));

		return script;
	}
}
