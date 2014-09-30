package cz.fi.muni.pa165.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents a building that is a PetStore. A PetStore contains many Cages and those cages include Pets.
 * 
 * @author Filip Nguyen
 *
 */
@Entity
public class PetStore {
	@Id
	@GeneratedValue
	private long id = 0;
	
	/**
	 * textIdentifier is a String that uniquelly identifies a PetStore e.g. "PSBRN1"
	 * The max lenght of this identifier is 10 characters and the identifier cannot be null
	 *  
	 */
	@Column(unique=true, length=10,nullable=false)
	private String textIdentifier;
	
	@Temporal(TemporalType.DATE)
	private Date dateOfOpening;
	
	@Temporal(TemporalType.TIME)
	private Date openTime;
	
	@Temporal(TemporalType.TIME)
	private Date closeTime;
	
	@OneToMany(mappedBy="petStore")
	private Set<Cage> cages = new HashSet<Cage>();
	
	/**
	 * Attributes of the embedded class will be part of the resulting entity
	 */
	@Embedded
	private Address address;
	
	@ElementCollection(fetch=FetchType.EAGER)
        @OrderBy(value = "street asc")        
	private List<Address> previousAddresses = new ArrayList<Address>();
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public List<Address> getPreviousAddresses() {
		return previousAddresses;
	}
	public void setPreviousAddresses(List<Address> previousAddresses) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PetStore other = (PetStore) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
