package cz.fi.muni.pa165;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.entity.Address;
import cz.fi.muni.pa165.entity.PetStore;


@ContextConfiguration(classes=DaoContext.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class PetStoreMappingTest  extends AbstractTestNGSpringContextTests
{
	@PersistenceUnit
	public EntityManagerFactory emf;
	
	@Test
	public void testSimplePersist() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("AHXZ");
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
		assertPetStoreExists();
		
	}

	@Test
	public void dateOfOpeningHasNoTime() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("");
		ps.setDateOfOpening(new Date());
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
		
		PetStore fromDb = loadPetStoreFromDatabase(ps.getId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDb.getDateOfOpening());
		
		Assert.assertEquals(cal.get(Calendar.MINUTE),0);
		Assert.assertEquals(cal.get(Calendar.HOUR),0);
		Assert.assertEquals(cal.get(Calendar.SECOND),0);
	}

	
	@Test
	public void openTimeAndCloseTimeHasNoDate() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("");
		ps.setOpenTime(new Date());
		ps.setCloseTime(new Date());
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
		
		PetStore fromDb = loadPetStoreFromDatabase(ps.getId());
		Calendar openTime = Calendar.getInstance();
		Calendar closeTime = Calendar.getInstance();
		openTime.setTime(fromDb.getOpenTime());
		closeTime.setTime(fromDb.getCloseTime());
		
		Assert.assertEquals(openTime.get(Calendar.YEAR),1970);
		Assert.assertEquals(openTime.get(Calendar.DAY_OF_MONTH),1);
		Assert.assertEquals(openTime.get(Calendar.MONTH),0);
		
		Assert.assertEquals(closeTime.get(Calendar.YEAR),1970);
		Assert.assertEquals(closeTime.get(Calendar.DAY_OF_MONTH),1);
		Assert.assertEquals(closeTime.get(Calendar.MONTH),0);
	}

	
	@Test
	public void embeddedAddressTest() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("");
		Address address = new Address();
		address.setCity("Brno");
		address.setStreet("Botanicka");
		address.setZipcode("4455566");
		ps.setAddress(address);
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
		
		PetStore fromDb = loadPetStoreFromDatabase(ps.getId());
		
		Assert.assertEquals(fromDb.getAddress().getCity(),"Brno");
		assertContainsAnnotation(PetStore.class,"address", Embedded.class);
	}

	
	@Test
	public void embeddedOldAddressesTest() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("");
		
		Address address = new Address();
		address.setCity("Brno");
		address.setStreet("Botanicka");
		address.setZipcode("4455566");
		
		Address address2 = new Address();
		address2.setCity("Brno");
		address2.setStreet("Obla");
		address2.setZipcode("117799");
		
		ps.addPreviousAddress(address);
		ps.addPreviousAddress(address2);
		
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
		
		PetStore fromDb = loadPetStoreFromDatabase(ps.getId());


		List<Address> previous = new ArrayList<Address>(fromDb.getPreviousAddresses());
		Assert.assertEquals(previous.get(0).getStreet(), "Botanicka");
		Assert.assertEquals(previous.get(1).getStreet(), "Obla");
		
		assertContainsAnnotation(PetStore.class,"previousAddresses", ElementCollection.class);
	}

	@Test(expectedExceptions=PersistenceException.class, expectedExceptionsMessageRegExp=".+DataException.+")
	public void identifierMaxLenIs10() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("AHXZAFDFSFDEFWEFEWFEW");
		em.persist(ps);
		em.getTransaction().commit();
		em.close();
	}
	
	@Test(expectedExceptions=PersistenceException.class, expectedExceptionsMessageRegExp=".+ConstraintViolationException.+")
	public void uniqueTextIdentifier() {	
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		PetStore ps = new PetStore();
		ps.setTextIdentifier("AHXZ");
		em.persist(ps);
		
		PetStore ps2 = new PetStore();
		ps2.setTextIdentifier("AHXZ");
		em.persist(ps2);
		em.getTransaction().commit();
		em.close();
		
	}

	private void assertContainsAnnotation(Class<?> clazz,
			String fieldName, Class<?> annotationClass) {
		HashSet<Annotation> annotations = null;
		boolean found = false;
		try {
			Field f = clazz.getDeclaredField(fieldName);
			
			Assert.assertNotNull(f,"You removed field "+fieldName+" from the class. it should be present");
			
			
			for (Annotation annotation : f.getAnnotations()){
				
				if (annotation.annotationType().equals(annotationClass)) {
					found = true;
				}
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(found,"The annotation "+annotationClass+" was not found on field " + fieldName);
	}


	private void assertPetStoreExists() {
		EntityManager em = emf.createEntityManager();
		Boolean exists = (Boolean)em.createQuery("SELECT COUNT(ps) > 0 FROM PetStore ps").getSingleResult();
		em.close();
		
		Assert.assertTrue(exists);
	}
	

	private PetStore loadPetStoreFromDatabase(long id) {
		EntityManager em = emf.createEntityManager();
		PetStore ps = em.find(PetStore.class, id);
		em.close();
		return ps;
	}
	
	
}