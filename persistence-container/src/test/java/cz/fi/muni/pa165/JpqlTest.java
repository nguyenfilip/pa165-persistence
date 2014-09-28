package cz.fi.muni.pa165;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.spi.LoadState;

import org.hibernate.annotations.OnDeleteAction;
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
public class JpqlTest  extends AbstractTestNGSpringContextTests
{
	@PersistenceUnit
	public EntityManagerFactory emf;
	
	@DirtiesContext
	@BeforeMethod
	public void setup(){
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		Calendar cal = Calendar.getInstance();
		
		Pet pet1 = new Pet();
		pet1.setName("Filip");		
		pet1.setBirthDate(cal.getTime());
		pet1.setColor(PetColor.WHITE);
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Pet pet2 = new Pet();
		pet2.setName("Stefan");	
		pet2.setColor(PetColor.RED);
		pet2.setBirthDate(cal.getTime());
		
		Pet pet3 = new Pet();
		pet3.setName("MARTIN");	
		pet3.setColor(PetColor.RED);
		
		
		Cage cage1 =new Cage();
		cage1.setDescription("Small uncomfortable cage");
		Cage cage2 =new Cage();
		cage2.setDescription("Big nice cage");
		
		pet1.setCage(cage1);
		pet2.setCage(cage1);
		cage1.getPets().add(pet1);
		cage1.getPets().add(pet2);
		
		
		em.persist(cage1);
		em.persist(cage2);
		em.persist(pet1);
		em.persist(pet2);
		em.persist(pet3);
		em.getTransaction().commit();
		em.close();
	}
	
	
	@Test
	public void findPets(){
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("SELECT p FROM Pet p", Pet.class).getResultList();
		em.close();
		
		Assert.assertEquals(pets.size(), 3);
	}
	
	/**
	 * The task is to find all cages and instruct the JPA to fetch all Pets for every such cage. Thanks to this
	 * it will be possible to work with the pets even after the EntityManager closes.
	 * 
	 * This test basically checks whether the pets are LOADED. You are expected to change the JPQL
	 * query so that c.pets are loaded. This is done by so called FETCH JOIN, take a look into 
	 * JPA spec for the syntax. 
	 */
	@Test
	public void fetchJoinCagesWithPets() {
		EntityManager em = emf.createEntityManager();
		List<Cage> cages = em.createQuery("SELECT c FROM Cage c JOIN c.pets", Cage.class).getResultList();
		em.close();
		
	
		for (Cage cage : cages)
			Assert.assertEquals(PersistenceUtilHelper.isLoaded(cage.getPets()),LoadState.LOADED);
	}
	
	/**
	 * This test shows how to use group by to find number of pets by color. The group by part of the
	 * query is missing.
	 */
	@Test
	public void groupBy() {
		EntityManager em = emf.createEntityManager();
		List<Object[]> pets = em
				.createQuery(
						"SELECT p.color, count(p) FROM Pet ORDER BY p.color")
				.getResultList();
		em.close();
		
		Assert.assertEquals(((PetColor) pets.get(0)[0]), PetColor.RED);
		Assert.assertEquals(((Long) pets.get(0)[1]), new Long(2));
		Assert.assertEquals(((PetColor) pets.get(1)[0]), PetColor.WHITE);
		Assert.assertEquals(((Long) pets.get(1)[1]), new Long(1));

		
	}
	
	/**
	 * This test is very similar to groupBy. The only difference is that you are expected to produce something
	 * more practical than Object[] as was the case in groupBy 
	 * 
	 * In this query more things are missing. First thing is the group by clause. The second
	 * thing missing is so called SELECT NEW construct. You should consult JPA spec and then use SELECT NEW with 
	 * PetsByColorDTO class (look up the constructor of the class).
	 */
	@Test
	public void groupBySelectNew() {
		EntityManager em = emf.createEntityManager();
		List<PetsByColorDTO> petsByColor = em.createQuery("SELECT FROM Pet p ORDER BY p.color",PetsByColorDTO.class).getResultList();
		em.close();

		Assert.assertEquals(petsByColor.get(0).getColor(), PetColor.RED);
		Assert.assertEquals(petsByColor.get(0).getPetCount(), new Long(2));
		Assert.assertEquals(petsByColor.get(1).getColor(), PetColor.WHITE);
		Assert.assertEquals(petsByColor.get(1).getPetCount(), new Long(1));
	}
	
	/**
	 * This should be easy one. There is a bug in the query, try to read the exception carefully and find out what is wrong.
	 */
	@Test
	public void findAllPets() {
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("SELECT Pet FROM Pet",Pet.class).getResultList();
		em.close();
		
		Assert.assertEquals(pets.size(), 3);
	}
	

	/**
	 * Your task is to find all Pets in the database using so called Named Query.
	 * 
	 * Showcase how to use named query, the createNamedQuery in this test shouldn't have null as the first argument.
	 * Hint: look at annotations above Pet entity, you should deduce what the first argument for the createNamedQuery should be
	 */
	@Test
	public void findAllPetsNamedQuery() {
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createNamedQuery(null,Pet.class).getResultList();
		em.close();
		
		Assert.assertEquals(pets.size(), 3);
	}
	
	
	
	/**
	 * This query should produce only 1 cat, the one with name 'Stefan', as you can see the WHERE clause is missing.
	 */
	@Test
	public void findCatStefan() {
		EntityManager em = emf.createEntityManager();
		Pet pet = em.createQuery("SELECT p FROM Pet p'",Pet.class).getSingleResult();
		em.close();
		
		Assert.assertEquals(pet.getName(), "Stefan");
	}
	
	/**
	 * The task is to count number of pets in the database.
	 * 
	 * This query should produce only 1 number. We call such queries scalar queries. You should use the COUNT function
	 * to get the result needed by the Assert in this test.
	 */
	@Test
	public void countPets() {
		EntityManager em = emf.createEntityManager();
		Long count = em.createQuery("SELECT p FROM Pet p",Long.class).getSingleResult();
		em.close();
		
		Assert.assertEquals(count, new Long(3));
	}
	
	
	/**
	 * Your task is to find cages that have no pets in it.
	 * 
	 * This test requires you to find out syntax of negated EMPTY operator that is part of JPQL syntax.
	 * Note this is something new, not presnet in SQL dialects. Its really object oriented way of querying, because the query
	 * basically says "find those cages that have non empty cages.pets collection". Consult the JPA spec to
	 * get the syntax right. 
	 */
	@Test
	public void findAllNonEmptyCages() {
		EntityManager em = emf.createEntityManager();
		List<Cage> cages = em.createQuery("SELECT c FROM Cage c WHERE c.pets IS",Cage.class).getResultList();
		em.close();
		
		Assert.assertEquals(cages.size(), 1);
		Assert.assertEquals(cages.get(0).getDescription(), "Small uncomfortable cage");
	}
	
	/**
	 * Your task is to find all touples (Cage, Pet) such that every cage is listed (even if they have no pets) and you are looking only
	 * for the pets 'Filip'. To do so you are required to use the LEFT JOIN.
	 * 
	 * The LEFT JOIN semantics together with ON keyword is fairly complicated. See JPA spec section 4.4.5.2
	 * 
	 * This query will test lot of your skills and also understanding. The LEFT JOIN is very similar to SQL LEFT JOIN. To get the results 
	 * that pass asserts in this test you will need to use ON clause and also use the SELECT NEW again
	 * 
	 */
	@Test
	public void leftJoinCagesWithPets() {
		EntityManager em = emf.createEntityManager();
		List<CageAndPet> cageAndPet = em.createQuery("SELECT NEW  FROM Cage c LEFT JOIN c.pets p",CageAndPet.class).getResultList();
		
		Assert.assertEquals(cageAndPet.size(), 2);
		
		//Note that the JOIN is not fetching anything, the pets wont be loaded!
		Assert.assertEquals(PersistenceUtilHelper.isLoaded(cageAndPet.get(0).getCage().getPets()), LoadState.NOT_LOADED);
		Assert.assertEquals(PersistenceUtilHelper.isLoaded(cageAndPet.get(1).getCage().getPets()), LoadState.NOT_LOADED);
		
		//The first cage has only 1 pet Filip associated
		Assert.assertEquals(cageAndPet.get(0).getCage().getDescription(),"Small uncomfortable cage");
		Assert.assertEquals(cageAndPet.get(0).getPet().getName(),"Filip");
				
		//Thanks for to the LEFT JOIN semantics, we get a cage without a pet
		Assert.assertNotNull(cageAndPet.get(1).getCage());
		Assert.assertNull(cageAndPet.get(1).getPet());
		
		em.close();
	}
	
	
	/**
	 * This test requires you not only to fix JPQL query but also to use setParameter method on the created query.
	 * 
	 * Your task is to find the Pets that has been born today.
	 * 
	 * To do so you should use a named parameter (e.g. :date) and you should set the paraemeter as an object.
	 */
	@Test
	public void findPetsBornToday() {
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("SELECT p FROM Pet p WHERE ",Pet.class).getResultList();
		em.close();		
	
		Assert.assertEquals(pets.size(),1);
		Assert.assertEquals(pets.get(0).getName(), "Filip");
	}
}