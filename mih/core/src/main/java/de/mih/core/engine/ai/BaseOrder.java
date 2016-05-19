package de.mih.core.engine.ai;

public abstract class BaseOrder
{
	private static int ID;
	
	public BaseOrder()
	{
		this.orderID = BaseOrder.ID++;
	}
	
	private int orderID;
	public int entityID;

	public int getID()
	{
		return orderID;
	}
	
	abstract public void handle();//int entity);

	abstract public boolean isFinished();

	abstract public void finish();

	abstract public void stop();

	abstract public boolean isStopped();
}
