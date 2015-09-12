package de.mih.core.engine.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

import de.mih.core.game.components.VisualC;
import de.mih.core.game.render.RenderManager;

public class AdvancedAssetManager {
	static AdvancedAssetManager advancedAssetManager;
	
	public AssetManager assetManager;
	
	public ArrayList<Model> allmodeltypes = new ArrayList<Model>();
	public ArrayList<VisualC> allvisuals = new ArrayList<VisualC>();
	public HashMap<String, Model> storedmodels;
	
	public AdvancedAssetManager()
	{
		this.assetManager = new AssetManager();
		loading();
	}
	
	public static AdvancedAssetManager getInstance()
	{
		if(advancedAssetManager == null)
		{
			advancedAssetManager = new AdvancedAssetManager();
		}
		return advancedAssetManager;
	}
	
	public void loading()
	{
		storedmodels = readinModels("assets/models/");

		// TODO: Outsource Modelinformations
		
		 Model redbox = RenderManager.getInstance().getModelBuilder().createBox(1f, 2f, 1f, new
		 Material(ColorAttribute.createDiffuse(Color.RED)),
		 VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		 allmodeltypes.add(redbox);
		 storedmodels.put("redbox", redbox);

		Model floor = RenderManager.getInstance().getModelBuilder().createBox(0.3f, .01f, 0.3f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		storedmodels.put("floor", floor);
	}
	

	
	public HashMap<String, Model> readinModels(String path) {
		HashMap<String, Model> temp = new HashMap<String, Model>();
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("obj")) {
						temp.put(handle.name(), RenderManager.getInstance().getModelLoader()
								.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.name()));
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	
	public Model getModelByName(String s) {
		if (storedmodels.containsKey(s)) {
			return storedmodels.get(s);
		}
		System.out.println("Model " + s + " not found!");
		return storedmodels.get("redbox");
	}

}
