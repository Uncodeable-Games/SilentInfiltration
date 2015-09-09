package de.mih.core.engine.ai.btree;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;

public class BTreeParser {
	
	public static BehaviorTree<Unit> readInBTree(String s, Unit u){	
		Reader reader = null;
		reader = Gdx.files.internal(s).reader();
		BehaviorTreeParser<Unit> parser = new BehaviorTreeParser<Unit>(BehaviorTreeParser.DEBUG_NONE);
		return parser.parse(reader, u);
	}
}
