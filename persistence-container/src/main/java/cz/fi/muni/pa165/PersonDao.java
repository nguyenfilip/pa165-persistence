package cz.fi.muni.pa165;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

public class PersonDao {

	@PersistenceContext
	private EntityManager em;
	
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	
	@Transactional
	public Person savePerson(){
		Person p = new Person();
		EntityManager em2 = emf.createEntityManager();
		em2.getTransaction().begin();
		em2.persist(p);
		em2.getTransaction().commit();
		em2.close();
		return p;
	}

	public void findPerson() {
		System.out.println(em.find(Person.class, new Long(1)));
	}

	
	public List<Person> findAllPeople() {
		return em.createQuery("select p from Person p", Person.class).getResultList();
	}
}
