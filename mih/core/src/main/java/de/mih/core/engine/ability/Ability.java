package de.mih.core.engine.ability;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.lua.LuaScript;

public class Ability
{
	private int id = -1;
	private String scriptPath;

	private transient LuaScript script;

	public Ability(){}

	public Ability(int id, String scriptPath){
		this.id = id;
		this.scriptPath = scriptPath;
	}

	public int getId()
	{
		return id;
	}

	public void setScript(LuaScript script)
	{
		this.script = script;
	}

	public String getScriptPath()
	{
		return scriptPath;
	}

	public void castNoTarget(int caster){
		script.run("onNoTarget",caster);
	}

	public void castOnPoint(int caster, Vector2 target){
		script.run("onPoint",caster,target);
	}

	public void castOnTarget(int caster, int targetId){
		script.run("onTarget",caster,targetId);
	}
}
