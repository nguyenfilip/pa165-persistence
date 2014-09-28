package cz.fi.muni.pa165.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

/**
 * Represents a building that is a PetStore. A PetStore contains many Cages and those cages include Pets.
 * 
 * @author Filip Nguyen
 *
 */

public class PetStore {
	private long id = 0;
	
	/**
	 * textIdentifier is a String that uniquely identifies a PetStore e.g. "PSBRN1"
	 * The max length of this identifier is 10 characters and the identifier cannot be null
	 *  
	 */
	private String textIdentifier;
	
	@Transient
	private Date dateOfOpening;
	
	@Transient
	private Date openTime;
	
	@Transient
	private Date closeTime;
	
//	@OneToMany(mappedBy="petStore")
	private Set<Cage> cages = new HashSet<Cage>();
	
	/**
	 * Attributes of the embedded class will be part of the resulting entity
	 */
//	private Address address;
	
	/**
	 * This should be ElementCollection. Not OneToMany! Note that the ElementCollection will
	 * inevitably create a new database table. However, you won't have direct access to the 
	 * Embeddable ElementCollection. 
	 */
	@Transient
	private Set<Address> previousAddresses = new HashSet<Address>();
	

	public Set<Address> getPreviousAddresses() {
		return previousAddresses;
	}
	public void setPreviousAddresses(Set<Address> previousAddresses) {
		this.previousAddresses = previousAddresses;
	}
	public String getTextIdentifier() {
		return textIdentifier;
	}
	public void setTextIdentifier(String textIdentifier) {
		this.textIdentifier = textIdentifier;
	}
	public Date getDateOfOpening() {
		return dateOfOpening;
	}
	public void setDateOfOpening(Date dateOfOpening) {
		this.dateOfOpening = dateOfOpening;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Set<Cage> getCages() {
		return cages;
	}
	public void setCages(Set<Cage> cages) {
		this.cages = cages;
	}

	public void addPreviousAddress(Address previous) {
		previousAddresses.add(previous);
	}

}
