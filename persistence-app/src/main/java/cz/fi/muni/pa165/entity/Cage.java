package cz.fi.muni.pa165.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;



public class Cage {

	private long id = -1;

	private String description;
	private int capacity;
	
	@Transient
	private Set<Pet> pets = new HashSet<Pet>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Set<Pet> getPets() {
		return pets;
	}

	public void setPets(Set<Pet> pets) {
		this.pets = pets;
	}

	@Override
	public String toString() {
		return "Cage [id=" + id + ", description=" + description
				+ ", capacity=" + capacity  + "]";
	}
	
	

	
	
}
