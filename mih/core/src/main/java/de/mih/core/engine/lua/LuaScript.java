package de.mih.core.engine.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * Created by Cataract on 13.04.2016.
 */
public class LuaScript
{

	private Globals globals = JsePlatform.standardGlobals();
	private LuaValue luaValue;

	public void run(String functionname, Object... params){

		if (params.length == 0){
			globals.get(functionname).invoke();
			return;
		}

		LuaValue[] values = new LuaValue[params.length];
		for  (int i = 0; i<params.length;i++){
			values[i] = CoerceJavaToLua.coerce( params[i]);
		}

		globals.get(functionname).invoke(values);
	}

	public Globals getGlobals()
	{
		return globals;
	}

	public void setLuaValue(LuaValue luaValue)
	{
		this.luaValue = luaValue;
		this.luaValue.call();
	}
}
