package de.mih.core.game.player;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Interaction
{
	
	public String  command;
	public Texture icon;

	int actor;
	int target;

	public InteractionListener listener;
	
	public ArrayList<String> filter = new ArrayList<String>();

	public Interaction(String c, Texture i)
	{
		command = c;
		icon = i;
	}

	public void setTarget(int t)
	{
		target = t;
	}

	public void setActor(int a)
	{
		actor = a;
	}

	public void interact()
	{
		listener.onInteraction(actor, target);
	}

	public static interface InteractionListener
	{
		public void onInteraction(int actor, int target);
	}
}
