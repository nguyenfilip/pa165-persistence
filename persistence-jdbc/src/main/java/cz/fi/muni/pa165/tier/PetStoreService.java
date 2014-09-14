package cz.fi.muni.pa165.tier;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public class PetStoreService {

	private PetDao petDao;
	private CageDao cageDao;
	
	
	
	public PetDao getPetDao() {
		return petDao;
	}

	public void setPetDao(PetDao petDao) {
		this.petDao = petDao;
	}

	public CageDao getCageDao() {
		return cageDao;
	}

	public void setCageDao(CageDao cageDao) {
		this.cageDao = cageDao;
	}

	public Map<Integer, Pet> getAllPets() {
		return petDao.findAll();
	}

	@Transactional
	public void sellPet(Pet pet) {
		Cage cage = cageDao.find(pet.getCageFk());
		cage.setRemaining(Math.min(cage.getRemaining()+1, cage.getCapacity()));
		pet.setCageFk(null);
		petDao.update(pet);
		cageDao.update(cage);
	}

}
