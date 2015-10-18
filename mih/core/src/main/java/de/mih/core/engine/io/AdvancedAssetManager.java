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

import de.mih.core.engine.render.RenderManager;
import de.mih.core.game.components.VisualC;

public class AdvancedAssetManager {	
	private static AdvancedAssetManager instance;
	public AssetManager assetManager;
	RenderManager renderManager;
	
	public ArrayList<Model> allmodeltypes = new ArrayList<Model>();
	public ArrayList<VisualC> allvisuals = new ArrayList<VisualC>();
	public HashMap<String, Model> storedmodels;
	
	public AdvancedAssetManager(RenderManager renderManager)
	{
		this.assetManager = new AssetManager();
		this.renderManager = renderManager;
		instance = this;
		loading();
	}
	
	public static AdvancedAssetManager getInstance()
	{
		return instance;
	}
	
	public void loading()
	{
		storedmodels = readinModels("assets/models/");

		// TODO: Outsource Modelinformations
		
		 Model redbox = this.renderManager.getModelBuilder().createBox(1f, 2f, 1f, new
		 Material(ColorAttribute.createDiffuse(Color.RED)),
		 VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		 allmodeltypes.add(redbox);
		 storedmodels.put("redbox", redbox);

		Model floor = this.renderManager.getModelBuilder().createBox(0.3f, .01f, 0.3f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		storedmodels.put("floor", floor);

		Model center = this.renderManager.getModelBuilder().createBox(0.5f, .01f, 0.5f,
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(center);
		storedmodels.put("center", center);

	}
	

	
	public HashMap<String, Model> readinModels(String path) {
		HashMap<String, Model> temp = new HashMap<String, Model>();
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("obj")) {
						temp.put(handle.nameWithoutExtension(), this.renderManager.getModelLoader()
								.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.nameWithoutExtension()));
						temp.put(handle.name(), this.renderManager.getModelLoader()
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
		return storedmodels.get("redbox");
	}

}
