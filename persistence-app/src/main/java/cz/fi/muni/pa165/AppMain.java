package cz.fi.muni.pa165;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.fi.muni.pa165.entity.Cage;
import cz.fi.muni.pa165.entity.Pet;
import cz.fi.muni.pa165.entity.Pet.PetColor;

public class AppMain {


	public static void main(String[] args) throws SQLException {
		//The following line is here just to start up a in-memory database 
		new AnnotationConfigApplicationContext(DaoContext.class);
		
		System.out.println(" ****** STARTING PET STORE APPLICATOIN ****** ");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Cage cage1 = new Cage();
		cage1.setDescription("SMALL CAGE");
		cage1.setCapacity(3);
		Cage cage2 = new Cage();
		cage2.setDescription("BIG CAGE");
		cage2.setCapacity(3);
		
		Calendar cal = Calendar.getInstance();
		
		
		Pet pet1 = new Pet();
		pet1.setName("Sisi");
		pet1.setCage(cage1);
		cage1.addPet(pet1);
		cal.set(2013, 1, 22);
		pet1.setBirthDate(cal.getTime());
		pet1.setColor(PetColor.BLACK);
		
		Pet pet2 = new Pet();
		pet2.setName("Rex");
		pet2.setCage(cage1);
		cage1.addPet(pet2);
		cal.set(2012, 1, 4);
		pet2.setBirthDate(cal.getTime());
		pet2.setColor(PetColor.RED);
		
		Pet pet3 = new Pet();
		pet3.setName("Dan");
		pet3.setCage(cage2);
		cage2.addPet(pet3);
		cal.set(2013, 5, 21);
		pet3.setBirthDate(cal.getTime());
		pet3.setColor(PetColor.WHITE);
		
		Pet pet4 = new Pet();
		pet4.setName("Fiona");
		pet4.setCage(cage2);
		cage2.addPet(pet4);
		cal.set(2014, 2, 12);
		pet4.setBirthDate(cal.getTime());
		pet4.setColor(PetColor.WHITE);
		
		
		em.persist(pet1);
		em.persist(pet2);
		em.persist(pet3);
		em.persist(pet4);
		em.persist(cage1);
		em.persist(cage2);
		em.getTransaction().commit();
		em.close();
		
		printAllCages(emf);
	}
	
	public static void printAllPets(EntityManagerFactory emf){
		System.out.println(" ********************************");
		System.out.println("        PET LIST      ");
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = em.createQuery("SELECT p from Pet p",Pet.class).getResultList();
		
		for (Pet p : pets) {
			System.out.println(p);
			System.out.println(p.getCage());
		}
		
		em.close();
		
	}
	public static void printAllCages(EntityManagerFactory emf){
		System.out.println(" ********************************");
		System.out.println("        CAGES LIST      ");
		EntityManager em = emf.createEntityManager();
		List<Cage> cages = em.createQuery("SELECT c from Cage c",Cage.class).getResultList();
		
		for (Cage c : cages) {
			System.out.println(c);
			for(Pet p : c.getPets()) {
				System.out.println(" - "+p);
			}
		}
		
		em.close();
		
	}

}
