package cz.fi.muni.pa165.dto;

import cz.fi.muni.pa165.entity.Pet.PetColor;

public class PetsByColorDTO {
	private PetColor color;
	private Long  petCount;
	
	
	
	
	public PetsByColorDTO(PetColor color, Long petCount) {
		super();
		this.color = color;
		this.petCount = petCount;
	}
	public PetColor getColor() {
		return color;
	}
	public void setColor(PetColor color) {
		this.color = color;
	}
	public Long  getPetCount() {
		return petCount;
	}
	public void setPetCount(Long  petCount) {
		this.petCount = petCount;
	}
	
	
	
}
