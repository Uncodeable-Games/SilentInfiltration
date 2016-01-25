package de.mih.core.engine.ecs;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.events.order.SelectEvent;

public class EventManager
{
	HashMap<Class<? extends BaseEvent>, ArrayList<EventListener<? extends BaseEvent>>> registeredHandlers = new HashMap<>();
	
	List<String> eventMessages = new ArrayList<>();

	LinkedList<BaseEvent> eventQueue = new LinkedList<>();
	FileHandle logFile;
	
	public EventManager()
	{
//		Calendar cal  = Calendar.getInstance();
//		Date     time = cal.getTime();
//		DateFormat formatter = new SimpleDateFormat();
		
		 logFile = Gdx.files.local("log.txt");
	}

	public void register(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
	{
		if (!registeredHandlers.containsKey(eventType))
		{
			registeredHandlers.put(eventType, new ArrayList<EventListener<? extends BaseEvent>>());
		}
		registeredHandlers.get(eventType).add(eventListener);
	}

	public void unregister(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
	{
		if (registeredHandlers.containsKey(eventType))
		{
			if (registeredHandlers.get(eventType).contains(eventListener))
			{
				registeredHandlers.get(eventType).remove(eventListener);
			}
		}
	}

	public void queueEvent(BaseEvent event)
	{
		this.eventQueue.add(event);
	}
	
	public void update()
	{
		if(eventQueue.isEmpty())
			return;
		BaseEvent event = this.eventQueue.poll();
		this.fire(event);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fire(BaseEvent event)
	{
//		if(event instanceof SelectEvent)
//		{
//			System.out.println("selected: " + ((SelectEvent) event).selectedentity);
//		}
		Calendar cal  = Calendar.getInstance();
		Date     time = cal.getTime();
		DateFormat formatter = new SimpleDateFormat();
		logFile.writeString(event.toString() + "\n" , true, "UTF-8");
		//logFile.writeString(string, append, "UTF-8");
		//Gdx.app.log("EventManager", event.toString());
		System.out.println(formatter.format(time) + ": " + event.toString());
		this.eventMessages.add(event.toString());
		if (registeredHandlers.containsKey(event.getClass()))
		{
			for(EventListener listener : registeredHandlers.get(event.getClass()))
			{
				listener.handleEvent(event);
			}
		}
	}
	
	public void saveEventLog() throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("eventlog.txt", "UTF-8");
		for(String message : eventMessages)
		{
			writer.println(message);
		}
		writer.close();
	}
	

}
