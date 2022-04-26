package com.emretopcu.shoppingwebservice.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="customer")
@NamedQuery(name="retrieve_customers", query="select c from Customer c where (:name is null or c.name like :name) "
		+ "and (:country is null or c.country = :country)")
public class Customer {
	
	@Id
	@Size(min=1, max=10, message="Id must have between 1-10 characters.")
	private String id;
	
	@Size(min=1, max=50, message="Name must have between 1-50 characters.")
	private String name;
	
	@Size(min=1, max=20, message="Country must have between 1-20 characters.")
	private String country;
	
	public Customer() {

	}

	public Customer(@Size(min = 1, max = 10, message = "Id must have between 1-10 characters.") String id,
			@Size(min = 1, max = 50, message = "Name must have between 1-50 characters.") String name,
			@Size(min = 1, max = 20, message = "Country must have between 1-20 characters.") String country) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
