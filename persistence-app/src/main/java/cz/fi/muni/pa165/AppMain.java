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
		Calendar cal = Calendar.getInstance();
		
		Pet pet1 = new Pet();
		pet1.setName("Sisi");
		cal.set(2013, 1, 22);
		pet1.setBirthDate(cal.getTime());
		pet1.setColor(PetColor.BLACK);
	
		em.persist(pet1);;
		em.getTransaction().commit();
		em.close();
		
		printAllPets(emf);
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

}
