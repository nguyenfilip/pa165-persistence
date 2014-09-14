package cz.fi.muni.pa165.tier;

public class Pet {

	private int id;
	private String name;
	private String typename;
	private Integer cageFk;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Integer getCageFk() {
		return cageFk;
	}
	public void setCageFk(Integer cageFk) {
		this.cageFk = cageFk;
	}
	@Override
	public String toString() {
		return "Pet [id=" + id + ", name=" + name + ", typename=" + typename
				+ ", cageFk=" + cageFk + "]";
	}
	
}
