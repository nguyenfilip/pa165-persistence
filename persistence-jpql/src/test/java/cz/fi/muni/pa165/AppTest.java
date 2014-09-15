package cz.fi.muni.pa165;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.test.annotation.DirtiesContext;
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
public class AppTest  extends AbstractTestNGSpringContextTests
{
	@PersistenceUnit
	public EntityManagerFactory emf;
	
	@DirtiesContext
	@BeforeMethod
	public void setup(){
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		Calendar cal = Calendar.getInstance();
		
		Pet pet = new Pet();
		pet.setName("FILIP");		
		pet.setBirthDate(cal.getTime());
		pet.setColor(PetColor.WHITE);
		
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Pet pet2 = new Pet();
		pet2.setName("STEFAN");	
		pet2.setColor(PetColor.RED);
		pet2.setBirthDate(cal.getTime());
		
		Pet pet3 = new Pet();
		pet3.setName("MARTIN");	
		pet3.setColor(PetColor.RED);
		
		
		Cage cage0 =new Cage();
		cage0.setDescription("Small uncomfortable cage");
		Cage cage1 =new Cage();
		cage1.setDescription("Big nice cage");
		
		pet.setCage(cage1);
		pet2.setCage(cage1);
		cage1.getPets().add(pet);
		cage1.getPets().add(pet2);
		
		
		em.persist(cage0);
		em.persist(cage1);
		em.persist(pet);
		em.persist(pet2);
		em.persist(pet3);
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void simpleTest() {
		Assert.assertNotNull(emf);
	}
	
	
	@Test
	public void selectCagesWithPets() {
		EntityManager em = emf.createEntityManager();
		List<Cage> cages = em.createQuery("SELECT c FROM Cage c JOIN FETCH c.pets", Cage.class).getResultList();
		em.close();
		
		System.out.println(cages.get(0).getPets().size());
	}
	
	@Test
	public void groupBy() {
		EntityManager em = emf.createEntityManager();
		List<Object[]> pets = em.createQuery("SELECT p.color, count(p) FROM Pet p GROUP BY p.color").getResultList();
		Long count = em.createQuery("SELECT 1", Long.class).getSingleResult();
		Assert.assertEquals(count, new Long(1));
		em.close();
	}
	
	@Test
	public void groupBySelectNew() {
		EntityManager em = emf.createEntityManager();
		List<PetsByColorDTO> pets = em.createQuery("SELECT NEW cz.fi.muni.pa165.dto.PetsByColorDTO(p.color, count(p)) FROM Pet p GROUP BY p.color",PetsByColorDTO.class).getResultList();
		
		for (PetsByColorDTO dto: pets){
			System.out.println(dto.getColor() + " : " +dto.getPetCount());
		}
		em.close();
	}
	
	@Test
	public void findAllPets() {
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("FROM Pet",Pet.class).getResultList();
		Assert.assertEquals(pets.size(), 1);
		em.close();
	}
	

	@Test
	public void findAllPetsNamedQuery() {
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createNamedQuery("findAll",Pet.class).getResultList();
		Assert.assertEquals(pets.size(), 2);
		em.close();
	}
	
	
	
	@Test
	public void findCatStefan() {
		EntityManager em = emf.createEntityManager();
		Pet pet = em.createQuery("FROM Pet p WHERE p.name ='STEFAN'",Pet.class).getSingleResult();
		Assert.assertEquals(pet.getName(), "STEFAN");
		em.close();
	}
	
	@Test
	public void countPets() {
		EntityManager em = emf.createEntityManager();
		Long count = em.createQuery("SELECT COUNT(c) FROM Cage c",Long.class).getSingleResult();
		Assert.assertEquals(count, new Long(2));
		em.close();
	}
	
	
	@Test
	public void countAllCages() {
		EntityManager em = emf.createEntityManager();
		Long count = em.createQuery("SELECT COUNT(c) FROM Cage c",Long.class).getSingleResult();
		Assert.assertEquals(count, new Long(2));
		em.close();
	}
	
	
	@Test
	public void findAllNonEmptyCages() {
		EntityManager em = emf.createEntityManager();
		List<Cage> cages = em.createQuery("FROM Cage c WHERE c.pets IS NOT EMPTY",Cage.class).getResultList();
		Assert.assertEquals(cages.size(), 1);
		em.close();
	}
	

	@Test
	public void nonNullableName() {
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Pet pet = new Pet();
		pet.setName("Jirka");
		pet.setColor(PetColor.BLACK);
		pet.setBirthDate(new Date());
		em.persist(pet);
		
		em.getTransaction().commit();
		em.close();
	}
	
	
	
	@Test
	public void findPetsBornToday() {
		
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("FROM Pet p WHERE p.birthDate = :date",Pet.class).setParameter("date", new Date()).getResultList();
		em.close();		
		System.out.println(pets.size());
	}
	
	
	@Test
	public void playWithDateTime() {
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Pet pet = new Pet();
		pet.setName("Tomas");
		pet.setColor(PetColor.BLACK);
		pet.setBirthDate(new Date());
		em.persist(pet);
		
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();		
		System.out.println(em.find(Pet.class, pet.getId()));
		em.close();		
	}
	
	
	@Test
	public void saveAndModifyPet() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Pet pet = new Pet();
		pet.setName("Tomas");
		pet.setColor(PetColor.BLACK);
		pet.setBirthDate(new Date());
		em.persist(pet);
		em.detach(pet);
		pet.setColor(PetColor.RED);
		//Retrieves managed entity from Database!
		pet = em.merge(pet);
		em.getTransaction().commit();
		em.close();
		
		
		pet.setColor(PetColor.WHITE);
		
		em = emf.createEntityManager();
		em.getTransaction().begin();
		pet = em.merge(pet);
		em.refresh(pet);
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();
		
		System.out.println(em.find(Pet.class, pet.getId()));
		em.close();		
	}
	
	
	@Test
	public void addCageWithPet() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Cage cage = new Cage();
		cage.setDescription("Super big cage");
		Pet pet = new Pet();
		pet.setName("Jirka");
		
		em.persist(cage);
		em.persist(pet);
		
		
		
		em.getTransaction().commit();
		em.close();
		
		em = emf.createEntityManager();
		Pet jirkaFromDatabase = em.find(Pet.class, pet.getId());
		System.out.println(jirkaFromDatabase.getCage().getPets().size());
		System.out.println(jirkaFromDatabase);
		System.out.println(jirkaFromDatabase.getCage());
		em.close();
		
		
	}
}