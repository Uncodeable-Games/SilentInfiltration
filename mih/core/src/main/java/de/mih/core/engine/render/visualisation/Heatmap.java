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
		colors = new Color[6];
		colors[0] = new Color(0, 0, 0, 1);
		colors[1] = Color.BLUE;//new Color(0.5f, 0.6f, 0.2f, 0.7f);
		colors[2] = Color.GREEN;new Color(0.6f, 0.7f, 0.2f, 0.5f);//Color.YELLOW;
		colors[3] = Color.YELLOW;
		colors[4] = Color.CORAL;
		colors[5] = Color.RED;

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
				events[i][j] = 0;
			}
		}

	}
	
	public float max_events;
	
	public void render()
	{
		
		
	    ImmediateModeRenderer20 r = new ImmediateModeRenderer20(false, true, 0);
		PerspectiveCamera camera = Game.getCurrentGame().getRenderManager().getCamera();
		
		//passes the projection matrix to the camera
		max_events = 25;
		for(int x = 0; x < len -1; x += 1)
		{

			for(int z = 0; z < width ; z+= 1)
			{
				if(events[x][z] > max_events){
					//max_events= events[x][z];
					events[x][z] = (int) max_events;
				}
			}
		}
//		System.out.println(max_events);
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
					int c = (Integer) Math.round((float) (events[x][z] + 1) / (float) max_events * 5.0f) ;
					r.color(colors[c]);
					r.vertex(x* 0.5f, 0, z* 0.5f);
					c  = (Integer) Math.round((float) (events[x+1][z] + 1)/ (float) max_events * 5.0f) ;
					r.color(colors[c]);

					r.vertex((x +1)* 0.5f, 0, z* 0.5f);
				}
				
			}
			r.end();


		}

		//flush the renderer
	}
}
