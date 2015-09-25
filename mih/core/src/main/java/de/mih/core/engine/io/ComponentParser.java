package de.mih.core.engine.io;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import de.mih.core.engine.ecs.Component;

public interface ComponentParser {
	static Map<String, ComponentParser> componentParser = new HashMap<>();
	
	public static void addComponentParser(String name, ComponentParser parser)
	{
		componentParser.put(name,parser);
	}
	
	public static ComponentParser getComponentParser(String name)
	{
		return componentParser.get(name);
	}
	
	
	public abstract Component parseXML(Node node);

	public static boolean hasParser(String nodeName)
	{
		return componentParser.containsKey(nodeName);
	}
	
	public static void print()
	{
		for(String s : componentParser.keySet())
		{
			System.out.println(s + ": " + componentParser.get(s));
		}
	}
}


