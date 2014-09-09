package cz.fi.muni.pa165;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

public class DogDao {

	@PersistenceUnit
	private EntityManagerFactory emf;
	
	
	public Person savePerson(){
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		Person p = new Person();
		em.persist(p);
		em.flush();
		et.commit();
		em.close();
		em = emf.createEntityManager();
		p = em.find(Person.class, new Long(-1));
		return p;
	}

	
}
