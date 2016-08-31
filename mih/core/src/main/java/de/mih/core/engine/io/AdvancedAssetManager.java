package de.mih.core.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.UBJsonReader;

import de.mih.core.engine.render.RenderManager;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class AdvancedAssetManager {
	private static AdvancedAssetManager instance;
	public AssetManager assetManager;
	private ObjLoader objLoader;
	private G3dModelLoader g3dModelLoader;

	public ArrayList<Model> allmodeltypes;
	public HashMap<String, Model> storedmodels;

	public AdvancedAssetManager() {
		this.assetManager = new AssetManager();
		instance = this;
		objLoader = new ObjLoader();
		g3dModelLoader = new G3dModelLoader(new UBJsonReader());

		allmodeltypes = new ArrayList<Model>();
		storedmodels = new HashMap<>();

	}

	public void demo() {
		Model redbox = ((Game) Game.getCurrentGame()).getRenderManager().getModelBuilder().createBox(1f, 2f, 1f,
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(redbox);
		storedmodels.put("redbox", redbox);

		Model floor = ((Game) Game.getCurrentGame()).getRenderManager().getModelBuilder().createBox(3f, .01f, 3f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		storedmodels.put("floor", floor);
	}

	public static AdvancedAssetManager getInstance() {
		return instance;
	}

	public void loadModels(String path) {
		HashMap<String, Model> temp = new HashMap<String, Model>();
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("obj")) {
						temp.put(handle.nameWithoutExtension(), objLoader.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.nameWithoutExtension()));
						temp.put(handle.name(), objLoader.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.name()));
					}
					if (handle.extension().equals("g3db")) {
						temp.put(handle.nameWithoutExtension(),
								g3dModelLoader.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.nameWithoutExtension()));
						temp.put(handle.name(), g3dModelLoader.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.name()));
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return temp;
		this.storedmodels = temp;
	}

	public void loadTextures(String path) {
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("png")) {
						this.assetManager.load(filePath.toString(), Texture.class);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Model getModelByName(String s) {
		if (storedmodels.containsKey(s)) {
			return storedmodels.get(s);
		}
		return storedmodels.get("redbox");
	}
}
