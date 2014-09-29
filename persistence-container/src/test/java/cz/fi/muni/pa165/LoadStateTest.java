package cz.fi.muni.pa165;

import cz.fi.muni.pa165.entity.Cage;
import cz.fi.muni.pa165.entity.Pet;
import cz.fi.muni.pa165.entity.Pet.PetColor;
import cz.fi.muni.pa165.entity.PetStore;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.spi.LoadState;
import org.hibernate.jpa.internal.util.PersistenceUtilHelper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(classes = DaoContext.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LoadStateTest extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    public EntityManagerFactory emf;
    private long petStoreId;

    @BeforeMethod
    public void setup() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Calendar cal = Calendar.getInstance();

        Pet pet1 = new Pet();
        pet1.setName("Sisi");
        pet1.setBirthDate(cal.getTime());
        pet1.setColor(PetColor.RED);

        Pet pet2 = new Pet();
        pet2.setName("Filip");
        pet2.setBirthDate(cal.getTime());
        pet2.setColor(PetColor.WHITE);

        Pet pet3 = new Pet();
        pet3.setName("Marek");
        pet3.setBirthDate(cal.getTime());
        pet3.setColor(PetColor.RED);

        Pet pet4 = new Pet();
        pet4.setName("Jana");
        pet4.setBirthDate(cal.getTime());
        pet4.setColor(PetColor.BLACK);

        Cage cage1 = new Cage();
        cage1.setCapacity(3);
        cage1.setDescription("Small cage");

        Cage cage2 = new Cage();
        cage2.setCapacity(4);
        cage2.setDescription("Big cage");

        Cage cage3 = new Cage();
        cage3.setCapacity(5);
        cage3.setDescription("Luxury cage");

        PetStore ps = new PetStore();
        ps.setTextIdentifier("AFZ");

        pet1.setCage(cage1);
        pet2.setCage(cage2);
        pet3.setCage(cage3);
        pet4.setCage(cage1);

        cage1.setPetStore(ps);
        cage2.setPetStore(ps);
        cage3.setPetStore(ps);

        em.persist(pet1);
        em.persist(pet2);
        em.persist(pet3);
        em.persist(pet4);

        em.persist(cage1);
        em.persist(cage2);
        em.persist(cage3);

        em.persist(ps);

        petStoreId = ps.getId();

        em.getTransaction().commit();
        em.close();
    }

    @Test(enabled = false)
    public void eagerFetchTest() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PetStore petStoreFromDb = em.find(PetStore.class, petStoreId);
        em.getTransaction().commit();
        em.close();

        /**
         * Add Assert.assertEquals here. Use PersistenceUtilHelper to find out whether the cages collection on the
         * PetStore is in LoadState.LOADED
         */
        Assert.assertEquals(PersistenceUtilHelper.isLoaded(petStoreFromDb.getCages()), LoadState.LOADED);

        for (Cage c : petStoreFromDb.getCages()) {
            for (Pet p : c.getPets()) {
                System.out.println(p.getName());
            }
        }

    }

    @Test
    public void lazyFetchCages() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PetStore petStoreFromDb = em.find(PetStore.class, petStoreId);
        em.getTransaction().commit();
        em.close();

        Assert.assertEquals(PersistenceUtilHelper.isLoaded(petStoreFromDb.getCages()), LoadState.NOT_LOADED);

    }

    @Test
    public void lazyFetchPets() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        PetStore petStoreFromDb = em.find(PetStore.class, petStoreId);
        Cage c = petStoreFromDb.getCages().iterator().next();
        em.getTransaction().commit();
        em.close();

        Assert.assertEquals(PersistenceUtilHelper.isLoaded(c.getPets()), LoadState.NOT_LOADED);

    }

}
