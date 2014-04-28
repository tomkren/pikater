package vietpot.server.Database;

import javax.persistence.EntityManager;

public final class UpdateObject<R>
{
	public R entity;
	public EntityManager manager;
	
	public UpdateObject(EntityManager em, R entityInstance)
	{
		this.entity = entityInstance;
		this.manager = em;
	}
	
	public void Update()
	{
		/*
		if(!manager.getTransaction().isActive())
		{
			manager.getTransaction().begin();
		}
		*/
		manager.merge(entity);
		manager.getTransaction().commit();
	}
	
	public void finalizeUpdate()
	{
		Update();
		manager.close();
	}
}
