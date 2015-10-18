package de.mih.core.engine.ai;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;

public class BTreeParser
{

	public static BehaviorTree<Integer> readInBTree(String s, Integer u)
	{
		Reader reader = null;
		reader = Gdx.files.internal(s).reader();
		BehaviorTreeParser<Integer> parser = new BehaviorTreeParser<Integer>(BehaviorTreeParser.DEBUG_NONE);
		return parser.parse(reader, u);
	}
}
