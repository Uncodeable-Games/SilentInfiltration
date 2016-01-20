package de.mih.core.engine.render.visualisation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer.Random;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.game.Game;

public class Heatmap
{
	public int[][] events;

	Color[] colors; 
	
	int len = 80;
	int width = 120;
	
	boolean first = true;
	public Heatmap(int x, int y)
	{
//		events = new int[len][width];
//		int c = 0;
//		for(int i = 0; i < len; i++)
//		{
//			for(int j = 0; j < width; j++)
//			{
//				events[i][j] = 
//			}
//		}
//		colors = new Color[5];
//		colors[0] = new Color(0, 0, 0, 1);
//		colors[0] = new Color(0.5f, 0.6f, 0.2f, 0.7f);
//		colors[0] = new Color(0.6f, 0.7f, 0.2f, 0.5f);//Color.YELLOW;
//		colors[0] = Color.ORANGE;
//		colors[4] = Color.RED;
		java.util.Random rand = new java.util.Random();
		if(!first)
			return;
		first = true;
		events = new int[len][width];
		int c = 0;
		for(int i = 0; i < len; i++)
		{
			for(int j = 0; j < width; j++)
			{
				events[i][j] = 0;// rand.nextInt(5);
//				if(i == j && j > 0)
//				{
//					events[i][j] = j;
//				}
//				if(i == 0 || j == 0)
//					events[i][j] = 0;// 
//				else if(i == 1 || j == 1)
//					events[i][j] = 2;
//				else if(i == 2 || j == 2)
//					events[i][j] = 3;
//				else if(i == 3 || j == 3)
//					events[i][j] = 4;
			}
		}
//		events[4][4] = 3;
//		events[4][5] = 4;
//		events[5][5] = 3;
//		events[5][6] = 3;
//		events[5][7] = 3;
//		events[5][8] = 3;
//		events[6][5] = 3;
//		events[6][6] = 4;
//		events[6][7] = 4;
//		events[6][8] = 3;
//		events[7][5] = 3;
//		events[7][6] = 4;
//		events[7][7] = 4;
//		events[7][8] = 3;
//		events[8][5] = 3;
//		events[8][6] = 3;
//		events[8][7] = 3;
//		events[8][8] = 3;
		

		//events[2][2] = 3;
//		events[1][1] = 1;
//		events[1][2] = 1;
//		events[2][1] = 1;
//		events[2][2] = 1;
//		events[2][4] = 3;
//		events[1][2] = 3;
//		events[1][3] = 3;
//		events[1][4] = 3;
//		events[3][2] = 3;
//		events[2][2] = 4;
//		events[3][4] = 3;
		colors = new Color[5];
		colors[0] = Color.CLEAR;
		colors[1] = Color.GREEN;
		colors[2] = Color.YELLOW;
		colors[3] = Color.ORANGE;
		colors[4] = Color.RED;
	}
	
	public float max_events;
	
	public void render()
	{
		
		
	    ImmediateModeRenderer20 r = new ImmediateModeRenderer20(false, true, 0);
		PerspectiveCamera camera = Game.getCurrentGame().getRenderManager().getCamera();
		
		//passes the projection matrix to the camera
		max_events = 0;
		for(int x = 0; x < len -1; x += 1)
		{

			for(int z = 0; z < width ; z+= 1)
			{
				if(events[x][z] > max_events)
					max_events= events[x][z];
			}
		}
		Color[] test = new Color[(int) max_events +1];
		test[0] = Color.CLEAR;
		test[(int) (max_events) / 2] = Color.YELLOW;
		Color tmp  = Color.YELLOW;
		Color tmp2 = Color.RED;


		test[(int) (max_events)] = Color.RED;
		
		for(int i = 0; i < max_events; i++)
		{
			if(i < max_events/2 )
			{
				float alpha = (float) i / (float) max_events /2 ;
				test[i] = new Color(alpha * tmp.r, alpha * tmp.g, alpha * tmp.b, (alpha) * 255f);
				
			}
			else if(i > max_events/2)
			{
				float alpha = ((float) i - (max_events/2)) / (float) max_events;
				test[i] = new Color(alpha * tmp2.r + (1.0f- alpha) * tmp.r, alpha * tmp2.g + (1.0f- alpha) * tmp.g, alpha * tmp2.b + (1.0f- alpha) * tmp.b, 1.0f);
			}
		}
		//push our vertex data here...
		//specify normals/colors/texcoords before vertex position
		for(int x = 0; x < len -1; x += 1)
		{
			r.begin(camera.combined, GL20.GL_TRIANGLE_STRIP);

			for(int z = 0; z < width ; z+= 1)
			{
				Vector3 v1 = new Vector3(x* 0.5f, 0, z* 0.5f);
				Vector3 v2 = new Vector3((x +1)* 0.5f, 0, z* 0.5f);

				if(Game.getCurrentGame().getRenderManager().isVisible(v1) || Game.getCurrentGame().getRenderManager().isVisible(v2))
				{
					int c = events[x][z];

					r.color(test[c]);
					r.vertex(x* 0.5f, 0, z* 0.5f);
					c = events[x +1][z];

					r.color(test[c]);

					r.vertex((x +1)* 0.5f, 0, z* 0.5f);
				}
				
			}
			r.end();


		}

		//flush the renderer
	}
}
