package de.mih.core.engine.ability;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.lua.LuaScript;
import de.mih.core.game.Game;

public class Ability
{
	private int id = -1;

	private LuaScript script;

	public Ability(){}

	public Ability(int id, String scriptPath){
		this.id = id;
		script = Game.getCurrentGame().getLuaScriptManager().loadScript(scriptPath);
		Game.getCurrentGame().getAbilityManager().addAbility(this);
	}

	public int getId()
	{
		return id;
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
