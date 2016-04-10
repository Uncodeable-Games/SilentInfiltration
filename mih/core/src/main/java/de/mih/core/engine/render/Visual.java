package de.mih.core.engine.render;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.components.VisualC;

import java.util.HashMap;

public class Visual
{
	public static HashMap<VisualC, Visual> visualc = new HashMap<>();

	private String        modeltype;
	private ModelInstance model;
	private int           angle;
	private Vector3     pos    = new Vector3();
	private Vector3     scale  = new Vector3(1f, 1f, 1f); // Do not make this public!
	private BoundingBox bounds = new BoundingBox();
	
	//Frustum Culling
	
	private Vector3 center     = new Vector3();
	private Vector3 dimensions = new Vector3();
	private float  radius;
	private Shader shader;
	
	public Visual(String modeltype)
	{
		this.modeltype = modeltype;
		model = new ModelInstance(AdvancedAssetManager.getInstance().getModelByName(modeltype));
		model.calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
	}
	
	public void setScale(float x, float y, float z)
	{
		scale.x = x;
		scale.y = y;
		scale.z = z;
		model.transform.scale(x, y, z);
		model.calculateBoundingBox(bounds);
		bounds.mul(model.transform);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
	}

	public String getModeltype()
	{
		return modeltype;
	}

	public ModelInstance getModel()
	{
		return model;
	}

	public int getAngle()
	{
		return angle;
	}

	public Vector3 getPos()
	{
		return pos;
	}

	public Vector3 getScale()
	{
		return scale;
	}

	public BoundingBox getBounds()
	{
		return bounds;
	}

	public Vector3 getDimensions()
	{
		return dimensions;
	}

	public Vector3 getCenter()
	{
		return center;
	}

	public float getRadius()
	{
		return radius;
	}

	public Shader getShader()
	{
		return shader;
	}
}
