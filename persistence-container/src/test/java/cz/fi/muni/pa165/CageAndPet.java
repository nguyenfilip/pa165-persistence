package cz.fi.muni.pa165;

import cz.fi.muni.pa165.entity.Cage;
import cz.fi.muni.pa165.entity.Pet;

public class CageAndPet {
	private Cage cage;
	private Pet pet;
	public CageAndPet(Cage cage, Pet pet) {
		super();
		this.cage = cage;
		this.pet = pet;
	}
	public Cage getCage() {
		return cage;
	}
	public void setCage(Cage cage) {
		this.cage = cage;
	}
	public Pet getPet() {
		return pet;
	}
	public void setPet(Pet pet) {
		this.pet = pet;
	}
}
