package cz.fi.muni.pa165;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.fi.muni.pa165.entity.Pet;

public class Main {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoContext.class);
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("myUnit");
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Pet pet = new Pet();
		pet.setName("Filip");
		em.persist(pet);
		em.getTransaction().commit();
		em.close();
		
		System.out.println(pet.getId());
		System.out.println(pet.getName());
	}

}
