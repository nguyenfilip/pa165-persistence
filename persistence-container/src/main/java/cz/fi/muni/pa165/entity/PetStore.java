package cz.fi.muni.pa165.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfOpening;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date openTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date closeTime;
	
//	@OneToMany(mappedBy="petStore")
	private Set<Cage> cages = new HashSet<Cage>();
	
	/**
	 * Attributes of the embedded class will be part of the resulting entity
	 */
	private Address address;
	
	/**
	 * This should be ElementCollection. Not OneToMany! Note that the ElementCollection will
	 * inevitably create a new database table. However, you won't have direct access to the 
	 * Embeddable ElementCollection. 
	 */
	private Set<Address> previousAddresses = new HashSet<Address>();
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
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
