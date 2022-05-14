package com.emretopcu.shoppingwebservice.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="supplier")
@NamedQuery(name="retrieve_suppliers", query="select s from Supplier s where (:name is null or s.name like :name) "
		+ "and (:country is null or s.country = :country)")
public class Supplier {

	@Id
	@Size(min=1, max=10, message="Id must have between 1-10 characters.")
	private String id;
	
	@Size(min=1, max=50, message="Name must have between 1-50 characters.")
	private String name;
	
	@Size(min=1, max=20, message="Country must have between 1-20 characters.")
	private String country;
	
	public Supplier() {
		
	}

	public Supplier(@Size(min = 1, max = 10, message = "Id must have between 1-10 characters.") String id,
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
