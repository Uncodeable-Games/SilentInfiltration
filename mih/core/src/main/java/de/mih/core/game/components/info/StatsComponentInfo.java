package de.mih.core.game.components.info;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.StringTokenizer;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.StatsC;

public class StatsComponentInfo implements ComponentInfo<StatsC>
{
	public int alertlevel = 0;
	public float alertlevelmulti = 1f;

	public float speed = 2;

	public float sneakspeed = 1;
	public float walkspeed = 2;
	public float runspeed = 3;

	public int hits = 0;
	public int maxhits = 5;

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
	
	
	@Override
	public void readFields(Map<String, String> fields)
	{

		for (String key : fields.keySet())
		{
			Field field = null;
			if (key.equals("values"))
			{
				StringTokenizer st = new StringTokenizer(fields.get(key), "=\n ");
				while (st.hasMoreTokens())
				{
					try
					{
						field = this.getClass().getField(st.nextToken());
					}
					catch (NoSuchFieldException | SecurityException e)
					{
						e.printStackTrace();
					}

					try
					{
						if (field.getType().toString().equals("float"))
						{
							field.setFloat(this, Float.parseFloat(st.nextToken()));
						}
						if (field.getType().toString().equals("int"))
						{
							field.setInt(this, Integer.parseInt(st.nextToken()));
						}
					}
					catch (IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}

				}
			}

			if (key.equals("booleans"))
			{
				StringTokenizer st = new StringTokenizer(fields.get(key), "\n");
				while (st.hasMoreTokens())
				{
					try
					{
						field = this.getClass().getField(st.nextToken());
						field.setBoolean(this, true);
					}
					catch (NoSuchFieldException | SecurityException | IllegalArgumentException
							| IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public StatsC generateComponent()
	{
		StatsC tmp = new StatsC();
		tmp.alertlevel = alertlevel;
		tmp.alertlevelmulti = alertlevelmulti;

		tmp.speed = speed;

		tmp.sneakspeed = sneakspeed;
		tmp.walkspeed = walkspeed;
		tmp.runspeed = runspeed;

		tmp.hits = hits;
		tmp.maxhits = maxhits;

		tmp.canunlockdoors = canunlockdoors;
		tmp.canlockdoors = canlockdoors;
		tmp.cansmashdoors = cansmashdoors;
		tmp.canuseweapons = canuseweapons;
		tmp.canko = canko;
		tmp.candisguise = candisguise;
		tmp.canhideincabinet = canhideincabinet;
		tmp.canclimbvent = canclimbvent;
		tmp.canwalkinsecareas = canwalkinsecareas;
		tmp.canpoison = canpoison;
		
		return tmp;
	}

}
