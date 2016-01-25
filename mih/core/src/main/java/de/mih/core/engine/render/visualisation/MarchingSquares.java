package de.mih.core.engine.render.visualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;

public class MarchingSquares
{
	public Cell newCell()
	{
		return new Cell();
	}
	public ShapeRenderer sr;
	public class Cell
	{
		public float isoLT, isoRT, isoLB, isoRB;
		public Vector3 lt, rt, lb, rb;
		
	}
	HashMap<Float, List<Vector3>> isolines = new HashMap<>();
	
	public Cell[][] cells;
	
	
	public void calculateIsoline(float c)
	{
		drawCount = 0;
		List<Vector3> isoline = new ArrayList<>();
		for(int i = 0; i < cells.length; i++)
		{
			for(int j = 0; j < cells[0].length; j++)
			{
				Cell current = cells[i][j];
				int index = calculateIndex(current, c);
				float gamma;
//				if(index > 0 && index < 15)
//					System.out.println(index);
				switch(index)
				{
					case 0:
					case 15:
						break;
					case 1:
					case 14: 
						case2(isoline, c, current.isoLB, current.isoLT, current.isoRB, current.lb, current.lt, current.rb);
						break;
					case 2:
					case 13:
						case2(isoline, c, current.isoRB, current.isoLB, current.isoRT, current.rb, current.lb, current.rt);
						break;
					case 4:
					case 11:
						case2(isoline, c, current.isoRT, current.isoLT, current.isoRB, current.rt, current.lt, current.rb);
						break;
					case 7:
					case 8: //correct
						case2(isoline, c, current.isoLT, current.isoLB, current.isoRT, current.lt, current.lb, current.rt);
						break;
					case 3: 
					case 12://correct
						case3(isoline, c, current.isoLT, current.isoLB, current.isoRT, current.isoRB, current.lt, current.lb, current.rt, current.rb);
						break;
					case 6:
					case 9: //correct
						case3(isoline, c, current.isoLT, current.isoRT, current.isoLB, current.isoRB, current.lt, current.rt, current.lb, current.rb);
						break;
					case 5:
						//Decider
						gamma = current.isoLT - (current.isoLT - current.isoRB) * (current.isoLT - current.isoRT) / (current.isoLT - current.isoRT - current.isoRB + current.isoLB);
						if(gamma > c)
						{
							case2(isoline, c, current.isoLT, current.isoRT, current.isoLB, current.lt, current.rt, current.lb);
							case2(isoline, c, current.isoRB, current.isoRT, current.isoLB, current.rb, current.rt, current.lb);
						}
						else
						{
							case2(isoline, c, current.isoRT, current.isoLT, current.isoRB, current.rt, current.lt, current.rb);
							case2(isoline, c, current.isoLB, current.isoLT, current.isoRB, current.lb, current.lt, current.rb);
						}
						break;
					case 10:
						gamma = current.isoLT - (current.isoLT - current.isoRB) * (current.isoLT - current.isoRT) / (current.isoLT - current.isoRT - current.isoRB + current.isoLB);
						if(gamma > c)
						{
							case2(isoline, c, current.isoLB, current.isoLT, current.isoRB, current.lb, current.lt, current.rb);
							case2(isoline, c, current.isoRT, current.isoLT, current.isoRB, current.rt, current.lt, current.rb);
						}
						else
						{
							case2(isoline, c, current.isoLT, current.isoRT, current.isoLB, current.lt, current.rt, current.lb);
							case2(isoline, c, current.isoRB, current.isoRT, current.isoLB, current.rb, current.rt, current.lb);
						}
						break;
				}
			}
		}
		this.isolines.put(c, isoline);
		System.out.println("DRAWCOUNT: " + drawCount);
	}
	
	int drawCount = 0;
	void case2(List<Vector3> isoline, float c, float edge, float other1, float other2, Vector3 vEdge, Vector3 vOther1, Vector3 vOther2)
	{
		float a1 = calculateIntersection(edge, other1, c);
		float a2 = calculateIntersection(edge, other2, c);
		Vector3 intersection1 = vEdge.cpy().scl(1.0f - a1).add(vOther1.cpy().scl(a1));
		Vector3 intersection2 = vEdge.cpy().scl(1.0f - a2).add(vOther2.cpy().scl(a2));
		isoline.add(intersection1);
		isoline.add(intersection2);
		//sr.line(intersection1, intersection2);
		drawCount++;
	}
	
	void case3(List<Vector3> isoline, float c, float edge11, float edge12, float edge21, float edge22, Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4)
	{
		float a1 = calculateIntersection(edge11, edge12, c);
		float a2 = calculateIntersection(edge21, edge22, c);
//		System.out.println(a1 + " " + a2);
		Vector3 intersection1 = v1.cpy().scl(1.0f - a1).add(v2.cpy().scl(a1));
		Vector3 intersection2 = v3.cpy().scl(1.0f - a2).add(v4.cpy().scl(a2));
		isoline.add(intersection1);
		isoline.add(intersection2);
		//sr.line(intersection1, intersection2);
		drawCount++;
	}
	
	float calculateIntersection(float iso1, float iso2, float c)
	{
		//float c = iso1 + (iso2 - iso1)/ (0.5f) * alpha
//		System.out.println("Iso1: " + iso1);
//		System.out.println("Iso2: " + iso2);
//		System.out.println("c: " + c);

		float smaller = iso1; //Math.min(iso1, iso2);
		float greater = iso2; //Math.max(iso1, iso2);
		float alpha  = (c - smaller) / (greater- smaller) ;
		
//	s	System.out.println("alpha: " + alpha);
		///float alpha = 
		//float alpha =  Math.abs(iso2 - iso1) / c;
	//	System.out.println(iso2 - iso1);
		//System.out.println("alpha" + alpha);
		return alpha;
	}
	
	
	public int calculateIndex(Cell cell, float c)
	{
		int index = 0;
		if(cell.isoLT > c)
			index += 8;
		if(cell.isoRT > c)
			index += 4;
		if(cell.isoRB > c)
			index += 2;
		if(cell.isoLB > c)
			index += 1;
		return index;
	}
	
	public void render()
	{
		for(List<Vector3> list : this.isolines.values())
		{
			for(int i = 0; i < list.size(); i+= 2)
			{
				//System.out.println("line");
				//System.out.println(list.get(i) + " " + list.get(i + 1));
				if(Game.getCurrentGame().getRenderManager().isVisible(list.get(i)) || Game.getCurrentGame().getRenderManager().isVisible(list.get(i + 1)))
				{
					sr.line(list.get(i), list.get(i+1));
				}
			}
		}

	}
	

}
