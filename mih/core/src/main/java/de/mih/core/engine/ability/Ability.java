package de.mih.core.engine.ability;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.game.Game;

public class Ability
{
	private int id = -1;

	private Castable castable;

	public Ability(){}

	public Ability(int id, Castable oncast){
		this.id = id;
		this.castable = oncast;

		Game.getCurrentGame().getAbilityManager().addAbility(this);
	}

	public int getId()
	{
		return id;
	}

	public Castable getCastable()
	{
		return castable;
	}

	public void castNoTarget(int caster){
		this.castable.noTarget(caster);
	}

	public void castOnPoint(int caster, Vector2 target){
		this.castable.onPoint(caster,target);
	}

	public void castOnTarget(int caster, int targetId){
		this.castable.onTarget(caster,targetId);
	}
}
