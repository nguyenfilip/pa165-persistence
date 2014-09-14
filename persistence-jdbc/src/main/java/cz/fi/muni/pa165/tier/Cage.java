package cz.fi.muni.pa165.tier;

public class Cage {
	
	private int id;
	private String description;
	private int capacity;
	private int remaining;
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	@Override
	public String toString() {
		return "Cage [id=" + id + ", description=" + description
				+ ", capacity=" + capacity + ", remaining=" + remaining + "]";
	}
	
	
	
	
}
