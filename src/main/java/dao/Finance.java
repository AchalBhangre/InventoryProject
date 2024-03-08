package dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Finance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	private String name; 
	private String cost;
	private String purchaseTimePlanned;
	
	public Finance() {
        // Default constructor required by JPA
    }
	
	public Integer getId() {
		return id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getPurchaseTimePlanned() {
		return purchaseTimePlanned;
	}

	

	public void setPurchaseTimePlanned(String purchaseTimePlanned) {
		this.purchaseTimePlanned = purchaseTimePlanned;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", cost=" + cost + ", purchaseTimePlanned=" + purchaseTimePlanned
				+ "]";
	}

	

	

	

}
