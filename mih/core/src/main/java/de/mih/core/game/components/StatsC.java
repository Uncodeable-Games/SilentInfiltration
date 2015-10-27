package de.mih.core.game.components;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.component.Component;

public class StatsC extends Component
{

	public int alertlevel = 0;
	public float alertlevelmulti = 1f;

	/* 
	 * Speed is no universal stat, i could imagine an entity with hitpoints that cannot move
	 */
	public float speed = 2;
	public float sneakspeed = 1;
	public float walkspeed = 2;
	public float runspeed = 3;

	public int hits = 0;
	public int maxhits = 5;

	/*
	 * All these shoul be reorganized as abilities
	 * We could create an AbilityComponent that could be extended or include script files. 
	 */
	public boolean canunlockdoors = false;
	public boolean canlockdoors = false;
	public boolean cansmashdoors = false;
	public boolean canuseweapons = false;
	public boolean canko = false;
	public boolean candisguise = false;
	public boolean canhideincabinet = false;
	public boolean canclimbvent = false;
	public boolean canwalkinsecareas = false;
	public boolean canpoison = false;

}
