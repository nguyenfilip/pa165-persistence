package cz.fi.muni.pa165;

import cz.fi.muni.pa165.dto.PetsByColorDTO;
import cz.fi.muni.pa165.entity.Cage;
import cz.fi.muni.pa165.entity.Pet;
import cz.fi.muni.pa165.entity.Pet.PetColor;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class JpqlTest extends AbstractTestNGSpringContextTests {

    @PersistenceUnit
    public EntityManagerFactory emf;

    @DirtiesContext
    @BeforeMethod
    public void setup() {
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

        Cage cage1 = new Cage();
        cage1.setDescription("Small uncomfortable cage");
        Cage cage2 = new Cage();
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
    public void findPets() {
        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p", Pet.class).getResultList();
        em.close();

        Assert.assertEquals(pets.size(), 3);
    }

    @Test
    public void fetchJoinCagesWithPets() {
        EntityManager em = emf.createEntityManager();
        List<Cage> cages = em.createQuery("SELECT c FROM Cage c JOIN FETCH c.pets", Cage.class).getResultList();
        em.close();

        for (Cage cage : cages) {
            Assert.assertEquals(PersistenceUtilHelper.isLoaded(cage.getPets()), LoadState.LOADED);
        }
    }

    @Test
    public void groupBy() {
        EntityManager em = emf.createEntityManager();
        List<Object[]> pets = em
                .createQuery(
                        "SELECT p.color, count(p) FROM Pet p GROUP BY p.color ORDER BY p.color")
                .getResultList();
        em.close();

        Assert.assertEquals(((PetColor) pets.get(0)[0]), PetColor.RED);
        Assert.assertEquals(((Long) pets.get(0)[1]), new Long(2));
        Assert.assertEquals(((PetColor) pets.get(1)[0]), PetColor.WHITE);
        Assert.assertEquals(((Long) pets.get(1)[1]), new Long(1));

    }

    @Test
    public void groupBySelectNew() {
        EntityManager em = emf.createEntityManager();
        List<PetsByColorDTO> petsByColor = em.createQuery("SELECT NEW cz.fi.muni.pa165.dto.PetsByColorDTO(p.color, count(p)) FROM Pet p GROUP BY p.color ORDER BY p.color", PetsByColorDTO.class).getResultList();
        em.close();

        Assert.assertEquals(petsByColor.get(0).getColor(), PetColor.RED);
        Assert.assertEquals(petsByColor.get(0).getPetCount(), new Long(2));
        Assert.assertEquals(petsByColor.get(1).getColor(), PetColor.WHITE);
        Assert.assertEquals(petsByColor.get(1).getPetCount(), new Long(1));
    }

    @Test
    public void findAllPets() {
        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createQuery("SELECT p FROM Pet p", Pet.class).getResultList();
        em.close();

        Assert.assertEquals(pets.size(), 3);
    }

    @Test
    public void findAllPetsNamedQuery() {
        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createNamedQuery("findAll", Pet.class).getResultList();
        em.close();

        Assert.assertEquals(pets.size(), 3);
    }

    @Test
    public void findCatStefan() {
        EntityManager em = emf.createEntityManager();
        Pet pet = em.createQuery("FROM Pet p WHERE p.name ='Stefan'", Pet.class).getSingleResult();
        em.close();

        Assert.assertEquals(pet.getName(), "Stefan");
    }

    @Test
    public void countPets() {
        EntityManager em = emf.createEntityManager();
        Long count = em.createQuery("SELECT COUNT(p) FROM Pet p", Long.class).getSingleResult();
        em.close();

        Assert.assertEquals(count, new Long(3));
    }

    @Test
    public void findAllNonEmptyCages() {
        EntityManager em = emf.createEntityManager();
        List<Cage> cages = em.createQuery("SELECT c FROM Cage c WHERE c.pets IS NOT EMPTY", Cage.class).getResultList();
        em.close();

        Assert.assertEquals(cages.size(), 1);
        Assert.assertEquals(cages.get(0).getDescription(), "Small uncomfortable cage");
    }

    /**
     * The LEFT JOIN semantics together with ON keyword is fairly complicated. See JPA spec section 4.4.5.2
     */
    @Test
    public void leftJoinCagesWithPets() {
        EntityManager em = emf.createEntityManager();
        List<CageAndPet> cageAndPet = em.createQuery("SELECT NEW cz.fi.muni.pa165.CageAndPet(c,p) FROM Cage c LEFT JOIN c.pets p ON p.name= 'Filip'", CageAndPet.class).getResultList();

        Assert.assertEquals(cageAndPet.size(), 2);

        //Note that the JOIN is not fetching anything, the pets wont be loaded!
        Assert.assertEquals(PersistenceUtilHelper.isLoaded(cageAndPet.get(0).getCage().getPets()), LoadState.NOT_LOADED);
        Assert.assertEquals(PersistenceUtilHelper.isLoaded(cageAndPet.get(1).getCage().getPets()), LoadState.NOT_LOADED);

        //The first cage has only 1 pet Filip associated
        Assert.assertEquals(cageAndPet.get(0).getCage().getDescription(), "Small uncomfortable cage");
        Assert.assertEquals(cageAndPet.get(0).getPet().getName(), "Filip");

        //Thanks for to the LEFT JOIN semantics, we get a cage without a pet
        Assert.assertNotNull(cageAndPet.get(1).getCage());
        Assert.assertNull(cageAndPet.get(1).getPet());

        em.close();
    }

    @Test
    public void findPetsBornToday() {

        EntityManager em = emf.createEntityManager();
        List<Pet> pets = em.createQuery("FROM Pet p WHERE p.birthDate = :date", Pet.class).setParameter("date", new Date()).getResultList();
        em.close();

        Assert.assertEquals(pets.size(), 1);
        Assert.assertEquals(pets.get(0).getName(), "Filip");
    }
}
