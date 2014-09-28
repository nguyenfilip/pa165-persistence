package cz.fi.muni.pa165;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.dto.PetsByColorDTO;
import cz.fi.muni.pa165.entity.Cage;
import cz.fi.muni.pa165.entity.Pet;
import cz.fi.muni.pa165.entity.Pet.PetColor;


@ContextConfiguration(classes=DaoContext.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class EntityLifecycleTest  extends AbstractTestNGSpringContextTests
{
	@PersistenceUnit
	public EntityManagerFactory emf;
	private long pet1Id;
	 
	
	@BeforeMethod
	public void setup(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Calendar cal = Calendar.getInstance();
		
		Pet pet = new Pet();
		pet.setName("FILIP");		
		pet.setBirthDate(cal.getTime());
		pet.setColor(PetColor.WHITE);
		
		
		em.persist(pet);
		pet1Id = pet.getId();
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void entityDetachTest() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Pet pet = em.find(Pet.class, pet1Id);
		Assert.assertTrue(em.contains(pet));
		em.detach(pet);
		Assert.assertFalse(em.contains(pet));
		em.getTransaction().commit();
		em.close();
		
		/**
		 * Modification of detached entity
		 */
		pet.setName("Honza");
		
		em = emf.createEntityManager();
		em.getTransaction().begin();
		
		/**
		 * Here the pet should be associated with em again. 
		 * In other words, it should get into MANAGED state again.
		 */
		//ASSOCIATE HERE
		pet = em.merge(pet);
		
		Pet petFromDb = em.find(Pet.class, pet1Id);
		Assert.assertEquals(petFromDb.getName(),"Honza");
		
		//CHANGE MANAGED ENTITY HERE
		pet.setName("Marek");
		
		petFromDb = em.find(Pet.class, pet1Id);
		Assert.assertEquals(petFromDb.getName(),"Marek");
		
		em.getTransaction().commit();
		em.close();
		
	}
	
	
	@Test
	public void entityRemove() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		long size = (Long)em.createQuery("SELECT COUNT(p) FROM Pet p").getSingleResult();
		Assert.assertEquals(size,1);
		
		//DELETE THE PET HERE
		Pet p = em.find(Pet.class, pet1Id);
		em.remove(p);
		
		size = (Long)em.createQuery("SELECT COUNT(p) FROM Pet p").getSingleResult();
		Assert.assertEquals(size,0);
		em.getTransaction().commit();
		em.close();
		
	}
	
	
	@Test
	public void persistCascade(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Cage c = new Cage();
		c.setDescription("My new cage!");
		Pet p = new Pet();
		p.setName("Jirka");
		p.setCage(c);
		
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}
}