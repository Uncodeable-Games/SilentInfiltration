package de.mih.core.engine.ecs;

import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SystemManager extends BaseRenderer
{
	
	static SystemManager systemM;
	
	List<BaseSystem>          registeredSystems;
	PriorityQueue<BaseSystem> rS;
	/**
	 * linked entityManger for iteration over entities
	 */
	EntityManager             entityManager;

	private boolean limitRenderer;

	private List<Integer> entitiesToRender;

	public SystemManager(RenderManager renderManager, EntityManager entityManager, int initialCapacity)
	{
		super(renderManager, true, 1);
		this.entityManager = entityManager;
		this.registeredSystems = new ArrayList<BaseSystem>();
	}

	public void register(BaseSystem s)
	{
		if (!registeredSystems.contains(s))
		{
			registeredSystems.add(s);
			Collections.sort(registeredSystems);
		}
	}

	public void update(double dt)
	{
		for (BaseSystem s : registeredSystems)
		{
			for (int entity = 0; entity < entityManager.entityCount; entity++)
			{
				if (s.matchesSystem(entity))
				{
					s.update(dt, entity);
				}
			}
			s.update(dt);
		}
	}
	
	public void limitRenderer(boolean limit)
	{
		limitRenderer = limit;
	}
	
	public void setEntitiesToRender(List<Integer> entitiesToRender)
	{
		this.entitiesToRender = entitiesToRender;
	}

	public void render()
	{
		if (this.limitRenderer)
		{
			render(this.entitiesToRender);
			return;
		}
		for (BaseSystem s : registeredSystems)
		{
			for (int entity = 0; entity < entityManager.entityCount; entity++)
			{
				if (s.matchesSystem(entity))
				{
					s.render(entity);
				}
			}
			s.render();
		}
	}
	
	public void render(List<Integer> entitiesToRender)
	{
		for (BaseSystem s : registeredSystems)
		{
			for (Integer entity : entitiesToRender)
			{
				if (s.matchesSystem(entity))
				{
					s.render(entity);
				}
			}
			s.render();
		}
	}
}
